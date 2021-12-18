package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vortex.it_solutions.databinding.FragmentCategoriesBinding
import com.vortex.it_solutions.fragments.controllers.BaseController
import com.vortex.it_solutions.fragments.controllers.CategoriesController
import com.vortex.it_solutions.view_models.CategoriesViewModel

class CategoriesFragment : Fragment()
{
	val TAG = CategoriesFragment::class.java.name
	var binding: FragmentCategoriesBinding? = null
	var controller: BaseController? = null
	val categoriesViewModel: CategoriesViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentCategoriesBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		controller = CategoriesController(this)
		controller!!.initFragmentViews()

	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		binding = null
		controller = null
	}
}