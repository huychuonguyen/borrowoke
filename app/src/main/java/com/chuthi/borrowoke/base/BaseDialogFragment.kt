package com.chuthi.borrowoke.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 2023/01/30
- Project : Chibijob
 **************************************/
abstract class BaseDialogFragment : DialogFragment() {

    @LayoutRes
    abstract fun contentViewId(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(contentViewId(), container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}