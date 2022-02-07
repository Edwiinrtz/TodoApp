package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toDoAdapter = ToDoAdapter(mutableListOf())

        binding.rvTodoList.adapter = toDoAdapter
        binding.rvTodoList.layoutManager = LinearLayoutManager(this)

        binding.btnAddNewActivity.setOnClickListener {
            val text = binding.etNewActivity.text.toString()
            if(text.length > 1){
                val todo = ToDo(title = text)

                toDoAdapter.addTodo(todo)

            }
            binding.etNewActivity.text.clear()
        }


        binding.btnDeleteActivity.setOnClickListener{
            toDoAdapter.deleteDone()
        }
    }
}