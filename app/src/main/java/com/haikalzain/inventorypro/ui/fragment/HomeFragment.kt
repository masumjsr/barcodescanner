package com.haikalzain.inventorypro.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikalzain.inventorypro.databinding.FragmentHomeBinding
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.HomeItem
import com.haikalzain.inventorypro.model.homeItemList
import com.haikalzain.inventorypro.ui.adapter.HomeAdapter


class HomeFragment : Fragment() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    var adapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        activity?.let {

            adapter = HomeAdapter()
            adapter?.itemClickListener = object : ItemClickListener<HomeItem> {
                override fun onItemClickListener(item: Any) {
                    val data = item as HomeItem
                    when(data.id){
                        0->findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSpreadListFragment())
                        1->findNavController().navigate( HomeFragmentDirections.actionHomeFragmentToTemplateListFragment())
                        2->findNavController().navigate( HomeFragmentDirections.actionHomeFragmentToAboutFragment())
                    }

                }

            }
            adapter?.updateDataSet(homeItemList)
            viewBinding.homeRecycler.layoutManager = LinearLayoutManager(it)
            viewBinding.homeRecycler.adapter = adapter
        }
        return viewBinding.root
    }


}