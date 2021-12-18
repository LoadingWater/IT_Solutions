package com.vortex.it_solutions.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vortex.it_solutions.models.Category

class CategoriesViewModel: ViewModel()
{
	val categories = MutableLiveData<MutableList<Category>>(mutableListOf())
	fun getCategories() = categories.value!!
	fun updateResults(index: Int, number: String)
	{
		if (index in 0 until categories.value!!.size)
		{
			categories.value!![index].results = number
		}
	}

	object Extensions
	{
		fun MutableList<String>.toCategories(): MutableList<Category>
		{
			val categories = mutableListOf<Category>()
			this.forEachIndexed { index, category ->
				val cat = Category()
				cat.category = category
				cat.results = ""
				categories.add(index, cat)
			}
			return categories
		}
	}
}