package com.example.ewroteh.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ewroteh.Entity.GoodsType
import com.example.ewroteh.R


class SelectGoodTypeAdapter(
    var goodsTypeList: MutableList<GoodsType>,
    val onItemClick: (GoodsType, Int) -> Unit) :
    RecyclerView.Adapter<SelectGoodTypeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGoodsTypeName = itemView.findViewById<TextView>(R.id.tvGoodsTypeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_good_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var goodsType = goodsTypeList[position]
        holder.tvGoodsTypeName.text = goodsType.name
        holder.itemView.setOnClickListener{
            onItemClick(goodsType, position)
        }
    }
}