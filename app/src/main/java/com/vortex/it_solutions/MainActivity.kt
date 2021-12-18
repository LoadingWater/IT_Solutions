package com.vortex.it_solutions

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(findViewById(R.id.my_toolbar_am))
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		findViewById<ImageButton>(R.id.goToLists_am).setOnClickListener {
			val nav = Navigation.findNavController(this, R.id.main_activity_navigation_am)
			nav.navigate(R.id.action_global_yourCartFragment)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			android.R.id.home -> onBackPressed()
		}
		return super.onOptionsItemSelected(item)
	}
}