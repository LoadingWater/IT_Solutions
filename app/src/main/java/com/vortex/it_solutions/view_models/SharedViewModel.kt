package com.vortex.it_solutions.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vortex.it_solutions.models.Product

class SharedViewModel: ViewModel()
{
	val selectedCategory = MutableLiveData<String>("")
	val clickedProduct = MutableLiveData<Product>(Product())
	//val clickedList = MutableLiveData<Int>(0)
}