package com.example.lifetaskmanager.ui

import android.app.DateTimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifetaskmanager.adapters.TodoAdapter
import com.example.lifetaskmanager.databinding.FragmentTodosBinding
import com.example.lifetaskmanager.viewmodels.TodoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

class TodosFragment : Fragment() {
    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodoViewModel by viewModels()
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        observeTodos()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            onCheckChanged = { todo ->
                viewModel.toggleTodoCompleted(todo)
            },
            onDeleteClick = { todo ->
                viewModel.deleteTodo(todo)
            }
        )

        binding.todosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TodosFragment.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddTodo.setOnClickListener {
            showAddTodoDialog()
        }
    }

    private fun observeTodos() {
        viewModel.allTodos.observe(viewLifecycleOwner) { todos ->
            adapter.submitList(todos)
        }
    }

    private fun showAddTodoDialog() {
        val context = requireContext()
        val input = TextInputEditText(context).apply {
            hint = "Enter task"
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Add Todo")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val title = input.text?.toString()
                if (!title.isNullOrBlank()) {
                    showDateTimePicker { dueDate ->
                        viewModel.insert(title, dueDate, dueDate)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDateTimePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()

        DateTimePickerDialog(requireContext())
            .show { selectedDate ->
                onDateSelected(selectedDate)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 