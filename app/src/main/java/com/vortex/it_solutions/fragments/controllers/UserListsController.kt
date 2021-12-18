package com.vortex.it_solutions.fragments.controllers

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.vortex.it_solutions.CustomApplication
import com.vortex.it_solutions.R
import com.vortex.it_solutions.databases.ListOfProducts
import com.vortex.it_solutions.databases.SelectedListId
import com.vortex.it_solutions.databinding.ListItemBinding
import com.vortex.it_solutions.fragments.UserListsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserListsController(val fragment: UserListsFragment): BaseController(fragment)
{
	var binding = fragment.binding!!
	val productsDao = (activity.application as CustomApplication).localDB.userListsDao()
	val selectedIdDao = (activity.application as CustomApplication).localDB.selectedListDao()

	override fun initFragmentViews()
	{
		// Just loading user's lists here
		lateinit var listOfProducts: List<ListOfProducts>
		CoroutineScope(IO).launch {
			listOfProducts = productsDao.getAllItemsLists()
			// Move inflating on main thread
			view.post {
				listOfProducts.forEach { itemsList ->
					val userListBinding = createUserListView(itemsList)
					binding.userListsFul.addView(userListBinding.root)
				}
			}
		}
	}

	private fun createUserListView(itemsList: ListOfProducts): ListItemBinding
	{
		val itemBinding = ListItemBinding.inflate(fragment.layoutInflater, binding.userListsFul, false)
		// Text
		itemBinding.listIdLi.text = "List id: ${itemsList.id}"
		itemBinding.dateLi.setText(itemsList.createdAt)
		itemBinding.listTitleLi.setText(itemsList.title)
		// Listeners
		itemBinding.root.setOnClickListener { view ->
			Toast.makeText(view.context, "Clicked", Toast.LENGTH_SHORT).show()
			sharedViewModel.clickedList.value = itemsList.id
			fragment.findNavController().navigate(R.id.action_yourCartFragment_to_itemsInOneList)
		}
		itemBinding.deleteListLi.setOnClickListener {
			binding.userListsFul.removeView(itemBinding.root)
			CoroutineScope(IO).launch {
				val id = itemsList.id
				productsDao.deleteOne(id)
				// Reset selected id if selected list was deleted
				if (selectedIdDao.getId().id == id)
				{
					selectedIdDao.updateId(SelectedListId(1))
				}
			}
		}
		return itemBinding
	}

	override fun cleanAfterYourself()
	{

	}
}