package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        private const val TABLE_NAME = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblStudent = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT" + ")")
        db?.execSQL(createTblStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(student: StudentModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, student.name)
        values.put(EMAIL, student.email)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return _success
    }

    @SuppressLint("Range")
    fun getAllStudent(): ArrayList<StudentModel>{
        val studentList: ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val student = StudentModel()
                student.id = cursor.getInt(cursor.getColumnIndex(ID))
                student.name = cursor.getString(cursor.getColumnIndex(NAME))
                student.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: StudentModel): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID, student.id)
        values.put(NAME, student.name)
        values.put(EMAIL, student.email)
        return db.update(TABLE_NAME, values, "$ID=?", arrayOf(student.id.toString()))
    }

    fun deleteStudent(id: Int): Int{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(ID, id)

        val _success = db.delete(TABLE_NAME, "id="+id, null)
        db.close()
        return _success
    }

}