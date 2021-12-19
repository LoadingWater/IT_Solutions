package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vortex.it_solutions.R
import com.vortex.it_solutions.databinding.FragmentProductsScreenBinding
import com.vortex.it_solutions.fragments.controllers.ProductsScreenController
import com.vortex.it_solutions.view_models.ItemsViewModel

class ProductsScreenFragment : Fragment()
{
	var binding: FragmentProductsScreenBinding? = null
	var controller: ProductsScreenController? = null
	val itemsVM: ItemsViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentProductsScreenBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		controller = ProductsScreenController(this)

		controller!!.initFragmentViews()
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		requireActivity().findViewById<SearchView>(R.id.search_view_am).isVisible = false
		controller = null
		binding = null
	}
}