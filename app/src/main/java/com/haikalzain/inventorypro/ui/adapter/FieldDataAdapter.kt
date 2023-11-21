package com.haikalzain.inventorypro.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haikalzain.inventorypro.common.Field
import com.haikalzain.inventorypro.databinding.FieldDataBinding
import com.haikalzain.inventorypro.extensions.setAll

class FieldDataAdapter() :
    RecyclerView.Adapter<FieldDataAdapter.ViewHolder>() {
    private val items = mutableListOf<Field>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FieldDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=FieldDataBinding.bind(holder.itemView)
        val item=items[position]
        binding.title.text=item.name
        binding.value.text=item.value

    }

    fun updateDataSet(newList: List<Field>) {
        items.setAll(newList.toMutableList())
        notifyDataSetChanged()
        //   initObservers()
    }
}