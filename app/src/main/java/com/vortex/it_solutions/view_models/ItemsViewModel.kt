package com.vortex.it_solutions.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vortex.it_solutions.models.Product

class ItemsViewModel: ViewModel()
{
	val items = MutableLiveData<List<Product>>(listOf())
	fun getItems() = items.value!!
	fun setItems(products: List<Product>)
	{
		this.items.value = products
	}
}