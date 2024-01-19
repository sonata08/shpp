package com.example.myprofile.ui.main.contacts.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Item decoration for adding spacing and margins between items in a RecyclerView.
 *
 * @param sideMargin The side margin (left and right) applied to each item in pixels.
 * @param marginBetween The vertical spacing between items in pixels.
 * @param marginBottom The bottom margin applied only to the last item in pixels.
 */
class ContactsItemDecoration(
    private val sideMargin: Int,
    private val marginBetween: Int,
    private val marginBottom: Int,
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

        outRect.top = spacing
        outRect.left = sideSpacing
        outRect.right = sideSpacing

        if (isLastItem(parent, view)) {
            outRect.bottom = spacingBottom
        }
    }

    private fun isLastItem(parent: RecyclerView, view: View): Boolean {
        return parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)
    }
}