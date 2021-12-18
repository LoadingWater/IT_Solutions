package com.vortex.it_solutions.fragments.controllers

import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.vortex.it_solutions.CustomApplication
import com.vortex.it_solutions.databinding.ProductInAListItemBinding
import com.vortex.it_solutions.fragments.ProductsInAListFragment
import com.vortex.it_solutions.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductsInAListController(val fragment: ProductsInAListFragment): BaseController(fragment)
{
	val binding = fragment.binding!!
	val productsDao = (activity.application as CustomApplication).localDB.userListsDao()
	val selectedListIdDao = (activity.application as CustomApplication).localDB.selectedListDao()
	val vm = fragment.productsInAListVM

	override fun initFragmentViews()
	{
		// Observers
		vm.productsMap.observe(fragment.viewLifecycleOwner, Observer { map ->
			binding.userListsFpial.removeAllViews()
			var total = 0f
			map.forEach { mapEntry ->
				val productBinding = createOneProductView(mapEntry.key, mapEntry.value)
				binding.userListsFpial.addView(productBinding.root)
				total += mapEntry.key.price * mapEntry.value
			}

			binding.totalFpial.text = "${"%.2f".format(total)} $"
		})

		var products: List<Product>? = null
		CoroutineScope(IO).launch {
			products = productsDao.getListById(sharedViewModel.clickedList.value!!)?.products
		}.invokeOnCompletion {
			view.post {
				vm.setUnparsedProducts(products)
				vm.setProducts(products)
			}
		}
	}

	fun removeProductFromTheList(productId: Int)
	{
		runBlocking {
			val listId = selectedListIdDao.getId().id

			val newProducts = vm.getUnparsedProducts().toMutableList()
			newProducts.removeAll { it.id == productId }
			// Resetting unparsed products cause other wise this function will be using first init value
			// Event after products deletion
			vm.setUnparsedProducts(newProducts)
			vm.setProducts(newProducts)

			productsDao.updateListProducts(newProducts, listId)
		}
	}

	private fun createOneProductView(product: Product, amount: Int): ProductInAListItemBinding
	{
		// Inflating views for each product in selected list and adding them to LinearLayout
		val productView = ProductInAListItemBinding.inflate(fragment.layoutInflater, binding.userListsFpial, false)

		Glide.with(view.context).load(product.image).into(productView.productImagePiali)
		productView.productTitlePiali.text = product.title
		productView.amountPiali.setText(amount.toString())
		productView.productPricePiali.text = "${product.price * amount} $"

		productView.decreasePiali.setOnClickListener {
			CoroutineScope(IO).launch {
				decreaseAmount(product)
			}
		}
		productView.increasePiali.setOnClickListener {
			CoroutineScope(IO).launch {
				increaseAmount(product)
			}
		}
		productView.removeProductPiali.setOnClickListener {
			removeProductFromTheList(product.id)
		}
		return productView
	}

	private suspend fun decreaseAmount(product: Product)
	{
		// get list from db -> modify -> update db -> update view model
		val id = selectedListIdDao.getId().id
		val newProducts: MutableList<Product>? = productsDao.getListById(id)?.products?.toMutableList()
		if (newProducts != null)
		{
			if (newProducts.count { it == product } > 1)
			{
				newProducts.remove(product)
			}
			productsDao.updateListProducts(newProducts, id)
			CoroutineScope(Main).launch {
				vm.setUnparsedProducts(newProducts)
				vm.setProducts(newProducts)
			}
		}
	}

	private suspend fun increaseAmount(product: Product)
	{
		// get list from db -> modify -> update db -> update view model
		val id = selectedListIdDao.getId().id
		val newProducts: MutableList<Product>? = productsDao.getListById(id)?.products?.toMutableList()
		if (newProducts != null)
		{
			newProducts.add(product)
			productsDao.updateListProducts(newProducts, id)
			CoroutineScope(Main).launch {
				vm.setUnparsedProducts(newProducts)
				vm.setProducts(newProducts)
			}
		}
	}

	override fun cleanAfterYourself()
	{

	}
}