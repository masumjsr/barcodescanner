package com.haikalzain.inventorypro.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haikalzain.inventorypro.databinding.StartMenuItemBinding
import com.haikalzain.inventorypro.extensions.setAll
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.HomeItem

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var itemClickListener: ItemClickListener<HomeItem>? = null
    private val items = mutableListOf<HomeItem>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StartMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding:StartMenuItemBinding=StartMenuItemBinding.bind(holder.itemView)
        val item=items[position]
        binding.textView.text=item.title
        binding.imageView.setImageResource(item.image)
        binding.root.setOnClickListener {
            itemClickListener?.onItemClickListener(item)
        }
    }

    fun updateDataSet(newList: List<HomeItem>) {
        items.setAll(newList.toMutableList())
        notifyDataSetChanged()
        //   initObservers()
    }
}