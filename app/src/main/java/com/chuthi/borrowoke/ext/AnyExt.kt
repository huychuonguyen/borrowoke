package com.chuthi.borrowoke.ext

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.data.model.ParcelizeData

fun Any.toParcelableData(): BaseModel? {
    return when (this) {
        is BaseModel -> this
        is String -> ParcelizeData.StringData(this)
        is Int -> ParcelizeData.IntData(this)
        is Float -> ParcelizeData.FloatData(this)
        is Double -> ParcelizeData.DoubleData(this)
        is Number -> ParcelizeData.NumberData(this)
        is Boolean -> ParcelizeData.BooleanData(this)
        else -> null
    }
}

fun Any.navigateTo(
    action: NavDirections,
    @IdRes viewId: Int? = null,
    isAnimation: Boolean = false
) {
    // define navigation options
    val navOptions =
        NavOptions.Builder()
            // set animation
            .setEnterAnim(androidx.transition.R.anim.abc_slide_in_top)
            .setExitAnim(androidx.transition.R.anim.abc_slide_out_bottom)
            .setPopEnterAnim(androidx.transition.R.anim.abc_slide_in_bottom)
            .setPopEnterAnim(androidx.transition.R.anim.abc_slide_out_top)
            // build
            .build()

    // navigate based on each component type
    when (this) {
        is View -> findNavController().navigate(
            action,
            if (isAnimation) navOptions else null
        )

        is Fragment -> findNavController().navigate(
            action,
            if (isAnimation) navOptions else null
        )

        is Activity -> if (viewId != null)
            findNavController(viewId).navigate(
                action,
                if (isAnimation) navOptions else null
            )

        else -> Unit
    }
}