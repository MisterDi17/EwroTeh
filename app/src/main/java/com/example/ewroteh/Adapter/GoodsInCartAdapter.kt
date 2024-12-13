package com.example.ewroteh.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ewroteh.Entity.GoodsInStock
import com.example.ewroteh.Model.dbRepository
import com.example.ewroteh.R

class GoodsInCartAdapter(
    var goodsInStockList: MutableList<GoodsInStock>,
    private val GoodsInStockAddListener: (GoodsInStock, Int) -> Unit,
    private val GoodsInStockRemoveListener: (GoodsInStock, Int) -> Unit,
    private val repository: dbRepository
)
    : RecyclerView.Adapter<GoodsInCartAdapter.ViewHolder>()  { var saleId: Int = repository.getOrCreateSaleId()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGoodsInStockName = itemView.findViewById<TextView>(R.id.tvGoodsInStockName)
        val tvGoodsAddedCount = itemView.findViewById<TextView>(R.id.tvGoodsAddedCount)
        val btRemove = itemView.findViewById<ImageView>(R.id.imRemove)
        val btAdd = itemView.findViewById<ImageView>(R.id.imAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goods_in_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goodsInStockList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var goodsInStock = goodsInStockList[position]
        holder.tvGoodsInStockName.text = goodsInStock.name
        holder.tvGoodsAddedCount.text = repository.getGoodsCountInCart(goodsInStock.goodId, saleId).toString()
        holder.btAdd.setOnClickListener {
            val success = repository.addGoodsToCart(goodsInStock.goodId, saleId)
            if (success) {
                GoodsInStockAddListener(goodsInStock, position)
                notifyItemChanged(position)
            } else {
                Toast.makeText(holder.itemView.context, "На складе недостаточно товаров", Toast.LENGTH_SHORT).show()
            }
        }
        holder.btRemove.setOnClickListener {
            repository.removeGoodsFromCart(goodsInStock.goodId, saleId)
            GoodsInStockRemoveListener(goodsInStock, position)
            notifyItemChanged(position)
        }
    }
}