package com.vortex.it_solutions.fragments.controllers

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vortex.it_solutions.MainActivity
import com.vortex.it_solutions.view_models.SharedViewModel

abstract class BaseController(fragment: Fragment)
{
	val TAG = fragment::class.java.simpleName
	val activity = fragment.requireActivity() as MainActivity
	val view = fragment.requireView()
	//protected val safeDB = (activity.application as CustomApplication).safeDB
	val sharedViewModel: SharedViewModel = ViewModelProvider(fragment.requireActivity()).get(SharedViewModel::class.java)
	//val newSharedViewModel: SharedViewModel = ViewModelProvider(fragment.requireActivity()).get(SharedViewModel::class.java)
	//val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)


	/**
	 * Sets up fragment views
	 *
	 * Collects all your inits/listeners/subscribers etc. in one place.
	 */
	abstract fun initFragmentViews()

	/**
	 * In this function you must null all your allocated objects or anything
	 * that can affect application after a controller is destroyed.
	 *
	 * Ie. if you postDelay something and controlled gets destroyed you could get you result
	 * on another screen. Like hiding ui after delay and closing this fragment will hide ui in
	 * another fragment instead.
	 */
	abstract fun cleanAfterYourself()
}