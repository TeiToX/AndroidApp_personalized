package com.example.lifetaskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val dueDate: Date?,
    val isCompleted: Boolean = false,
    val reminderTime: Date?
) 