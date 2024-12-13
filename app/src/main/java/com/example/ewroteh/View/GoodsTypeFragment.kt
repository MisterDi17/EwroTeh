package com.example.ewroteh.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ewroteh.Adapter.GoodsTypeAdapter
import com.example.ewroteh.Entity.GoodsType
import com.example.ewroteh.HomeActivity
import com.example.ewroteh.MainActivity
import com.example.ewroteh.Model.DatabaseHelper
import com.example.ewroteh.Model.dbRepository
import com.example.ewroteh.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GoodsTypeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: dbRepository
    private lateinit var goodsTypeAdapter: GoodsTypeAdapter
    private var goodsTypeList: MutableList<GoodsType> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goods_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = dbRepository(DatabaseHelper(view.context))
        recyclerView = view.findViewById(R.id.goodsTypeRecyclerView)
        goodsTypeList = repository.getAllGoodsType()
        goodsTypeAdapter = GoodsTypeAdapter(goodsTypeList, { position ->
            repository.deleteGoodsType(goodsTypeList[position].goodsTypeId, requireContext())
        }, { goodsType, position ->
            (activity as? HomeActivity)?.openEditGoodsTypeFragment(goodsType)
        })

        recyclerView.adapter = goodsTypeAdapter

        val btAddGoodsType = view.findViewById<FloatingActionButton>(R.id.fabAddGoodsType)
        btAddGoodsType.setOnClickListener {
            (activity as? HomeActivity)?.openEditGoodsTypeFragment(null)
        }
    }
}