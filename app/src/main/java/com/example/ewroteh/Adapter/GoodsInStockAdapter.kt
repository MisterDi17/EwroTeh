package com.example.ewroteh.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ewroteh.Entity.GoodsInStock
import com.example.ewroteh.Model.dbRepository
import com.example.ewroteh.R

class GoodsInStockAdapter(
    var goodsInStockList: MutableList<GoodsInStock>,
    private val GoodsInStockDeleteListener: (Int) -> Unit,
    private val GoodsInStockEditListener: (GoodsInStock, Int) -> Unit,
    private val repository: dbRepository
)
    : RecyclerView.Adapter<GoodsInStockAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGoodsInStockName = itemView.findViewById<TextView>(R.id.tvGoodsInStockName)
        val tvGoodsInStockType = itemView.findViewById<TextView>(R.id.tvGoodsInStockType)
        val tvGoodsInStockCount = itemView.findViewById<TextView>(R.id.tvGoodsInStockCount)
        val tvGoodsInStockPrice = itemView.findViewById<TextView>(R.id.tvGoodsInStockPrice)
        val btEdit = itemView.findViewById<ImageView>(R.id.imEdit)
        val btDelete = itemView.findViewById<ImageView>(R.id.imDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goods_in_stock_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsInStockList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var goodsInStock = goodsInStockList[position]
        holder.tvGoodsInStockName.text = goodsInStock.name
        holder.tvGoodsInStockType.text = repository.getGoodsTypeName(goodsInStock.goodsTypeId)
        holder.tvGoodsInStockCount.text = "Количество: ${goodsInStock.goodsCount.toString()}"
        holder.tvGoodsInStockPrice.text = "Стоимость: ${goodsInStock.price.toString()} руб."
        holder.btDelete.setOnClickListener {
            GoodsInStockDeleteListener(position)
            goodsInStockList.removeAt(position)
            notifyItemRemoved(position)
            update(goodsInStockList)
        }
        holder.btEdit.setOnClickListener {
            GoodsInStockEditListener(goodsInStock, position)
            notifyItemChanged(position)
        }
    }
    fun update(newList: MutableList<GoodsInStock>) {
        goodsInStockList = newList
        notifyDataSetChanged()
    }
}