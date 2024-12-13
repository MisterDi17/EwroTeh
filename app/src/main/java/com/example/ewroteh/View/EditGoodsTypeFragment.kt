package com.example.ewroteh.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ewroteh.Entity.GoodsType
import com.example.ewroteh.Model.DatabaseHelper
import com.example.ewroteh.Model.dbRepository
import com.example.ewroteh.R

class EditGoodsTypeFragment(var goodType: GoodsType?) : Fragment() {
    private lateinit var repository: dbRepository
    lateinit var name: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_goods_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.etGoodsTypeName)
        if (goodType != null)
            initializeFragment()
        else
            goodType = GoodsType(0, "")
        val btSave = view.findViewById<Button>(R.id.btSaveGoodsType)
        repository = dbRepository(DatabaseHelper(view.context))
        btSave.setOnClickListener {
            saveData()
            try {
                if(goodType!!.goodsTypeId != 0)
                    repository.editGoodsType(goodType!!)
                else
                    repository.insertGoodsType(goodType!!.name)
                parentFragmentManager.popBackStack()
            }
            catch (e: Exception){
                Log.d("uwu", "Error ${e.message}")
            }
        }
    }
    private fun saveData(){
        goodType!!.name = name.text.toString()
    }
    private fun initializeFragment(){
        name.setText(goodType!!.name)
    }
}