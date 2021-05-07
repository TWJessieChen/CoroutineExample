package com.jessie.chen.coroutineexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskViewModel (private val repository: TasksRepository) {
    suspend fun getTasks(type: TaskType = TaskType.All): List<String> {
        return withContext(Dispatchers.IO) {
            when (type) {
                TaskType.Activated -> repository.getActivatedTasksFromRoom()
                TaskType.Completed -> repository.getCompletedTasksFromRoom()
                else -> repository.getTasksFromRoom()
            }
        }
    }
}