package com.example.github_ui.utils

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> Fragment.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}

fun View.show(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@Suppress("DEPRECATION")
fun TextView.setTextFromHtml(text: String?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT))
    } else {
        setText(Html.fromHtml(text))
    }
}