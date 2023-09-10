package com.example.myprofile.ui.activities.contacts.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Draws dividers between items and background frame
class ContactsItemDecoration(
    private val sideMargin: Int,
    private val marginBetween: Int,
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
        val sideSpacing = view.resources.getDimensionPixelSize(sideMargin)
        val spacing = view.resources.getDimensionPixelSize(marginBetween)
        val spacingBottom = view.resources.getDimensionPixelSize(marginBottom)
        view.setBackgroundResource(backgroundShape)
        outRect.top = spacing
        outRect.left = sideSpacing
        outRect.right = sideSpacing

        if (isLastItem(parent, view, state)) {
            outRect.bottom = spacingBottom
        }
    }

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State): Boolean {
        return parent.getChildAdapterPosition(view) == state.itemCount - 1
    }
}