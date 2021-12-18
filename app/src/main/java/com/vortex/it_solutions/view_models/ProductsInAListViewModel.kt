package com.vortex.it_solutions.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vortex.it_solutions.models.Product

class ProductsInAListViewModel: ViewModel()
{
	val unparsedProducts = MutableLiveData<List<Product>>(emptyList())
	fun getUnparsedProducts() = unparsedProducts.value!!
	fun setUnparsedProducts(products: List<Product>?)
	{
		if (products != null)
		{
			unparsedProducts.value = products
		}
	}

	val productsMap = MutableLiveData<Map<Product, Int>>(hashMapOf())
	fun getProducts() = productsMap.value
	fun setProducts(products: List<Product>?)
	{
		val freq: MutableMap<Product, Int> = HashMap()
		products?.forEachIndexed { index, item ->
			if (!freq.contains(item))
			{
				freq[item] = 1
			} else
			{
				freq[item] = freq.getValue(item) + 1
			}
		}
		// I first wrote this function and later, in testing, find out that modifying products
		// count changes their order in a list. That's why sorting function
		freq.toSortedMap(compareByDescending { it.id })
		this.productsMap.value = freq
	}
}