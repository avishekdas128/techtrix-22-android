package com.orangeink.techtrix.util.qr

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import java.util.*

class QREncoder {

    var colorWhite = -0x1
    var colorBlack = -0x1000000
    private var dimension = Int.MIN_VALUE
    private var contents: String? = null
    private var displayContents: String? = null
    var title: String? = null
        private set
    private var format: BarcodeFormat? = null
    private var encoded = false

    constructor(data: String?, type: String?) {
        encoded = encodeContents(data, null, QRContents.Type.TEXT)
    }

    constructor(data: String?, type: String?, dimension: Int) {
        this.dimension = dimension
        encoded = encodeContents(data, null, QRContents.Type.TEXT)
    }

    constructor(data: String?, bundle: Bundle?, type: String, dimension: Int) {
        this.dimension = dimension
        encoded = encodeContents(data, bundle, type)
    }

    private fun encodeContents(data: String?, bundle: Bundle?, type: String): Boolean {
        // Default to QR_CODE if no format given.
        format = BarcodeFormat.QR_CODE
        if (format == BarcodeFormat.QR_CODE) {
            format = BarcodeFormat.QR_CODE
            encodeQRCodeContents(data, bundle, type)
        } else if (data != null && data.isNotEmpty()) {
            contents = data
            displayContents = data
            title = "Text"
        }
        return contents != null && contents!!.isNotEmpty()
    }

    private fun encodeQRCodeContents(data: String?, bundle: Bundle?, type: String) {
        var data = data
        when (type) {
            QRContents.Type.TEXT -> if (data != null && data.isNotEmpty()) {
                contents = data
                displayContents = data
                title = "Text"
            }
            QRContents.Type.EMAIL -> {
                data = trim(data)
                if (data != null) {
                    contents = "mailto:$data"
                    displayContents = data
                    title = "E-Mail"
                }
            }
            QRContents.Type.PHONE -> {
                data = trim(data)
                if (data != null) {
                    contents = "tel:$data"
                    displayContents = PhoneNumberUtils.formatNumber(data)
                    title = "Phone"
                }
            }
            QRContents.Type.SMS -> {
                data = trim(data)
                if (data != null) {
                    contents = "sms:$data"
                    displayContents = PhoneNumberUtils.formatNumber(data)
                    title = "SMS"
                }
            }
            QRContents.Type.CONTACT -> if (bundle != null) {
                val newContents = StringBuilder(100)
                val newDisplayContents = StringBuilder(100)
                newContents.append("VCARD:")
                val name = trim(bundle.getString(ContactsContract.Intents.Insert.NAME))
                if (name != null) {
                    newContents.append("N:").append(escapeVCard(name)).append(';')
                    newDisplayContents.append(name)
                }
                val address = trim(bundle.getString(ContactsContract.Intents.Insert.POSTAL))
                if (address != null) {
                    newContents.append("ADR:").append(escapeVCard(address)).append(';')
                    newDisplayContents.append('\n').append(address)
                }
                val uniquePhones: MutableCollection<String> = HashSet(QRContents.PHONE_KEYS.size)
                run {
                    var x = 0
                    while (x < QRContents.PHONE_KEYS.size) {
                        val phone =
                            trim(bundle.getString(QRContents.PHONE_KEYS.get(x)))
                        if (phone != null) {
                            uniquePhones.add(phone)
                        }
                        x++
                    }
                }
                for (phone in uniquePhones) {
                    newContents.append("TEL:").append(escapeVCard(phone)).append(';')
                    newDisplayContents.append('\n').append(PhoneNumberUtils.formatNumber(phone))
                }
                val uniqueEmails: MutableCollection<String> = HashSet(QRContents.EMAIL_KEYS.size)
                var x = 0
                while (x < QRContents.EMAIL_KEYS.size) {
                    val email = trim(bundle.getString(QRContents.EMAIL_KEYS[x]))
                    if (email != null) {
                        uniqueEmails.add(email)
                    }
                    x++
                }
                for (email in uniqueEmails) {
                    newContents.append("EMAIL:").append(escapeVCard(email)).append(';')
                    newDisplayContents.append('\n').append(email)
                }
                val url = trim(bundle.getString(QRContents.URL_KEY))
                if (url != null) {
                    // escapeVCard(url) -> wrong escape e.g. http\://zxing.google.com
                    newContents.append("URL:").append(url).append(';')
                    newDisplayContents.append('\n').append(url)
                }
                val note = trim(bundle.getString(QRContents.NOTE_KEY))
                if (note != null) {
                    newContents.append("NOTE:").append(escapeVCard(note)).append(';')
                    newDisplayContents.append('\n').append(note)
                }

                // Make sure we've encoded at least one field.
                if (newDisplayContents.length > 0) {
                    newContents.append(';')
                    contents = newContents.toString()
                    displayContents = newDisplayContents.toString()
                    title = "Contact"
                } else {
                    contents = null
                    displayContents = null
                }
            }
            QRContents.Type.LOCATION -> if (bundle != null) {
                // These must use Bundle.getFloat(), not getDouble(), it's part of the API.
                val latitude = bundle.getFloat("LAT", Float.MAX_VALUE)
                val longitude = bundle.getFloat("LONG", Float.MAX_VALUE)
                if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
                    contents = "geo:$latitude,$longitude"
                    displayContents = "$latitude,$longitude"
                    title = "Location"
                }
            }
        }
    }

    // All are 0, or black, by default
    val bitmap: Bitmap?
        get() = if (!encoded) null else try {
            var hints: MutableMap<EncodeHintType?, Any?>? = null
            val encoding = guessAppropriateEncoding(contents)
            if (encoding != null) {
                hints = EnumMap(EncodeHintType::class.java)
                hints[EncodeHintType.CHARACTER_SET] = encoding
            }
            val writer = MultiFormatWriter()
            val result = writer.encode(contents, format, dimension, dimension, hints)
            val width = result.width
            val height = result.height
            val pixels = IntArray(width * height)
            // All are 0, or black, by default
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (result[x, y]) colorBlack else colorWhite
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (ex: Exception) {
            null
        }

    private fun guessAppropriateEncoding(contents: CharSequence?): String? {
        // Very crude at the moment
        for (i in 0 until contents!!.length) {
            if (contents[i].code > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }

    private fun trim(s: String?): String? {
        if (s == null) {
            return null
        }
        val result = s.trim { it <= ' ' }
        return result.ifEmpty { null }
    }

    private fun escapeVCard(input: String?): String? {
        if (input == null || input.indexOf(':') < 0 && input.indexOf(';') < 0) {
            return input
        }
        val length = input.length
        val result = StringBuilder(length)
        for (i in 0 until length) {
            val c = input[i]
            if (c == ':' || c == ';') {
                result.append('\\')
            }
            result.append(c)
        }
        return result.toString()
    }
}