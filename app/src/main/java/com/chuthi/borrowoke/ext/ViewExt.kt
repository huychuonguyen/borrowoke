package com.chuthi.borrowoke.ext

import android.content.res.TypedArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.other.DEFAULT_CLICK_INTERVAL
import com.chuthi.borrowoke.util.SafeClickListener
import com.google.android.material.snackbar.Snackbar

/**
 * Register safe click listener with delay time
 * @param defaultInterval delay time
 */
fun View.onSafeClick(
    defaultInterval: Long = DEFAULT_CLICK_INTERVAL,
    @DrawableRes drawableId: Int = R.drawable.bg_click,
    onSafeClick: (View) -> Unit
) {
    // register on click with delay
    val safeClickListener = SafeClickListener(defaultInterval = defaultInterval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
    // set background effect
    background = ContextCompat.getDrawable(context, drawableId)

    /*val attrs = intArrayOf(android.R.attr.selectableItemBackground)
    val typedArray: TypedArray = this.context.obtainStyledAttributes(attrs)
    val backgroundResource = typedArray.getResourceId(0, 0)
    setBackgroundResource(backgroundResource)
    typedArray.recycle()*/
}

fun View.showSnackBar(
    message: String?,
    isLengthLong: Boolean = false,
    actionText: String? = null,
    action: () -> Unit = {}
) {
    message ?: return
    Snackbar
        .make(
            this,
            message,
            if (isLengthLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
        ).apply {
            actionText ?: return@apply
            setAction(actionText) {
                action.invoke()
            }
            setActionTextColor(ContextCompat.getColor(context, R.color.yellow))
        }
        .show()
}

// region Animation
fun View.toggleSlide(
    visibility: Int,
    slideEge: Int = Gravity.BOTTOM,
    duration: Long = 300L,
    onStart: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    val transition: Transition = Slide(slideEge).apply {
        this.duration = duration
        addTarget(id)
        addListener(object : TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                onStart.invoke()
            }

            override fun onTransitionEnd(transition: Transition) {
                onFinish.invoke()
            }

            override fun onTransitionCancel(transition: Transition) {
                onFinish.invoke()
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }
        })
    }
    TransitionManager.beginDelayedTransition(
        parent as ViewGroup, transition
    )
    setVisibility(visibility)
}

fun Guideline.setPercent(ratio: Float) {
    val glParams = layoutParams as? ConstraintLayout.LayoutParams
    glParams?.guidePercent = ratio
    layoutParams = glParams
}
// endregion

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}