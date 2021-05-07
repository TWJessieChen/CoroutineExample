package com.jessie.chen.coroutineexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    private val repository by lazy {
        TasksRepository()
    }

    private val viewModel by lazy {
        TaskViewModel(repository)
    }

//    private lateinit var repository: TasksRepository
//
//    private lateinit var viewModel : TaskViewModel

    private val scope = MainScope()

    private lateinit var btnAll: Button
    private lateinit var btnActivated: Button
    private lateinit var btnCompleted: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAll = findViewById(R.id.btnAll)
        btnActivated = findViewById(R.id.btnActivated)
        btnCompleted = findViewById(R.id.btnCompleted)
//        repository = TasksRepository()
//        viewModel = TaskViewModel(repository)

        btnAll.setOnClickListener {
            Log.d(TAG, "Thread All: " + Thread.currentThread().name)
            getTasksList(TaskType.All)
        }
        btnActivated.setOnClickListener {
            Log.d(TAG, "Thread Activated: " + Thread.currentThread().name)
            getTasksList(TaskType.Activated)
        }
        btnCompleted.setOnClickListener {
            Log.d(TAG, "Thread Completed: " + Thread.currentThread().name)
            getTasksList(TaskType.Completed)
        }

    }

    private fun getTasksList(type: TaskType) {
        scope.launch {
            val tasks = viewModel.getTasks(type)
            Toast.makeText(
                this@MainActivity,
                tasks.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

}