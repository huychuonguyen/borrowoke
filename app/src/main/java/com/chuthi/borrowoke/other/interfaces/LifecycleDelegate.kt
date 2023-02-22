package com.chuthi.borrowoke.other.interfaces

import androidx.lifecycle.LifecycleOwner

interface LifecycleDelegate {
    fun registerLifecycleOwner(owner: LifecycleOwner)
}