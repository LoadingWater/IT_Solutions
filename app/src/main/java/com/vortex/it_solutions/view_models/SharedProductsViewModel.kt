package com.vortex.it_solutions.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vortex.it_solutions.databases.ListOfProducts
import com.vortex.it_solutions.models.Product

class SharedProductsViewModel: ViewModel()
{
	// region UserLists
	val userLists = MutableLiveData<List<ListOfProducts>>(null)
	fun getUserLists(): List<ListOfProducts>
	{
		if (userLists.value == null)
		{
			userLists.value = emptyList()
		}
		return userLists.value!!
	}

	fun setUserLists(lists: List<ListOfProducts>)
	{
		userLists.value = lists
	}

	fun updateListTitleNotNotify(listId: Int, newTitle: String)
	{
		val allLists = getUserLists().toMutableList()
		val oneList = allLists.find { it.id == listId }
		oneList?.title = newTitle
	}

	fun updateListDateNotNotify(listId: Int, newDate: String)
	{
		val allLists = getUserLists()
		val oneList = allLists.find { it.id == listId }
		oneList?.createdAt = newDate
	}

	fun deleteList(listId: Int)
	{
		val lists = getUserLists().toMutableList()
		lists.removeAll { it.id == listId }
		setUserLists(lists)
	}

	val selectedListId = MutableLiveData<Int>(0)
	val clickedListId = MutableLiveData<Int>(0)
	// endregion UserLists

	// region ProductsInAList

	fun getProductsInCurrentList(): List<Product>?
	{
		val lists = getUserLists()
		val list = lists.find { it.id == clickedListId.value!! }
		return list?.products
	}
	fun updateProductsInCurrentList(products: List<Product>?)
	{
		if (products != null)
		{
			val lists = getUserLists()
			val list = lists.find { it.id == clickedListId.value!! }
			list?.products = products
			setUserLists(lists)
		}
	}

	companion object
	{
		fun List<Product>.getFreqMap(): MutableMap<Product, Int>
		{
			val freq: MutableMap<Product, Int> = HashMap()
			this.forEachIndexed { index, item ->
				if (!freq.contains(item))
				{
					freq[item] = 1
				}
				else
				{
					freq[item] = freq.getValue(item) + 1
				}
			}
			freq.toSortedMap(compareByDescending { it.id })
			return freq
		}
	}
	// endregion ProductsInAList
}