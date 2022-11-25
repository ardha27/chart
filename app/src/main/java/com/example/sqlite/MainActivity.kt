package com.example.sqlite

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : AppCompatActivity() {

    private lateinit var edName : EditText
    private lateinit var edEmail : EditText
    private lateinit var btnAdd : Button
    private lateinit var btnView : Button
    private lateinit var btnUpdate : Button

    private lateinit var dbHelper : SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter : StudentAdapter? = null
    private var studentList : StudentModel? = null

    private lateinit var ourLineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ourLineChart = findViewById(R.id.ourLineChart)

        initView()
        initRecyclerView()
        try {
            retrieveRecordsAndPopulateCharts()
        }catch (e: Exception) {
            Log.e("Error", e.toString())
        }


        dbHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener { getStudent() }
        btnUpdate.setOnClickListener { updateStudent() }
        adapter?.setOnClickItem { student ->
            Toast.makeText(this, student.name, Toast.LENGTH_SHORT).show()

            edName.setText(student.name)
            edEmail.setText(student.email)
            studentList = student
        }

        adapter?.setOnClickDelete { student ->
            deleteStudent(student.id)
        }
    }

    private fun getStudent() {
        val studentList = dbHelper.getAllStudent()
        Log.e("Student List", studentList.size.toString())

        adapter?.addItems(studentList)
    }

    private fun addStudent(){
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if (name.isEmpty()||email.isEmpty()){
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT).show()
        } else {
            val student = StudentModel(name = name, email = email)
            val id = dbHelper.insertStudent(student)
            if (id > 0){
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudent()
                retrieveRecordsAndPopulateCharts()
            } else {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteStudent(id: Int){
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete Student")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            dbHelper.deleteStudent(id)
            getStudent()
            retrieveRecordsAndPopulateCharts()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun updateStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if (name == studentList?.name && email == studentList?.email){
            Toast.makeText(this, "Data not changed", Toast.LENGTH_SHORT).show()
            return
        }
        if (studentList == null) return
        val student = StudentModel(id = studentList!!.id, name = name, email = email)
        val status = dbHelper.updateStudent(student)
        if (status > 0){
            Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
            clearEditText()
            getStudent()
        } else {
            Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearEditText(){
        edName.text.clear()
        edEmail.text.clear()
        edName.requestFocus()

    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
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
            color = ContextCompat.getColor(this@MainActivity, R.color.black_75)
            setCircleColor(ContextCompat.getColor(this@MainActivity, R.color.black))
            setDrawCircleHole(false)
            valueTextSize = 8f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            fillDrawable = ContextCompat.getDrawable(this@MainActivity, R.drawable.bg_spark_line)
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
        val student: List<StudentModel> = sqliteHelper.getAllStudent()
        //create arrays for storing the values gotten
        val studentIDArray = Array<Int>(student.size) { 0 }
        val studentNameArray = Array<String>(student.size) { "" }
        val studentEmailArray = Array<String>(student.size) { "" }

        //add the records till done
        var index = 0
        for (a in student) {
            studentIDArray[index] = a.id
            studentNameArray[index] = a.name
            studentEmailArray[index] = a.email
            index++
        }
        //call the methods for populating the charts
        populateLineChart(studentIDArray)

    }

}