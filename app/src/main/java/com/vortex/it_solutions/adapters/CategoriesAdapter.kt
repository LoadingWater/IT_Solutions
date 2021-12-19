package com.vortex.it_solutions.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.Gson
import com.vortex.it_solutions.R
import com.vortex.it_solutions.api.FakeStoreApi
import com.vortex.it_solutions.enums.Categories
import com.vortex.it_solutions.fragments.CategoriesFragment
import com.vortex.it_solutions.models.Category
import com.vortex.it_solutions.view_models.SharedViewModel
import java.util.*

class CategoriesAdapter(val categories: List<Category>, val fragment: CategoriesFragment): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>()
{
	val TAG = CategoriesAdapter::class.java.name
	val sharedViewModel = ViewModelProvider(fragment.requireActivity()).get(SharedViewModel::class.java)
	val categoriesVM = fragment.categoriesViewModel

	// Loading resources here so we don't need to load them in onBindViewHolder() every time
	val resultsString = fragment.resources.getString(R.string.results_in_category)
	val electronicsDrawable = ResourcesCompat.getDrawable(fragment.resources, R.drawable.electronics, null)
	val jeweleryDrawable = ResourcesCompat.getDrawable(fragment.resources, R.drawable.jewelry, null)
	val mensClothingDrawable = ResourcesCompat.getDrawable(fragment.resources, R.drawable.men_clothes, null)
	val womensClothingDrawable = ResourcesCompat.getDrawable(fragment.resources, R.drawable.women_clothes, null)

	inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
	{
		var categoryRaw = ""
		val categoryImage = view.findViewById<ImageView>(R.id.categoryImage_ci)
		val categoryName = view.findViewById<TextView>(R.id.categoryTitle_ci)
		val categoryResults = view.findViewById<TextView>(R.id.productsResults_ci)
		val root = categoryName.parent as ConstraintLayout
		val errorToast: Toast = Toast.makeText(categoryImage.context, categoryImage.resources.getString(R.string.on_results_load_fail), Toast.LENGTH_LONG)

		init
		{
			root.setOnClickListener {
				sharedViewModel.selectedCategory.value = categoryRaw
				val nav = Navigation.createNavigateOnClickListener(R.id.productsScreenFragment)
				nav.onClick(root)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val category = categories[position]
		val categoryName = category.category
		holder.categoryRaw = categoryName
		when (categoryName)
		{
			Categories.electronics.value -> holder.categoryImage.setImageDrawable(electronicsDrawable)
			Categories.jewelery.value -> holder.categoryImage.setImageDrawable(jeweleryDrawable)
			Categories.mensClothing.value -> holder.categoryImage.setImageDrawable(mensClothingDrawable)
			Categories.womensClothing.value -> holder.categoryImage.setImageDrawable(womensClothingDrawable)
		}
		holder.categoryName.text = categoryName.replaceFirstChar {
			if (it.isLowerCase()) it.titlecase(
				Locale.getDefault()
			) else it.toString()
		}
		// Make api request if first time loading
		if (categoriesVM.getCategories()[position].results.isEmpty())
		{
			getCategoriesResults(categoryName, holder)
		}
		else
		{
			val results = categoriesVM.getCategories()[holder.adapterPosition].results
			holder.categoryResults.text = "$resultsString $results"
		}
	}

	override fun getItemCount(): Int
	{
		return categories.size
	}

	/**
	 * @param category value from Categories enumeration
	 */
	private fun getCategoriesResults(category: String, holder: ViewHolder)
	{
		val onSuccess = Response.Listener<String> {
			// Parsing response and updating values in a viewModel
			val results = Gson().fromJson(it, Array<Any>::class.java).size.toString()
			// Updating value without notifying observer, which is in CategoryController. Or
			// It will trigger recycler update multiple times
			categoriesVM.updateResults(holder.adapterPosition, results)
			holder.categoryResults.text = "$resultsString $results"
		}
		val onError = Response.ErrorListener {
			// Prevents toasts from queueing
			holder.errorToast.apply {
				cancel()
				show()
			}
			Log.e(TAG, it.message.toString())
			categoriesVM.updateResults(holder.adapterPosition, "N/A")
			holder.categoryResults.text = holder.categoryImage.resources.getString(R.string.on_results_load_fail)
		}
		FakeStoreApi.getProductsInCategory(category, holder.categoryImage.context, onSuccess, onError)
	}
}