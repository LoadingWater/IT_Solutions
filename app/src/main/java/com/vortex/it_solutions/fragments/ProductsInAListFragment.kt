package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vortex.it_solutions.databinding.FragmentProductsInAListBinding
import com.vortex.it_solutions.fragments.controllers.ProductsInAListController
import com.vortex.it_solutions.view_models.ProductsInAListViewModel

class ProductsInAListFragment : Fragment()
{
	var binding: FragmentProductsInAListBinding? = null
	var controller: ProductsInAListController? = null
	val productsInAListVM: ProductsInAListViewModel by viewModels()

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

	override fun onDestroyView()
	{
		super.onDestroyView()
		binding = null
		controller = null
	}
}