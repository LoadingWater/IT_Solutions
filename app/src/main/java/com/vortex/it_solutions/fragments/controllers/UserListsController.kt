package com.vortex.it_solutions.fragments.controllers

import android.app.Service
import android.os.Vibrator
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vortex.it_solutions.CustomApplication
import com.vortex.it_solutions.R
import com.vortex.it_solutions.databases.ListOfProducts
import com.vortex.it_solutions.databases.SelectedListId
import com.vortex.it_solutions.databinding.ListItemBinding
import com.vortex.it_solutions.fragments.UserListsFragment
import com.vortex.it_solutions.view_models.SharedProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class UserListsController(val fragment: UserListsFragment): BaseController(fragment)
{
	var binding = fragment.binding!!
	val productsDao = (activity.application as CustomApplication).localDB.userListsDao()
	val selectedIdDao = (activity.application as CustomApplication).localDB.selectedListDao()
	val vm = ViewModelProvider(activity).get(SharedProductsViewModel::class.java)

	override fun initFragmentViews()
	{
		Log.i("testing", "initFragmentViews(). User")
		// Observers
		CoroutineScope(IO).launch {
			createSelectedId(1)
			val id = selectedIdDao.getId().id
			val listOfProducts = productsDao.getAllItemsLists()
			view.post {
				vm.selectedListId.value = id
				vm.setUserLists(listOfProducts)
			}
		}
		vm.userLists.observe(fragment.viewLifecycleOwner, { allLists ->
			if (allLists == null)
			{
				Log.i("Testing", "Lists are null")
			}
			else
			{
				Log.i("Testing", "Lists are not null")
				binding.userListsFul.removeAllViews()
				allLists.forEach { oneList ->
					val userListBinding = createUserListView(oneList)
					binding.userListsFul.addView(userListBinding.root)
				}
			}
		})

		// Listeners
		binding.floatingActionButtonFul.setOnClickListener {
			CoroutineScope(IO).launch {
				createNewUserList()
			}
		}
	}

	private suspend fun createSelectedId(id: Int)
	{
		if (selectedIdDao.getId() == null)
		{
			selectedIdDao.createId(SelectedListId(id))
		}
	}

	private suspend fun createNewUserList()
	{
		val sdf = SimpleDateFormat("dd/M/yyyy")
		val currentDate = sdf.format(Date())

		val allLists = vm.getUserLists().toMutableList()
		val newList = ListOfProducts("List number ${allLists.size + 1}", emptyList(), currentDate)
		val newId = productsDao.insertOneList(newList)
		// Updating VM
		// Cause id is autogenerated we can't set it and need to make db call to get created list
		val listFromDb = productsDao.getListById(newId.toInt())
		allLists.add(listFromDb!!)

		CoroutineScope(Main).launch {
			vm.setUserLists(allLists)
			if (allLists.size == 1)
			{
				vm.selectedListId.value = allLists[0].id
				vm.userLists.value = vm.userLists.value
			}
		}
	}

	private fun createUserListView(itemsList: ListOfProducts): ListItemBinding
	{
		val itemBinding = ListItemBinding.inflate(fragment.layoutInflater, binding.userListsFul, false)
		// Text
		itemBinding.dateLi.setText(itemsList.createdAt)
		itemBinding.listTitleLi.setText(itemsList.title)
		if (itemsList.id == vm.selectedListId.value)
		{
			itemBinding.root.setBackgroundDrawable(ResourcesCompat.getDrawable(view.resources, R.drawable.list_selected_bg, null))
		}
		// Listeners
		// Open list
		itemBinding.root.setOnClickListener { view ->
			vm.clickedListId.value = itemsList.id
			fragment.findNavController().navigate(R.id.action_yourCartFragment_to_itemsInOneList)
		}
		// Remove list
		itemBinding.deleteListLi.setOnClickListener {
			binding.userListsFul.removeView(itemBinding.root)

			vm.deleteList(itemsList.id)
			// Reset selected id if selected list was deleted
			if (vm.selectedListId.value == itemsList.id)
			{
				if (vm.getUserLists().isNotEmpty())
				{
					vm.selectedListId.value = vm.getUserLists().first().id
					vm.userLists.value = vm.userLists.value
				}
			}
		}
		// Change title
		itemBinding.listTitleLi.setOnFocusChangeListener { v, hasFocus ->
			if (!hasFocus)
			{
				val view = v as EditText
				vm.updateListTitleNotNotify(itemsList.id, view.text.toString())
			}
		}
		// Change date
		itemBinding.dateLi.setOnFocusChangeListener { v, hasFocus ->
			if (!hasFocus)
			{
				val view = v as EditText
				vm.updateListDateNotNotify(itemsList.id, view.text.toString())
			}
		}
		// Select list
		itemBinding.root.setOnLongClickListener {
			Toast.makeText(it.context, "New list selected", Toast.LENGTH_SHORT).show()
			val vibrator = view.context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
			vibrator.vibrate(50)
			vm.selectedListId.value = itemsList.id
			vm.userLists.value = vm.userLists.value
			true
		}
		return itemBinding
	}

	fun safeToDB()
	{
		runBlocking {
			productsDao.deleteAll()
			productsDao.insertAllLists(vm.getUserLists())
			selectedIdDao.updateSelectedListId(vm.selectedListId.value!!)
		}
	}

	override fun cleanAfterYourself()
	{
	}
}