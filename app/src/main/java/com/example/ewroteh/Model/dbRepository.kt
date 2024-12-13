package com.example.ewroteh.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.widget.Toast
import com.example.ewroteh.Entity.GoodsInStock
import com.example.ewroteh.Entity.GoodsType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class dbRepository(private val dbHelper: DatabaseHelper) {
    fun insertGoodsType(name: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_GOODS_TYPE_NAME, name)
        }
        db.insert(DatabaseHelper.TABLE_GOODS_TYPE, null, values)
        db.close()
    }

    fun getAllGoodsType(): MutableList<GoodsType> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_GOODS_TYPE}", null)
        val goodsTypes = mutableListOf<GoodsType>()

        with(cursor) {
            while (moveToNext()) {
                val goodsTypeId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_TYPE_ID))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_TYPE_NAME))
                goodsTypes.add(GoodsType(goodsTypeId, name))
            }
        }
        cursor.close()
        db.close()
        return goodsTypes
    }

    fun editGoodsType(goodsType: GoodsType) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_GOODS_TYPE_NAME, goodsType.name)
        }
        db.update(
            DatabaseHelper.TABLE_GOODS_TYPE,
            values,
            "${DatabaseHelper.COLUMN_GOODS_TYPE_ID} = ${goodsType.goodsTypeId}",
            null
        )
        db.close()
    }

    fun deleteGoodsType(goodsTypeId: Int, context: Context): Boolean {
        var db = dbHelper.readableDatabase
        val query = "SELECT COUNT(*) FROM ${DatabaseHelper.TABLE_GOODS_IN_STOCK} WHERE ${DatabaseHelper.COLUMN_GOODS_IN_STOCK_TYPE} = ?"
        val cursor = db.rawQuery(query, arrayOf(goodsTypeId.toString()))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        if (count > 0){
            Toast.makeText(context, "Невозможно удалить тип товара, так как он используется в одном или нескольких товарах", Toast.LENGTH_SHORT).show()
            return false
        }
        db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_GOODS_TYPE,
            "${DatabaseHelper.COLUMN_GOODS_TYPE_ID} = $goodsTypeId", null
        )
        db.close()
        return true
    }

    fun insertGoodsInStock(name: String, goodsCount: Int, price: Double, goodsTypedId: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_NAME, name)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT, goodsCount)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_PRICE, price)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_TYPE, goodsTypedId)
        }
        db.insert(DatabaseHelper.TABLE_GOODS_IN_STOCK, null, values)
        db.close()
    }

    fun getAllGoodsInStock(): MutableList<GoodsInStock> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_GOODS_IN_STOCK}", null)
        val goodsInStock = mutableListOf<GoodsInStock>()

        with(cursor) {
            while (moveToNext()) {
                val goodId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_NAME))
                val goodsCount = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT))
                val price = getDouble(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_PRICE))
                val goodsTypeId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_TYPE))
                goodsInStock.add(GoodsInStock(goodId, name, goodsCount, price, goodsTypeId))
            }
        }
        cursor.close()
        db.close()
        return goodsInStock
    }

    fun editGoodsInStock(goodsInStock: GoodsInStock) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_NAME, goodsInStock.name)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT, goodsInStock.goodsCount)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_PRICE, goodsInStock.price)
            put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_TYPE, goodsInStock.goodsTypeId)
        }
        db.update(
            DatabaseHelper.TABLE_GOODS_IN_STOCK,
            values,
            "${DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID} = ${goodsInStock.goodId}",
            null
        )
        db.close()
    }

    fun deleteGoodsInStock(goodId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            DatabaseHelper.TABLE_GOODS_SOLD,
            "${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD} = $goodId", null
        )
        db.delete(
            DatabaseHelper.TABLE_GOODS_IN_STOCK,
            "${DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID} = $goodId", null
        )
        db.close()
    }

    fun getGoodsTypeName(goodsTypeId: Int): String {
        val db = dbHelper.readableDatabase
        var goodType = GoodsType(1,"")
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT ${DatabaseHelper.COLUMN_GOODS_TYPE_NAME} FROM ${DatabaseHelper.TABLE_GOODS_TYPE} " +
                        "WHERE ${DatabaseHelper.COLUMN_GOODS_TYPE_ID} = ?", arrayOf(goodsTypeId.toString())
            )
            if (cursor != null && cursor.moveToFirst()) {
                goodType.name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_TYPE_NAME))
            }
        } finally {
            cursor?.close()
            db.close()
        }
        return goodType.name
    }

    fun getOrCreateSaleId(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${DatabaseHelper.COLUMN_SALE_INFORMATION_ID} FROM ${DatabaseHelper.TABLE_SALE_INFORMATION} WHERE ${DatabaseHelper.COLUMN_SALE_INFORMATION_PURCHASE_DATE} = '' LIMIT 1",
            null
        )
        return if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SALE_INFORMATION_ID))
        } else {
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_SALE_INFORMATION_PURCHASE_DATE, "")
                put(DatabaseHelper.COLUMN_SALE_INFORMATION_TOTAL_PRICE, 0)
            }
            val newSaleId = db.insert(DatabaseHelper.TABLE_SALE_INFORMATION, null, contentValues)
            newSaleId.toInt()
        }.also {
            cursor.close()
        }
    }

    fun addGoodsToCart(goodId: Int, saleId: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val cursor = db.rawQuery(
                "SELECT ${DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT} FROM ${DatabaseHelper.TABLE_GOODS_IN_STOCK} WHERE ${DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID} = ?",
                arrayOf(goodId.toString())
            )
            var stockCount = 0
            if (cursor.moveToFirst()) {
                stockCount = cursor.getInt(0)
            }
            cursor.close()
            val goodsInCartCount = getGoodsCountInCart(goodId, saleId)
            if (goodsInCartCount >= stockCount) {
                return false
            }
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_GOODS_SOLD_GOOD, goodId)
                put(DatabaseHelper.COLUMN_GOODS_SOLD_SALE, saleId)
            }
            db.insert(DatabaseHelper.TABLE_GOODS_SOLD, null, contentValues)
            db.setTransactionSuccessful()
            return true
        } finally {
            db.endTransaction()
        }
    }

    fun removeGoodsFromCart(goodId: Int, saleId: Int) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val cursor = db.rawQuery(
                "SELECT ${DatabaseHelper.COLUMN_GOODS_SOLD_ID} FROM ${DatabaseHelper.TABLE_GOODS_SOLD} WHERE ${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD} = ? AND ${DatabaseHelper.COLUMN_GOODS_SOLD_SALE} = ? LIMIT 1",
                arrayOf(goodId.toString(), saleId.toString())
            )
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(0)
                db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_GOODS_SOLD} WHERE ${DatabaseHelper.COLUMN_GOODS_SOLD_ID} = ?", arrayOf(id.toString()))
            }
            cursor.close()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getGoodsCountInCart(goodId: Int, saleId: Int): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseHelper.TABLE_GOODS_SOLD} WHERE ${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD} = ? AND ${DatabaseHelper.COLUMN_GOODS_SOLD_SALE} = ?", arrayOf(goodId.toString(), saleId.toString()))
        return if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }
    }

    fun completeSale(saleId: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val cursor = db.rawQuery("SELECT ${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD}, COUNT(${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD}) AS count FROM ${DatabaseHelper.TABLE_GOODS_SOLD} WHERE ${DatabaseHelper.COLUMN_GOODS_SOLD_SALE} = ? GROUP BY ${DatabaseHelper.COLUMN_GOODS_SOLD_GOOD}", arrayOf(saleId.toString()))
            while (cursor.moveToNext()) {
                val goodId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_SOLD_GOOD))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))

                val stockCursor = db.rawQuery("SELECT ${DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT} FROM ${DatabaseHelper.TABLE_GOODS_IN_STOCK} WHERE ${DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID} = ?", arrayOf(goodId.toString()))
                if (stockCursor.moveToFirst()) {
                    val stockCount = stockCursor.getInt(stockCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT))
                    val newStockCount = stockCount - count
                    val contentValues = ContentValues().apply {
                        put(DatabaseHelper.COLUMN_GOODS_IN_STOCK_COUNT, newStockCount)
                    }
                    db.update(DatabaseHelper.TABLE_GOODS_IN_STOCK, contentValues, "${DatabaseHelper.COLUMN_GOODS_IN_STOCK_ID} = ?", arrayOf(goodId.toString()))
                }
                stockCursor.close()
            }
            cursor.close()
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_SALE_INFORMATION_PURCHASE_DATE, getCurrentDate())
            }
            db.update(DatabaseHelper.TABLE_SALE_INFORMATION, contentValues, "${DatabaseHelper.COLUMN_SALE_INFORMATION_ID} = ?", arrayOf(saleId.toString()))
            val newSaleContentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_SALE_INFORMATION_PURCHASE_DATE, "")
                put(DatabaseHelper.COLUMN_SALE_INFORMATION_TOTAL_PRICE, 0)
            }
            db.insert(DatabaseHelper.TABLE_SALE_INFORMATION, null, newSaleContentValues)
            db.setTransactionSuccessful()
            return true
        } finally {
            db.endTransaction()
        }
    }
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}