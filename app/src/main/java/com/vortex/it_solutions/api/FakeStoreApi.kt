package com.vortex.it_solutions.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley.newRequestQueue

object FakeStoreApi
{
	private var requestQueue: RequestQueue? = null

	private val allProducts = "https://fakestoreapi.com/products"
	private val singleProductBase = "https://fakestoreapi.com/products/"
	private val allCategories = "https://fakestoreapi.com/products/categories"
	private val specificCategoryBase = "https://fakestoreapi.com/products/category/"


	fun getAllCategories(context: Context, onSuccess: Response.Listener<String>, onError: Response.ErrorListener)
	{
		if (requestQueue == null)
		{
			requestQueue = newRequestQueue(context)
		}
		val request = StringRequest(Request.Method.GET, allCategories, onSuccess, onError)
		requestQueue!!.add(request)
	}

	fun getProductsInCategory(category: String, context: Context, onSuccess: Response.Listener<String>, onError: Response.ErrorListener)
	{
		if (requestQueue == null)
		{
			requestQueue = newRequestQueue(context)
		}
		val finalUrl = "$specificCategoryBase$category"
		val request = StringRequest(Request.Method.GET, finalUrl, onSuccess, onError)
		requestQueue!!.add(request)
	}
}