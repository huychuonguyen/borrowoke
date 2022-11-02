package com.chuthi.borrowoke.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun setupUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set layout id
        setContentView(layoutId())
        // setup ui
        setupUI()
    }
}