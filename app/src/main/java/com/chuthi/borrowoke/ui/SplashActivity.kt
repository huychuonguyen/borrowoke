package com.chuthi.borrowoke.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.chuthi.borrowoke.base.BaseActivity
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.databinding.ActivitySplashBinding
import com.chuthi.borrowoke.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
class SplashActivity(override val viewModel: BaseViewModel? = null) :
    BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    private var countDown: CountDownTimer? = null

    override fun setViewBinding(inflater: LayoutInflater) = ActivitySplashBinding.inflate(inflater)

    override fun onObserveData(): Nothing? = null

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
        private const val COUNTDOWN_DURATION = 0L
    }
}
