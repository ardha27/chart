package com.example.sqlite.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var addbaby : Button
    private lateinit var growthgraph : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addbaby = findViewById(R.id.addbaby)
        growthgraph = findViewById(R.id.growthgraph)

        addbaby.setOnClickListener(this)
        growthgraph.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.addbaby -> {
                val intent = Intent(this@MainActivity, BabyList::class.java)
                startActivity(intent)
            }
            R.id.growthgraph -> {
                Toast.makeText(this, "Growth Graph", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, GrowthGraph::class.java)
                startActivity(intent)
            }
        }
    }
}