package com.haikalzain.inventorypro.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haikalzain.inventorypro.databinding.FieldPreviewBinding
import com.haikalzain.inventorypro.extensions.setAll
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.FieldItem
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory
import com.haikalzain.inventorypro.utils.Mover

class FieldAdapter(val context: Context) :
    RecyclerView.Adapter<FieldAdapter.ViewHolder>() {
    var itemClickListener: ItemClickListener<FieldItem>? = null
    private val items = mutableListOf<FieldItem>()
    var itemMover: ItemClickListener<Mover>?=null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FieldPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=FieldPreviewBinding.bind(holder.itemView)
        val item=items[position]
        val fieldView = FieldViewFactory.createFieldViewForType(context, item.fieldType, item.name)


        binding.container.addView(fieldView)
        binding.deleteBtn.isEnabled = item.deletable
        binding.deleteBtn.setOnClickListener {

            itemClickListener?.onItemClickListener(item)}
        fieldView?.disableInput()
    }

    fun updateDataSet(newList: List<FieldItem>) {
        items.setAll(newList.toMutableList())
        notifyDataSetChanged()
        //   initObservers()
    }
    fun moveItem(from: Int, to: Int) {
        itemMover?.onItemClickListener(Mover(from, to))
    }
}