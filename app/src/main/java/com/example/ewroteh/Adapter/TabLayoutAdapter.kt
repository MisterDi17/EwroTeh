package com.example.ewroteh.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ewroteh.View.GoodsInStockFragment
import com.example.ewroteh.View.GoodsTypeFragment

class TabLayoutAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> GoodsInStockFragment()
            1-> GoodsTypeFragment()
            else-> GoodsInStockFragment()
        }
    }

}