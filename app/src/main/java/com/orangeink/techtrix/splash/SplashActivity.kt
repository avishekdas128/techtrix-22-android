package com.orangeink.techtrix.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orangeink.techtrix.common.MainActivity
import com.orangeink.techtrix.databinding.ActivitySplashBinding
import com.orangeink.techtrix.preferences.Prefs
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Prefs(this).home = null
        Prefs(this).notifications = null
        decideNextPage()
    }

    private fun decideNextPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}