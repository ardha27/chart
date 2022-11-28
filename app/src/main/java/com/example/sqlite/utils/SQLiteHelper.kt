package com.example.sqlite.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite.model.BabyModel

class SQLiteHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "baby.db"
        private const val TABLE_NAME = "tbl_baby"
        private const val ID = "id"
        private const val NAME = "NIK"
        private const val EMAIL = "nama"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblBaby = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT" + ")")
        db?.execSQL(createTblBaby)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertBaby(baby: BabyModel): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, baby.NIK)
        values.put(EMAIL, baby.nama)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return _success
    }

    @SuppressLint("Range")
    fun getAllBaby(): ArrayList<BabyModel>{
        val babyList: ArrayList<BabyModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val baby = BabyModel()
                baby.id = cursor.getInt(cursor.getColumnIndex(ID))
                baby.NIK = cursor.getString(cursor.getColumnIndex(NAME))
                baby.nama = cursor.getString(cursor.getColumnIndex(EMAIL))
                babyList.add(baby)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return babyList
    }

    fun updateBaby(baby: BabyModel): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID, baby.id)
        values.put(NAME, baby.NIK)
        values.put(EMAIL, baby.nama)
        return db.update(TABLE_NAME, values, "$ID=?", arrayOf(baby.id.toString()))
    }

    fun deleteBaby(id: Int): Int{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(ID, id)

        val _success = db.delete(TABLE_NAME, "id="+id, null)
        db.close()
        return _success
    }

}