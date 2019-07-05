package com.sharecare.gitusers.binding

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.TextView

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:intToTextView")
    fun intToTextView(view: TextView, number: Int) {
        view.text = number.toString()
    }
}
