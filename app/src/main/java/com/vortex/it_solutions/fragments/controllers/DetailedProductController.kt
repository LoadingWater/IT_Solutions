package com.vortex.it_solutions.fragments.controllers

import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vortex.it_solutions.CustomApplication
import com.vortex.it_solutions.databases.ListOfProducts
import com.vortex.it_solutions.databases.SelectedListId
import com.vortex.it_solutions.fragments.DetailedProductFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailedProductController(val fragment: DetailedProductFragment): BaseController(fragment)
{
	val binding = fragment.binding!!

	val selectedListIdDao = (activity.application as CustomApplication).localDB.selectedListDao()
	val itemsDao = (activity.application as CustomApplication).localDB.userListsDao()

	override fun initFragmentViews()
	{
		// Listeners
		binding.toListDp.setOnClickListener {
			CoroutineScope(IO).launch {
				// If there are no created lists
				val lists = itemsDao.getAllItemsLists()
				if (lists.isEmpty())
				{
					createNewListAndSelect()
				}
				// Push to the selected list
				val message = if (addProductToSelectedList()) "Added to your list" else "Failed to add"
				it.post {
					Toast.makeText(it.context, message, Toast.LENGTH_SHORT).show()
				}
			}
		}
		// Setting up page info
		val item = sharedViewModel.clickedProduct.value!!
		Glide.with(view)
			.load(item.image)
			.transition(DrawableTransitionOptions.withCrossFade())
			.into(binding.productImageDp)
		binding.productTitleDp.text = item.title
		binding.productRatingDp.text = "${item.rating?.rate} (${item.rating?.count})"
		binding.priceDi.text = "${item.price} $"
		binding.productDescriptionDp.text = item.description
	}

	private suspend fun createNewListAndSelect()
	{
		val sdf = SimpleDateFormat("dd/M/yyyy")
		val currentDate = sdf.format(Date())
		val items = ListOfProducts("First list", emptyList(), currentDate)
		itemsDao.insertOneList(items)
		selectedListIdDao.updateId(SelectedListId(items.id))
	}

	/**
	 * @return returns true if added successfully. False otherwise
	 */
	private suspend fun addProductToSelectedList(): Boolean
	{
		val id =  selectedListIdDao.getId().id
		val oldList = itemsDao.getListById(id)
		if (oldList != null)
		{
			val newProducts = oldList.products.toMutableList()
			val product = sharedViewModel.clickedProduct.value!!
			newProducts.add(product)

			val newList = ListOfProducts(oldList.title, newProducts, oldList.createdAt)
			newList.id = id
			itemsDao.insertOneList(newList)
			return true
		}
		else
		{
			Log.e(TAG, "Old list is null")
			return false
		}
	}

	override fun cleanAfterYourself()
	{
	}
}