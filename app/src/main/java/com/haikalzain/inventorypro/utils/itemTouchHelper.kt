package com.haikalzain.inventorypro.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikalzain.inventorypro.ui.adapter.FieldAdapter
import com.haikalzain.inventorypro.ui.adapter.TemplateAdapter

 val itemTouchHelper by lazy {
    // 1. Note that I am specifying all 4 directions.
    //    Specifying START and END also allows
    //    more organic dragging than just specifying UP and DOWN.
    val simpleItemTouchCallback =
        object : SimpleCallback(UP or
                DOWN or
                START or
                END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {

                val adapter = recyclerView.adapter as FieldAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                // 2. Update the backing model. Custom implementation in
                //    MainRecyclerViewAdapter. You need to implement
                //    reordering of the backing model inside the method.
               if(from>2 && to>2)
                   adapter.moveItem(from, to)
                else Toast.makeText(adapter.context, "Can't Move protected field", Toast.LENGTH_SHORT).show()
                // 3. Tell adapter to render the model update.

                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {
                // 4. Code block for horizontal swipe.
                //    ItemTouchHelper handles horizontal swipe as well, but
                //    it is not relevant with reordering. Ignoring here.
            }
        }
    ItemTouchHelper(simpleItemTouchCallback)
}