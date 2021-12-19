package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vortex.it_solutions.databinding.FragmentProductsInAListBinding
import com.vortex.it_solutions.fragments.controllers.ProductsInAListController

class ProductsInAListFragment : Fragment()
{
	var binding: FragmentProductsInAListBinding? = null
	var controller: ProductsInAListController? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentProductsInAListBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		controller = ProductsInAListController(this)
		controller!!.initFragmentViews()

	}

	override fun onResume()
	{
		Log.i("testing", "On resume Products")
		super.onResume()
	}

	override fun onPause()
	{
		controller!!.saveToDB()
		Log.i("testing", "On pause Products")
		super.onPause()
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		controller?.cleanAfterYourself()
		controller = null
		binding = null
	}
}