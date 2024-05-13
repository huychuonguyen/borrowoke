package com.chuthi.borrowoke.base.interfaces

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.chuthi.borrowoke.other.interfaces.LifecycleDelegate

abstract class LifecycleObserverActivity : AppCompatActivity(), LifecycleObserver

abstract class LifecycleObserverFragment : Fragment(), LifecycleObserver


interface LifecycleObserver : LifecycleDelegate, LifecycleEventObserver, IDataObserver {

    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                when (this) {
                    is AppCompatActivity -> log.i(
                        "activity: ${this::class.java.simpleName}",
                        "LifecycleObserver_ON_RESUME"
                    )

                    is Fragment -> log.i(
                        "fragment: ${this::class.java.simpleName}",
                        "LifecycleObserver_ON_RESUME"
                    )

                    else -> Unit
                }
            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_DESTROY -> {
                when (this) {
                    is AppCompatActivity -> log.i(
                        "activity: ${this::class.java.simpleName}",
                        "LifecycleObserver_ON_DESTROY",
                    )

                    is Fragment -> log.i(
                        "fragment: ${this::class.java.simpleName}",
                        "LifecycleObserver_ON_DESTROY",
                    )

                    else -> Unit
                }
            }

            else -> Unit
        }
    }
}