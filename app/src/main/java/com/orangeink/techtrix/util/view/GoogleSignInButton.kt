package com.orangeink.techtrix.util.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.orangeink.techtrix.R


class GoogleSignInButton : AppCompatButton {
    /**
     * Text that user wants the button to have.
     * This overrides the default "Sign in with Google" text.
     */
    private var mText: String? = null

    /**
     * Flag to show the dark theme with Google's standard dark blue color.
     */
    private var mIsDarkTheme = false

    /**
     * Constructor
     *
     * @param context Context
     */
    constructor(context: Context) : super(context) {}

    /**
     * Constructor
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     */
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context, attributeSet, 0)
    }

    /**
     * Constructor
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     * @param defStyleAttr int
     */
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet, defStyleAttr)
    }

    /**
     * Initialize the process to get custom attributes from xml and set button params.
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     */
    private fun init(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        parseAttributes(context, attributeSet, defStyleAttr)
        setButtonParams()
    }

    /**
     * Parses out the custom attributes.
     *
     * @param context      Context
     * @param attributeSet AttributeSet
     */
    private fun parseAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        if (attributeSet == null) {
            return
        }

        // Retrieve styled attribute information from the styleable.
        val typedArray: TypedArray = context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.ButtonStyleable, defStyleAttr, 0)
        try {
            // Get text which user wants to set the button.
            mText = typedArray.getString(R.styleable.ButtonStyleable_android_text)
            // Get the attribute to check if user wants dark theme.
            mIsDarkTheme = typedArray.getBoolean(R.styleable.ButtonStyleable_isDarkTheme, false)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    /**
     * Set button parameters.
     */
    private fun setButtonParams() {
        // We need not have only upper case character.
        this.transformationMethod = null
        // Set button text
        setButtonText()
        // Set button text size
        setButtonTextSize()
        // Set button text color
        setButtonTextColor()
        // Set background of button
        setButtonBackground()
    }

    /**
     * Set the text size to standard as mentioned in guidelines.
     */
    private fun setButtonTextSize() {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    }

    /**
     * Check the theme and set background based on theme which is a selector.
     * The selector handles the background color when button is clicked.
     */
    private fun setButtonBackground() {
        val googleIconImageSelector: Int =
            if (mIsDarkTheme) R.drawable.dark_theme_google_icon_selector else R.drawable.light_theme_google_icon_selector
        setBackgroundResource(googleIconImageSelector)
    }

    /**
     * Check the theme and set text color based on theme.
     */
    private fun setButtonTextColor() {
        val textColor = if (mIsDarkTheme) R.color.white else R.color.black
        this.setTextColor(ContextCompat.getColor(context, textColor))
    }

    /**
     * If user has set text, that takes priority else use default button text.
     */
    private fun setButtonText() {
        if (mText == null || mText!!.isEmpty()) {
            mText = context.getString(R.string.continue_with_google)
        }
        this.text = mText
    }
}