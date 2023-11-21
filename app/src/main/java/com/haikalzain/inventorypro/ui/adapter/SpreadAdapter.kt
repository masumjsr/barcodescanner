package com.haikalzain.inventorypro.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haikalzain.inventorypro.common.Field
import com.haikalzain.inventorypro.common.Item
import com.haikalzain.inventorypro.databinding.SpreadItemBinding
import com.haikalzain.inventorypro.extensions.setAll
import com.haikalzain.inventorypro.iface.ItemClickListener

class SpreadAdapter(
    val context: Context,
) :
    RecyclerView.Adapter<SpreadAdapter.ViewHolder>() {
    var itemClickListener: ItemClickListener<Item>? = null
    var itemDeleteClickListener:ItemClickListener<Item>?=null
    var multiSelectListener:ItemClickListener<Boolean> ?=null
    var totalSelectListener:ItemClickListener<Int>?=null
     val items = mutableListOf<Item>()
    var isMultiSelectionMode=false
    var selectedItem= mutableListOf<Item>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SpreadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding=SpreadItemBinding.bind(holder.itemView)
        val item=items[position]

    val fields=ArrayList<Field>()
        val list = ArrayList<String>()

       item.forEach {
           if (it != null) {
               fields.add(it)
               list.add(it.value)
           }
       }
        val fieldAdapter=FieldDataAdapter()
        fieldAdapter.updateDataSet(fields)
        binding.view.setOnClickListener {
           if (isMultiSelectionMode)
           {
               if(selectedItem.contains(item))selectedItem.remove(item)
               else selectedItem.add(item)
               totalSelectListener?.onItemClickListener(selectedItem.size)

               notifyItemChanged(position)

           }
               else itemClickListener?.onItemClickListener(item)
        }
        binding.layer.isVisible=selectedItem.contains(item)

        binding.view.setOnLongClickListener {
            multiSelectListener?.onItemClickListener(true)
            selectedItem.add(item)
            totalSelectListener?.onItemClickListener(selectedItem.size)

            isMultiSelectionMode=true
            notifyItemChanged(position)

            return@setOnLongClickListener true
        }
        binding.spreadRecycler.adapter=fieldAdapter
        binding.spreadRecycler.layoutManager= LinearLayoutManager(context)


        binding.root.setOnClickListener {
            itemClickListener?.onItemClickListener(item)
        }

    }

    fun updateDataSet(newList: List<Item>) {
        items.setAll(newList.toMutableList())
        notifyDataSetChanged()
        //   initObservers()
    }
}