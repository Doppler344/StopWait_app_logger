package com.example.diploma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONTokener
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null

    private val URL: String = "https://6401d995ab6b7399d0ae14fc.mockapi.io/admin_api/v1/log"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener {
            getStudents()
            checkApiUpdates()
        }
        btnUpdate.setOnClickListener { updateStudent() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edEmail.setText(it.email)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun getStudents() {
        val stdList = sqliteHelper.getAllStudent()
        Log.i("Log_got", "${stdList.size}")



        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_LONG).show()
        } else {
            val std = StudentModel(name = name, email = email)
            val status = sqliteHelper.insertStudent(std)

            if (status > -1) {
                Toast.makeText(this, "Log Added...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
                getStudents()
            }


        }
    }

    private fun updateStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        //Check record not change
        if (name == std?.name && email == std?.email) {
            Toast.makeText(this, "Record not changed...", Toast.LENGTH_SHORT).show()
            return
        }
        if (std == null) {
            return
        }
        val std = StudentModel(id = std!!.id, name = name, email = email)
        val status = sqliteHelper.updateStudent(std)
        if (status > -1) {
            clearEditText()
            getStudents()
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteStudent(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqliteHelper.deleteStudentById(id = id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun checkApiUpdates() {
        val httpClient = OkHttpClient()
        val request = Request.Builder().url(URL).build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    return
                } else {
                    val body = response.body?.string()
                    if (body != null) {
                        val jsonArray = JSONTokener(body).nextValue() as JSONArray
                        for (i in 0 until jsonArray.length()) {
                            val log = jsonArray.getJSONObject(i).getString("log")
                            val id = jsonArray.getJSONObject(i).getString("id")

                            val std = StudentModel(name = log, id = id.toInt())
                            try {

                                sqliteHelper.insertStudent(std)
                            } finally {

                            }

                            // Notify the adapter
                        }

                    }
                }
            }
        })
    }
}