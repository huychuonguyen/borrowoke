package com.chuthi.borrowoke.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.other.interfaces.LifecycleDelegate

open class LifecycleObserverActivity : AppCompatActivity(), LifecycleDelegate,
    LifecycleEventObserver {

    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                //showToast("activity: ${this::class.java.simpleName}")
            }
            Lifecycle.Event.ON_PAUSE -> {

            }
            else -> Unit
        }
    }
}