package com.example.sqlite

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()

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
}