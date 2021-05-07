package com.jessie.chen.coroutineexample

import android.util.Log
import kotlinx.coroutines.*

class TasksRepository {
    private val TAG = this.javaClass.simpleName

    var controllCoroutines = ControlledCoroutinesExample<List<String>>()

    suspend fun getTasksFromRoom(): List<String> {
        return controllCoroutines.cancelPreviousThenRun {
            fakeGetTasks()
        }
    }

    suspend fun getActivatedTasksFromRoom(): List<String> {
        return controllCoroutines.cancelPreviousThenRun {
            fakeGetActivatedTasks()
        }
    }

    suspend fun getCompletedTasksFromRoom(): List<String> {
        return controllCoroutines.cancelPreviousThenRun {
            fakeGetCompletedTasks()
        }
    }

    private suspend fun fakeGetTasks(): List<String> {
        Log.d(TAG, "Thread All: " + Thread.currentThread().name)
        delay(1500L) // 模擬讀取資料所消耗的 IO 時間
        return mutableListOf("Task1", "Tasks2", "Tasks3")
    }

    private suspend fun fakeGetActivatedTasks(): List<String> {
        Log.d(TAG, "Thread Activated: " + Thread.currentThread().name)
        delay(1000L) // 模擬讀取資料所消耗的 IO 時間
        return mutableListOf("Task1")
    }

    private suspend fun fakeGetCompletedTasks(): List<String> {
        Log.d(TAG, "Thread Completed: " + Thread.currentThread().name)
        delay(500L) // 模擬讀取資料所消耗的 IO 時間
        return mutableListOf("Tasks2", "Tasks3")
    }
    
}

class ControlledCoroutinesExample<T> {
    private var cachedTasks: Deferred<T>? = null

    suspend fun cancelPreviousThenRun(block: suspend () -> T): T {
        // 如果當前有正在執行的 cachedTasks，可以直接取消並改成執行最新的請求
        cachedTasks?.cancelAndJoin()

        return coroutineScope {
            // 建立一個 async 並且 suspend
            val newTask = async {
                block()
            }

            // newTask 執行完畢時清除舊的 cachedTasks 任務
            newTask.invokeOnCompletion {
                cachedTasks = null
            }

            // newTask 完成後交給 cachedTasks
            cachedTasks = newTask
            // newTask 恢復狀態並開始執行
            newTask.await()
        }
    }
}