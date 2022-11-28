package com.example.sqlite.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.R

class ScaleBaby : AppCompatActivity() {

    private lateinit var tvNIK : TextView
    private lateinit var tvNama : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scale_baby)

        tvNIK = findViewById(R.id.tvNIK)
        tvNama = findViewById(R.id.tvNama)

        val NIK = intent.getStringExtra("NIK")
        val Nama = intent.getStringExtra("Nama")

        tvNIK.setText(NIK)
        tvNama.setText(Nama)

    }
}