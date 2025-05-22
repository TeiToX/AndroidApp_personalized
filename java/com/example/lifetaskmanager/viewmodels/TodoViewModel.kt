package com.example.lifetaskmanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.lifetaskmanager.data.AppDatabase
import com.example.lifetaskmanager.data.Todo
import com.example.lifetaskmanager.notifications.NotificationService
import kotlinx.coroutines.launch
import java.util.Date

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val todoDao = database.todoDao()
    private val notificationService = NotificationService(application)

    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    fun insert(title: String, dueDate: Date?, reminderTime: Date?) {
        val todo = Todo(
            title = title,
            dueDate = dueDate,
            reminderTime = reminderTime,
            isCompleted = false
        )
        viewModelScope.launch {
            val id = todoDao.insert(todo)
            reminderTime?.let {
                notificationService.scheduleNotification(
                    id,
                    title,
                    it.time - System.currentTimeMillis()
                )
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.update(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.delete(todo)
        }
    }

    fun toggleTodoCompleted(todo: Todo) {
        viewModelScope.launch {
            todoDao.update(todo.copy(isCompleted = !todo.isCompleted))
        }
    }
} 