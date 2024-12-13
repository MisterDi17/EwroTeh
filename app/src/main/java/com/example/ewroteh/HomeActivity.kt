package com.example.ewroteh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ewroteh.Entity.GoodsInStock
import com.example.ewroteh.Entity.GoodsType
import com.example.ewroteh.View.CashFragment
import com.example.ewroteh.View.EditGoodsInStockFragment
import com.example.ewroteh.View.EditGoodsTypeFragment
import com.example.ewroteh.View.WarehouseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bnv = findViewById<BottomNavigationView>(R.id.bottomMenu)
        bnv.setOnItemSelectedListener {
            when (it.itemId){
                R.id.cashRegister -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMain, CashFragment())
                        .commit()
                    true
                }
                R.id.warehouse -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMain, WarehouseFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
    fun openEditGoodsTypeFragment(goodsType: GoodsType?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewMain, EditGoodsTypeFragment(goodsType))
            .addToBackStack(null)
            .commit()
    }
    fun openEditGoodsInStockFragment(good: GoodsInStock?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewMain, EditGoodsInStockFragment(good))
            .addToBackStack(null)
            .commit()
    }
}