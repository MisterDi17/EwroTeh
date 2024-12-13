package com.example.ewroteh.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ewroteh.Entity.GoodsInStock
import com.example.ewroteh.Model.DatabaseHelper
import com.example.ewroteh.Model.dbRepository
import com.example.ewroteh.R


class EditGoodsInStockFragment(var good: GoodsInStock?) : Fragment() {
    private lateinit var repository: dbRepository
    lateinit var name: EditText
    lateinit var goodsCount: EditText
    lateinit var price: EditText
    lateinit var goodsTypeId: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_goods_in_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = dbRepository(DatabaseHelper(view.context))
        name = view.findViewById(R.id.etGoodInStockName)
        goodsCount = view.findViewById(R.id.etGoodCount)
        price = view.findViewById(R.id.etGoodPrice)
        goodsTypeId = view.findViewById(R.id.etGoodTypeName)
        if (good != null)
            initializeFragment()
        else
            good = GoodsInStock(0, "", 0, 0.0, 0)
        val btSave = view.findViewById<Button>(R.id.btSaveGoodInStock)
        val btSelectAndSave = view.findViewById<Button>(R.id.btSelectGoodTypeAndSave)
        if (good!!.goodId == 0)
            btSave.isEnabled = false
        else
            btSave.isEnabled = true
        btSelectAndSave.setOnClickListener {
            if (emptyCheck()) {
                saveData()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewMain, SelectGoodTypeFragment(good!!))
                    .addToBackStack("da2")
                    .commit()
            }
        }
        btSave.setOnClickListener {
            if (emptyCheck()) {
                saveData()
                repository.editGoodsInStock(good!!)
                parentFragmentManager.popBackStack()
            }
        }
    }
    private fun emptyCheck(): Boolean{
        if (!name.text.isEmpty() && !goodsCount.text.isEmpty() && !price.text.isEmpty())
            return true
        else {
            Toast.makeText(context, "Введите остальные данные", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    private fun saveData(){
        good!!.name = name.text.toString()
        good!!.goodsCount = goodsCount.text.toString().toInt()
        good!!.price = price.text.toString().toDouble()
    }
    private fun initializeFragment(){
        name.setText(good!!.name)
        goodsCount.setText(good!!.goodsCount.toString())
        price.setText(good!!.price.toString())
        val goodsTypeName = repository.getGoodsTypeName(good!!.goodsTypeId)
        goodsTypeId.setText(goodsTypeName)
//        goodsTypeId.setText(repository.getGoodsTypeName(good!!.goodsTypeId))
    }
}