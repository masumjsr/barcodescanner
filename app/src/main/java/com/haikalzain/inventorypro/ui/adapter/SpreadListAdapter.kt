package com.haikalzain.inventorypro.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haikalzain.inventorypro.databinding.SpreadListItemBinding
import com.haikalzain.inventorypro.extensions.setAll
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.TemplateItem

class SpreadListAdapter :
    RecyclerView.Adapter<SpreadListAdapter.ViewHolder>() {
    var itemClickListener: ItemClickListener<TemplateItem>? = null
    var itemMenuClickListener: ItemClickListener<TemplateItem>? = null

     val items = mutableListOf<TemplateItem>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SpreadListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=SpreadListItemBinding.bind(holder.itemView)
        val item=items[position]
        binding.textView.text=item.title
        binding.root.setOnClickListener {
            itemClickListener?.onItemClickListener(item)
        }

        binding.more.setOnClickListener {
            itemMenuClickListener?.onItemClickListener(item)
        }
    }

    fun updateDataSet(newList: List<TemplateItem>) {
        items.setAll(newList.toMutableList())
        notifyDataSetChanged()
        //   initObservers()
    }
}