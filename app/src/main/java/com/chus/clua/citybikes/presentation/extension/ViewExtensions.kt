package com.chus.clua.citybikes.presentation.extension

import android.view.View

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}