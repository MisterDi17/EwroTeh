package com.example.ewroteh.Entity

data class GoodsInStock(
    val goodId: Int,
    var name: String,
    var goodsCount: Int,
    var price: Double,
    var goodsTypeId: Int
)