package com.chuthi.borrowoke.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Show Toast
 */
fun AppCompatActivity.showToast(
    mess: String? = null,
    isLengthLong: Boolean = false
) {
    if (mess.isNullOrEmpty()) return
    Toast.makeText(
        this,
        mess,
        if (isLengthLong) Toast.LENGTH_LONG
        else Toast.LENGTH_SHORT
    ).show()
}

/**
 * repeatOnLifecycle extension
 * based on lifeCycle's [CoroutineScope] of activity
 */
fun AppCompatActivity.repeatOnLifecycle(
    action: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.invoke(this)
        }
    }
}

fun AppCompatActivity.popBackStack() = supportFragmentManager
    .popBackStack()

fun MainActivity.navigateTo(action: NavDirections) {
    val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.navHostFragment) as? NavHostFragment
    val navController = navHostFragment?.navController
    // navigate to destination via action
    navController?.navigate(action)
}