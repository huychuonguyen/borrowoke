package com.chuthi.borrowoke.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private var countDown: CountDownTimer? = null

    override fun layoutId() = R.layout.activity_splash

    override fun setupUI() {
        // delay splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // move to Main
            startCountDown()
        }, DELAY_SPLASH)
    }

    override fun onDestroy() {
        // clear count down
        countDown?.cancel()
        countDown = null

        super.onDestroy()
    }

    private fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startCountDown() {
        val tvCountDown = tvCountDown
        var countdownValue = COUNTDOWN_DURATION / 1000L
        countDown = object : CountDownTimer(COUNTDOWN_DURATION, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                tvCountDown.text = (countdownValue--).toString()
            }

            override fun onFinish() {
                moveToMain()
            }

        }
        countDown?.start()
    }

    companion object {
        private const val DELAY_SPLASH = 1000L
        private const val COUNTDOWN_DURATION = 3000L
    }
}