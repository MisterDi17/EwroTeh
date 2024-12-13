package com.example.ewroteh.Model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object
    {
        private const val DATABASE_NAME = "shop.db"
        private const val DATABASE_VERSION = 2

        //Таблица типов товаров
        const val TABLE_GOODS_TYPE = "GoodsType"
        const val COLUMN_GOODS_TYPE_ID = "GoodsTypeId"
        const val COLUMN_GOODS_TYPE_NAME = "Name"

        //Таблица товаров
        const val TABLE_GOODS_IN_STOCK = "GoodsInStock"
        const val COLUMN_GOODS_IN_STOCK_ID = "GoodID"
        const val COLUMN_GOODS_IN_STOCK_NAME = "Name"
        const val COLUMN_GOODS_IN_STOCK_COUNT = "GoodsCount"
        const val COLUMN_GOODS_IN_STOCK_PRICE = "Price"
        const val COLUMN_GOODS_IN_STOCK_TYPE = "GoodsTypeId"

        //Таблица информации о продаже
        const val TABLE_SALE_INFORMATION = "SaleInformation"
        const val COLUMN_SALE_INFORMATION_ID = "SaleId"
        const val COLUMN_SALE_INFORMATION_PURCHASE_DATE = "PurchaseDate"
        const val COLUMN_SALE_INFORMATION_TOTAL_PRICE = "TotalPrice"

        //Таблица продаваемых товаров
        const val TABLE_GOODS_SOLD = "GoodsSold"
        const val COLUMN_GOODS_SOLD_ID = "Id"
        const val COLUMN_GOODS_SOLD_GOOD = "GoodId"
        const val COLUMN_GOODS_SOLD_SALE = "SaleId"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createTableGoodsType = ("CREATE TABLE IF NOT EXISTS $TABLE_GOODS_TYPE ( " +
                "$COLUMN_GOODS_TYPE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_GOODS_TYPE_NAME TEXT)")
        db.execSQL(createTableGoodsType)

        val createTableGoodsInStock = ("CREATE TABLE IF NOT EXISTS $TABLE_GOODS_IN_STOCK ( " +
                "$COLUMN_GOODS_IN_STOCK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_GOODS_IN_STOCK_NAME TEXT, " +
                "$COLUMN_GOODS_IN_STOCK_COUNT INTEGER, " +
                "$COLUMN_GOODS_IN_STOCK_PRICE REAL, " +
                "$COLUMN_GOODS_IN_STOCK_TYPE INTEGER, " +
                "FOREIGN KEY ($COLUMN_GOODS_IN_STOCK_TYPE) REFERENCES $TABLE_GOODS_TYPE ($COLUMN_GOODS_TYPE_ID))")
        db.execSQL(createTableGoodsInStock)

        val createTableSaleInformation = ("CREATE TABLE IF NOT EXISTS $TABLE_SALE_INFORMATION ( " +
                "$COLUMN_SALE_INFORMATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_SALE_INFORMATION_PURCHASE_DATE TEXT, " +
                "$COLUMN_SALE_INFORMATION_TOTAL_PRICE REAL)")
        db.execSQL(createTableSaleInformation)

        val createTableGoodsSold = ("CREATE TABLE IF NOT EXISTS $TABLE_GOODS_SOLD ( " +
                "$COLUMN_GOODS_SOLD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_GOODS_SOLD_GOOD INTEGER, " +
                "$COLUMN_GOODS_SOLD_SALE INTEGER, " +
                "FOREIGN KEY ($COLUMN_GOODS_SOLD_GOOD) REFERENCES $TABLE_GOODS_IN_STOCK ($COLUMN_GOODS_IN_STOCK_ID), " +
                "FOREIGN KEY ($COLUMN_GOODS_SOLD_SALE) REFERENCES $TABLE_SALE_INFORMATION ($COLUMN_SALE_INFORMATION_ID))")
        db.execSQL(createTableGoodsSold)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOODS_SOLD")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SALE_INFORMATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOODS_IN_STOCK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOODS_TYPE")
        onCreate(db)
    }
}