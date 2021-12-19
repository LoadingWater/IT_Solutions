package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vortex.it_solutions.databinding.FragmentDetailedProductBinding
import com.vortex.it_solutions.fragments.controllers.DetailedProductController

class DetailedProductFragment : Fragment()
{
	val TAG = DetailedProductFragment::class.java.name
	var binding: FragmentDetailedProductBinding? = null
	var controller: DetailedProductController? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentDetailedProductBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		controller = DetailedProductController(this)
		controller!!.initFragmentViews()
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		controller = null
		binding = null
	}
}