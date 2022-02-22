package com.example.todoapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.fasterxml.jackson.module.kotlin.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private val mapper = jacksonObjectMapper()
    @RequiresApi(Build.VERSION_CODES.O)
    val JSON_PATHNAME = Environment.getExternalStorageDirectory().toPath().toString()+"/todosJson.json"


    private var todosFromJson = ""//gettingTodos()

    private lateinit var listOfTodos: MutableList<ToDo>//mapper.readValue(todosFromJson)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            todosFromJson = gettingTodos()
        }catch (e:Exception){
            todosFromJson = "[]"
        }

        listOfTodos = mapper.readValue(todosFromJson)

        toDoAdapter = ToDoAdapter(listOfTodos)

        binding.rvTodoList.adapter = toDoAdapter
        binding.rvTodoList.layoutManager = LinearLayoutManager(this)

        binding.btnAddNewActivity.setOnClickListener {
            val text = binding.etNewActivity.text.toString()
            if (text.isNotEmpty()) {
                val todo = ToDo(title = text)

                toDoAdapter.addTodo(todo)

            } else {
                Toast.makeText(this, "U must write something!", Toast.LENGTH_SHORT).show()

            }
            binding.etNewActivity.text.clear()
        }


        /*binding.btnDeleteActivity.setOnClickListener {
            toDoAdapter.deleteDone()
        }*/

        binding.btnDeleteActivity.setOnClickListener{
                //Toast.makeText(this,"Guardando archivo", Toast.LENGTH_LONG).show()
                toDoAdapter.deleteDone()

        }
    }

    override fun onStop() {
        try {
            when(checkWritingPermision()){
                true -> createJson()
            }
        } catch (e: Exception) {
            Log.e(String?.toString(), e.toString())
        }
        super.onStop()
    }

    private fun gettingTodos():String{
        var todos = ""
        val fr = FileReader(JSON_PATHNAME)

        todos = fr.readText()
        return todos
    }

    private fun createJson(){
        //Crear el archivo json usando FileWriter
        val jsonString = mapper.writeValueAsString(toDoAdapter.getTodos()).toString()



        val fileWriter = FileWriter(JSON_PATHNAME)

        fileWriter.write(jsonString)

        //Toast.makeText(this,"Datos guardados satisfactoriamente en:"+JSON_PATHNAME,Toast.LENGTH_LONG).show()
        fileWriter.close()
    }

    private fun checkWritingPermision():Boolean{
        //comprobar si el ususario aceptó los permisos o no
        when{
            ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                //El permiso ya estaba aceptado
                return true //si ya está aceptado
            }
            else -> {
                //pedir el permiso.
                requestWritingPermision()
                return false
            }
        }

    }
    private fun requestWritingPermision(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //El usuario ya habia rechazado los permisos, toca enviar a configuración para activación manual
            Toast.makeText(this,"EL usuario ya ha rechazado los permisos", Toast.LENGTH_LONG).show()
        }else{
            //pedir permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                23
            )


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        if(requestCode==23){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createJson()

            }else{
                Toast.makeText(this,"Json has been not created", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}


