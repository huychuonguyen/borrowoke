package com.chuthi.borrowoke.ext

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.chuthi.borrowoke.base.BaseAdapter


fun <T : Adapter<*>> RecyclerView.setupLinearLayout(adapter: T) = this.apply {
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}


fun <T : BaseAdapter<*, *>> RecyclerView.setupGridLayout(adapter: T) {
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}

fun RecyclerView.scrollToEnd(isSmoothScroll: Boolean = false) = post {
    val adt = this.adapter
    adt ?: return@post
    val position = adt.itemCount - 1
    if (position >= 0)
        if (isSmoothScroll) smoothScrollToPosition(adt.itemCount - 1)
        else scrollToPosition(adt.itemCount - 1)
}

fun RecyclerView.supportChangeAnimation(isAnimate: Boolean) = this.apply {
    itemAnimator = if (isAnimate)
        DefaultItemAnimator()
    else null
}
