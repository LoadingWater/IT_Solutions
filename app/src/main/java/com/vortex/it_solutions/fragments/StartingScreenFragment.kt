package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vortex.it_solutions.MainActivity
import com.vortex.it_solutions.R
import com.vortex.it_solutions.databinding.FragmentStartingScreenBinding
import com.vortex.it_solutions.fragments.controllers.BaseController


class StartingScreenFragment : Fragment()
{
	var binding: FragmentStartingScreenBinding? = null
	var controller: BaseController? = null
	//val sharedViewModel: SharedViewModel by lazy { ViewModelProvider(requireActivity()).get(SharedViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentStartingScreenBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		binding?.goShoppingFss?.setOnClickListener {
			findNavController().navigate(R.id.action_startingScreenFragment_to_categoriesFragment)
		}

		binding?.userListsFss?.setOnClickListener {
			findNavController().navigate(R.id.action_startingScreenFragment_to_yourCartFragment)
		}
	}

	override fun onResume()
	{
		super.onResume()
		(activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
		requireActivity().findViewById<ImageButton>(R.id.goToLists_am).isVisible = false
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		(activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
		activity?.findViewById<ImageButton>(R.id.goToLists_am)?.isVisible = true
		controller = null
		binding = null
	}
}