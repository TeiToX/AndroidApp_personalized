package com.example.lifetaskmanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY dueDate ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Insert
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todos WHERE dueDate >= :startDate AND dueDate < :endDate")
    fun getTodosByDateRange(startDate: Long, endDate: Long): LiveData<List<Todo>>
} 