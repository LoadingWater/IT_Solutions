package com.vortex.it_solutions.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vortex.it_solutions.databinding.FragmentUserListsBinding
import com.vortex.it_solutions.fragments.controllers.UserListsController

class UserListsFragment : Fragment()
{
	var binding: FragmentUserListsBinding? = null
	var controller: UserListsController? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = FragmentUserListsBinding.inflate(layoutInflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		controller = UserListsController(this)
		controller!!.initFragmentViews()
	}

	override fun onDestroyView()
	{
		super.onDestroyView()
		binding = null
		controller = null
	}
}