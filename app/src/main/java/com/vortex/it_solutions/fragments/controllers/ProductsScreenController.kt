package com.vortex.it_solutions.fragments.controllers

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.Gson
import com.vortex.it_solutions.R
import com.vortex.it_solutions.adapters.ItemsAdapter
import com.vortex.it_solutions.api.FakeStoreApi
import com.vortex.it_solutions.fragments.ProductsScreenFragment
import com.vortex.it_solutions.models.Product

class ProductsScreenController(val fragment: ProductsScreenFragment): BaseController(fragment)
{
	val binding = fragment.binding!!

	override fun initFragmentViews()
	{
		// Observers
		fragment.itemsVM.items.observe(fragment.viewLifecycleOwner, Observer {
			// Will be triggered on the fragment enter
			if (it.isEmpty())
			{
				binding.productsRecyclerFps.adapter = ItemsAdapter(emptyList(), fragment)
			}
			binding.productsRecyclerFps.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
			binding.productsRecyclerFps.adapter = ItemsAdapter(it, fragment)
			binding.productsRecyclerFps.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.categories_layout_animation)
			binding.productsRecyclerFps.scheduleLayoutAnimation()
		})

		// Close keyboard and remove blinking searchView cursor
		binding.productsRecyclerFps.setOnTouchListener { v, event ->
			val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
			v.requestFocus()
			false
		}

		// Toolbar section
		activity.findViewById<android.widget.SearchView>(R.id.search_view_am).isVisible = true

		activity.findViewById<SearchView>(R.id.search_view_am).setOnQueryTextListener(object: SearchView.OnQueryTextListener{
			override fun onQueryTextSubmit(query: String?) = false
			override fun onQueryTextChange(newText: String?): Boolean
			{
				(binding.productsRecyclerFps.adapter as? ItemsAdapter)?.findItem(newText.toString())
				return false
			}
		})

		getItemsFromCategory(sharedViewModel.selectedCategory.value!!)
	}

	private fun getItemsFromCategory(category: String)
	{
		val onSuccess = Response.Listener<String> {
			val items = Gson().fromJson(it, Array<Product>::class.java).toList()
			fragment.itemsVM.setItems(items)
		}
		val onError = Response.ErrorListener {

		}

		// Make request if first time
		val items = fragment.itemsVM.getItems()
		if (items.isEmpty())
		{
			FakeStoreApi.getProductsInCategory(category, view.context, onSuccess, onError )
		}
	}

	override fun cleanAfterYourself()
	{
		activity.findViewById<android.widget.SearchView>(R.id.search_view_am).apply {
			setQuery("", false)
			isVisible = false
		}
	}
}