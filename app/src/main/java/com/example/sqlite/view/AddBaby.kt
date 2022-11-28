package com.example.sqlite.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.R
import com.example.sqlite.model.BabyModel
import com.example.sqlite.utils.SQLiteHelper

class AddBaby : AppCompatActivity() {

    private lateinit var edNIK : EditText
    private lateinit var edNama : EditText
    private lateinit var dbHelper : SQLiteHelper
    private lateinit var btnAdd : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_baby)

        dbHelper = SQLiteHelper(this)
        btnAdd = findViewById(R.id.btnAdd)
        edNIK = findViewById(R.id.edNIK)
        edNama = findViewById(R.id.edNama)

        btnAdd.setOnClickListener {
            addBaby()
            clearEditText()
            intent = Intent(this@AddBaby, BabyList::class.java)
            startActivity(intent)
        }

    }

    private fun addBaby(){
        val NIK = edNIK.text.toString()
        val nama = edNama.text.toString()

        if (NIK.isEmpty()||nama.isEmpty()){
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT).show()
        } else {
            val baby = BabyModel(NIK = NIK, nama = nama)
            val id = dbHelper.insertBaby(baby)
            if (id > 0){
                Toast.makeText(this, "Baby added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add baby", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText(){
        edNIK.text.clear()
        edNama.text.clear()
        edNama.requestFocus()

    }

}