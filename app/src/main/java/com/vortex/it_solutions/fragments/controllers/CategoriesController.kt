package com.vortex.it_solutions.fragments.controllers

import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.Gson
import com.vortex.it_solutions.R
import com.vortex.it_solutions.adapters.CategoriesAdapter
import com.vortex.it_solutions.api.FakeStoreApi
import com.vortex.it_solutions.fragments.CategoriesFragment
import com.vortex.it_solutions.view_models.CategoriesViewModel.Extensions.toCategories

class CategoriesController(val fragment: CategoriesFragment): BaseController(fragment)
{
	private val binding = fragment.binding!!

	override fun initFragmentViews()
	{
		// Observers
		fragment.categoriesViewModel.categories.observe(fragment.viewLifecycleOwner, Observer {
			binding.recyclerFc.adapter = CategoriesAdapter(it, fragment)
			binding.recyclerFc.layoutAnimation = AnimationUtils.loadLayoutAnimation(view.context, R.anim.categories_layout_animation)
			binding.recyclerFc.scheduleLayoutAnimation()
		})

		// Listeners
		binding.retryFc.setOnClickListener {
			it.isVisible = false
			getCategories()
		}

		binding.recyclerFc.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
		binding.recyclerFc.adapter = CategoriesAdapter(emptyList(), fragment)

		val categories = fragment.categoriesViewModel.categories.value!!
		if (categories.isEmpty())
		{
			getCategories()
		}
	}

	private fun getCategories()
	{
		val onSuccess = Response.Listener<String> { response ->
			val categories = Gson().fromJson(response, Array<String>::class.java).toMutableList().toCategories()

			fragment.categoriesViewModel.categories.value = categories
		}
		val onError = Response.ErrorListener {
			Log.e(TAG, it.message.toString())
			Toast.makeText(fragment.requireContext(), "Connection error: ${it.message}", Toast.LENGTH_LONG).show()
			binding.retryFc.isVisible = true
		}
		FakeStoreApi.getAllCategories(fragment.requireContext(), onSuccess, onError)
	}

	override fun cleanAfterYourself()
	{
	}
}