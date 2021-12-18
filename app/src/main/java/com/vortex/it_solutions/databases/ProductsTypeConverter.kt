package com.vortex.it_solutions.databases

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vortex.it_solutions.models.Product

class ProductsTypeConverter
{
	@TypeConverter
	fun saveItems(products: List<Product>): String
	{
		return Gson().toJson(products)
	}

	@TypeConverter
	fun restoreItems(itemsConverted: String): List<Product>
	{
		return Gson().fromJson(itemsConverted, object: TypeToken<List<Product>>(){}.type)
	}
}