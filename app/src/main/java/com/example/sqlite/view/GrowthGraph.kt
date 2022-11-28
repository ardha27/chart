package com.example.sqlite.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sqlite.R
import com.example.sqlite.model.BabyModel
import com.example.sqlite.utils.SQLiteHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GrowthGraph : AppCompatActivity() {

    private lateinit var ourLineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.growth_graph)
        ourLineChart = findViewById(R.id.ourLineChart)

        try {
            retrieveRecordsAndPopulateCharts()
        }catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    private fun populateLineChart(values: Array<Int>) {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
            i++
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
        val data = LineData(lineDataSet)
        ourLineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = ourLineChart.xAxis
        xAxis.setDrawGridLines(true)

        ourLineChart.legend.isEnabled = false
        ourLineChart.axisRight.isEnabled = false

        ourLineChart.xAxis.apply {
            isGranularityEnabled = true
            granularity = 1f
            position = XAxis.XAxisPosition.BOTTOM
        }

        lineDataSet.apply {
            setDrawFilled(true)
            lineWidth = 2f
            circleRadius = 4f
            color = ContextCompat.getColor(this@GrowthGraph, R.color.black_75)
            setCircleColor(ContextCompat.getColor(this@GrowthGraph, R.color.black))
            setDrawCircleHole(false)
            valueTextSize = 8f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            fillDrawable = ContextCompat.getDrawable(this@GrowthGraph, R.drawable.bg_spark_line)
        }

        //remove description label
        ourLineChart.description.isEnabled = false

        //add animation
        ourLineChart.animateX(1000, Easing.EaseInSine)
        ourLineChart.data = data
        //refresh
        ourLineChart.invalidate()
    }

    fun retrieveRecordsAndPopulateCharts() {
        //creating the instance of DatabaseHandler class
        val sqliteHelper: SQLiteHelper = SQLiteHelper(this)
        //calling the retreiveAnimals method of DatabaseHandler class to read the records
        val baby: List<BabyModel> = sqliteHelper.getAllBaby()
        //create arrays for storing the values gotten
        val babyIDArray = Array<Int>(baby.size) { 0 }
        val babyNameArray = Array<String>(baby.size) { "" }
        val babyEmailArray = Array<String>(baby.size) { "" }

        //add the records till done
        var index = 0
        for (a in baby) {
            babyIDArray[index] = a.id
            babyNameArray[index] = a.NIK
            babyEmailArray[index] = a.nama
            index++
        }
        //call the methods for populating the charts
        populateLineChart(babyIDArray)

    }

}