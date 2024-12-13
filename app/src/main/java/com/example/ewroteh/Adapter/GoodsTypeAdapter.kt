package com.example.ewroteh.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ewroteh.Entity.GoodsType
import com.example.ewroteh.R

class GoodsTypeAdapter(
    var goodsTypeList: MutableList<GoodsType>,
    private val GoodsTypeDeleteListener: (Int) -> Unit,
    private val GoodsTypeEditListener: (GoodsType, Int) -> Unit)
    : RecyclerView.Adapter<GoodsTypeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGoodsTypeName = itemView.findViewById<TextView>(R.id.tvGoodsTypeName)
        val btEdit = itemView.findViewById<ImageView>(R.id.imEdit)
        val btDelete = itemView.findViewById<ImageView>(R.id.imDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goods_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var goodsType = goodsTypeList[position]
        holder.tvGoodsTypeName.text = goodsType.name
        holder.btDelete.setOnClickListener {
            GoodsTypeDeleteListener(position)
            goodsTypeList.removeAt(position)
            notifyItemRemoved(position)
            update(goodsTypeList)
        }
        holder.btEdit.setOnClickListener {
            GoodsTypeEditListener(goodsType, position)
            notifyItemChanged(position)
        }
    }
    fun update(newList: MutableList<GoodsType>) {
        goodsTypeList = newList
        notifyDataSetChanged()
    }
}