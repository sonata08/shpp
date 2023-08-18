package com.example.myprofile.ui.contacts.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ContactsItemDecoration(
    private val margin: Int,
    private val marginBottom: Int,
    private val backgroundShape: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // converts dp to px
        val spacing = view.resources.getDimensionPixelSize(margin)
        val spacingBottom = view.resources.getDimensionPixelSize(marginBottom)
        view.setBackgroundResource(backgroundShape)
        outRect.top = spacing
        outRect.left = spacing
        outRect.right = spacing

        if (isLastItem(parent, view, state)) {
            outRect.bottom = spacingBottom
        }
    }

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State): Boolean {
        return parent.getChildAdapterPosition(view) == state.itemCount - 1
    }
}