package com.chus.clua.citybikes.presentation.features.networkslist

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView

class NetWorkSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SearchView(context, attributeSet, defStyleAttr) {

    private var listener: (() -> Unit)? = null

    override fun onActionViewCollapsed() {
        super.onActionViewCollapsed()
        listener?.invoke()
    }

    fun setOnActionViewCollapsed(block: () -> Unit) {
        listener = block
    }

}