package com.chuthi.borrowoke.base.interfaces

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.other.interfaces.LifecycleDelegate

abstract class LifecycleObserverActivity : AppCompatActivity(), LifecycleObserver, IDataObserver

abstract class LifecycleObserverFragment : Fragment(), LifecycleObserver, IDataObserver

interface LifecycleObserver : LifecycleDelegate, LifecycleEventObserver {

    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                when (this) {
                    is AppCompatActivity -> Log.i(
                        "LifecycleObserver_ON_RESUME", "activity: ${this::class.java.simpleName}"
                    )

                    is Fragment -> Log.i(
                        "LifecycleObserver_ON_RESUME", "fragment: ${this::class.java.simpleName}"
                    )

                    else -> Unit
                }
            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_DESTROY -> {
                when (this) {
                    is AppCompatActivity -> Log.i(
                        "LifecycleObserver_ON_DESTROY", "activity: ${this::class.java.simpleName}"
                    )

                    is Fragment -> Log.i(
                        "LifecycleObserver_ON_DESTROY", "fragment: ${this::class.java.simpleName}"
                    )

                    else -> Unit
                }
            }

            else -> Unit
        }
    }
}