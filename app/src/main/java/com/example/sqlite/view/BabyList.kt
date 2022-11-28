package com.example.sqlite.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.R
import com.example.sqlite.adapter.BabyAdapter
import com.example.sqlite.utils.SQLiteHelper

class BabyList : AppCompatActivity() {


    private lateinit var btnAdd : Button

    private lateinit var dbHelper : SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter : BabyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.baby_list)

        initView()
        initRecyclerView()

        dbHelper = SQLiteHelper(this)

        val babyList = dbHelper.getAllBaby()
        adapter?.addItems(babyList)

        adapter?.setOnClickDelete { baby ->
            deleteBaby(baby.id)
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this@BabyList, AddBaby::class.java)
            startActivity(intent)
        }

        adapter?.setOnClickItem { baby ->
            val intent = Intent(this@BabyList, ScaleBaby::class.java)
            intent.putExtra("NIK", baby.NIK)
            intent.putExtra("Nama", baby.nama)
            startActivity(intent)

        }

    }

    private fun deleteBaby(id: Int){
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete Baby")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            dbHelper.deleteBaby(id)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BabyAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        btnAdd = findViewById(R.id.btnAdd)
        recyclerView = findViewById(R.id.recyclerView)
    }



}