package com.example.lifetaskmanager.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifetaskmanager.adapters.RecipeAdapter
import com.example.lifetaskmanager.databinding.FragmentRecipesBinding
import com.example.lifetaskmanager.viewmodels.RecipeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        setupSearch()
        observeRecipes()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(
            onViewClick = { recipe ->
                showRecipeDetailsDialog(recipe)
            },
            onDeleteClick = { recipe ->
                viewModel.deleteRecipe(recipe)
            }
        )

        binding.recipesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@RecipesFragment.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddRecipe.setOnClickListener {
            showAddRecipeDialog()
        }
    }

    private fun setupSearch() {
        binding.searchRecipes.addTextChangedListener { text ->
            val query = text?.toString() ?: ""
            viewModel.searchRecipes(query).observe(viewLifecycleOwner) { recipes ->
                adapter.submitList(recipes)
            }
        }
    }

    private fun observeRecipes() {
        viewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            if (binding.searchRecipes.text.isNullOrEmpty()) {
                adapter.submitList(recipes)
            }
        }
    }

    private fun showAddRecipeDialog() {
        val context = requireContext()
        val nameInput = createInput(context, "Recipe name")
        val ingredientsInput = createInput(context, "Ingredients")
        val instructionsInput = createInput(context, "Instructions")

        val dialogView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(nameInput)
            addView(ingredientsInput)
            addView(instructionsInput)
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Add Recipe")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.editText?.text?.toString()
                val ingredients = ingredientsInput.editText?.text?.toString()
                val instructions = instructionsInput.editText?.text?.toString()

                if (!name.isNullOrBlank() && !ingredients.isNullOrBlank() && !instructions.isNullOrBlank()) {
                    viewModel.insert(name, ingredients, instructions)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRecipeDetailsDialog(recipe: Recipe) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(recipe.name)
            .setMessage("""
                Ingredients:
                ${recipe.ingredients}
                
                Instructions:
                ${recipe.instructions}
            """.trimIndent())
            .setPositiveButton("Close", null)
            .show()
    }

    private fun createInput(context: Context, hint: String): TextInputLayout {
        return TextInputLayout(context).apply {
            this.hint = hint
            addView(TextInputEditText(context))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 