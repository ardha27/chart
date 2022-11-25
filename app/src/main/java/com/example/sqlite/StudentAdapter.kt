package com.example.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var studentList: ArrayList<StudentModel> = ArrayList()
    private var onClickItem: ((StudentModel) -> Unit)? = null
    private var onClickDelete: ((StudentModel) -> Unit)? = null

    fun addItems(studentList: ArrayList<StudentModel>) {
        this.studentList = studentList
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (StudentModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDelete(callback: (StudentModel) -> Unit) {
        this.onClickDelete = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.card_items_student, parent, false)
    )
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bindView(student)
        holder.itemView.setOnClickListener {
            onClickItem?.invoke(student)
        }
        holder.btnDelete.setOnClickListener {
            onClickDelete?.invoke(student)
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class StudentViewHolder (var view: View) : RecyclerView.ViewHolder(view) {
        private var id: TextView = view.findViewById(R.id.tvId)
        private var tvName: TextView = view.findViewById(R.id.tvName)
        private var tvEmail: TextView = view.findViewById(R.id.tvEmail)
        var btnDelete: TextView = view.findViewById(R.id.btnDelete)

        fun bindView (student: StudentModel){
            id.text = student.id.toString()
            tvName.text = student.name
            tvEmail.text = student.email
        }
    }

}