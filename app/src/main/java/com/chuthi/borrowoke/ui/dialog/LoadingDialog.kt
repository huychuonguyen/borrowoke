package com.chuthi.borrowoke.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseDialogFragment

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 2023/01/30
- Project : Chibijob
 **************************************/

/**
 * LoadingDialog.
 * Based on [BaseDialogFragment]
 */
class LoadingDialog : BaseDialogFragment() {

    override fun contentViewId() = R.layout.dialog_loading

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.run {
            setCanceledOnTouchOutside(false)
            isCancelable = false
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setDimAmount(DIM_AMOUNT)
        }
    }

    override fun onStart() {
        super.onStart()
        // set fullscreen
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        const val TAG = "LoadingDialog"
        private const val DIM_AMOUNT = 0.3f
    }
}