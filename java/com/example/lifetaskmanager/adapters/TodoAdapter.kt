package com.example.lifetaskmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lifetaskmanager.data.Todo
import com.example.lifetaskmanager.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TodoAdapter(
    private val onCheckChanged: (Todo) -> Unit,
    private val onDeleteClick: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(
        private val binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(todo: Todo) {
            binding.todoTitle.text = todo.title
            binding.todoCheckbox.isChecked = todo.isCompleted
            binding.todoDueDate.text = todo.dueDate?.let { "Due: ${dateFormat.format(it)}" } ?: ""

            binding.todoCheckbox.setOnClickListener {
                onCheckChanged(todo)
            }

            binding.todoDelete.setOnClickListener {
                onDeleteClick(todo)
            }
        }
    }

    private class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
} 