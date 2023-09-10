package com.example.myprofile.ui.activities.contacts.adapter

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R


abstract class SwipeToDeleteCallback :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val backgroundColor = ColorDrawable(recyclerView.context.getColor(R.color.blue))
        val deleteIcon = (ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete)!!)
        // change icon color
        val coloredIcon = DrawableCompat.wrap(deleteIcon).mutate()
        DrawableCompat.setTint(coloredIcon, recyclerView.context.getColor(R.color.white))

        val iconMargin = (itemView.height - coloredIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + ((itemView.height - coloredIcon.intrinsicHeight) / 2)
        val iconBottom = iconTop + coloredIcon.intrinsicHeight

        // the left/right margins of the recyclerView items
        val spacing =
            recyclerView.resources.getDimensionPixelSize(R.dimen.margin_between_items)

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + coloredIcon.intrinsicWidth
            coloredIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            backgroundColor.setBounds(
                itemView.left - spacing, itemView.top, dX.toInt() + spacing, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - coloredIcon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            coloredIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            backgroundColor.setBounds(
                itemView.right + dX.toInt(), itemView.top, itemView.right + spacing, itemView.bottom
            )
        } else { // view is unSwiped
            backgroundColor.setBounds(0, 0, 0, 0)
        }
        backgroundColor.draw(c)
        coloredIcon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}