package com.vortex.it_solutions.fragments.controllers

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.vortex.it_solutions.CustomApplication
import com.vortex.it_solutions.databinding.ProductInAListItemBinding
import com.vortex.it_solutions.fragments.ProductsInAListFragment
import com.vortex.it_solutions.models.Product
import com.vortex.it_solutions.view_models.SharedProductsViewModel
import com.vortex.it_solutions.view_models.SharedProductsViewModel.Companion.getFreqMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductsInAListController(val fragment: ProductsInAListFragment): BaseController(fragment)
{
	val binding = fragment.binding!!
	val productsDao = (activity.application as CustomApplication).localDB.userListsDao()
	val selectedListIdDao = (activity.application as CustomApplication).localDB.selectedListDao()
	val vm = ViewModelProvider(activity).get(SharedProductsViewModel::class.java)

	override fun initFragmentViews()
	{
		Log.i("testing", "initFragmentViews(). Products")
		// Observers
		vm.userLists.observe(fragment.viewLifecycleOwner, Observer { lists ->
			val products = vm.getProductsInCurrentList()
			if (products == null)
			{
				CoroutineScope(IO).launch {
					val clicked = vm.clickedListId.value!!
					Log.i(TAG, clicked.toString())
					val productsDB: List<Product>? = productsDao.getListById(clicked)?.products
					view.post {
						vm.updateProductsInCurrentList(productsDB)
					}
				}
			}
			else
			{
				binding.userListsFpial.removeAllViews()
				var total = 0f
				products.getFreqMap().forEach { entry ->
					val productBinding = createOneProductView(entry.key, entry.value)
					binding.userListsFpial.addView(productBinding.root)
					total += entry.key.price * entry.value
				}
				binding.totalFpial.text = "${"%.2f".format(total)} $"
			}
		})
	}

	fun removeProductFromTheList(productId: Int)
	{
		val currentProducts = vm.getProductsInCurrentList()!!.toMutableList()
		currentProducts.removeAll { it.id == productId }
		vm.updateProductsInCurrentList(currentProducts)
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
			decreaseAmount(product)
		}
		productView.increasePiali.setOnClickListener {
			increaseAmount(product)
		}
		productView.removeProductPiali.setOnClickListener {
			removeProductFromTheList(product.id)
		}
		return productView
	}

	private fun decreaseAmount(product: Product)
	{
		val currentProducts = vm.getProductsInCurrentList()!!.toMutableList()
		if (currentProducts.count { it == product } > 1)
		{
			currentProducts.remove(product)
		}
		vm.updateProductsInCurrentList(currentProducts)
	}

	private fun increaseAmount(product: Product)
	{
		val currentProducts = vm.getProductsInCurrentList()!!.toMutableList()
		currentProducts.add(product)
		vm.updateProductsInCurrentList(currentProducts)
	}

	fun saveToDB()
	{
		runBlocking {
			val a = vm.getProductsInCurrentList()
			if (a != null)
			{
				productsDao.updateListProducts(a, vm.clickedListId.value!!)
			}
		}
	}

	override fun cleanAfterYourself()
	{
		Log.i("testing", "cleanAfterYourself(). Products")
//		val a = vm.getProductsInCurrentList()
//		if (a == null)
//		{
//			Log.e(TAG, "Products is current list are null")
//		}
//		else
//		{
//			Log.e(TAG, "Clearing products")
//			runBlocking {
//				productsDao.updateListProducts(a, vm.clickedListId.value!!)
//				val test = productsDao.getListById(11)
//			}
//		}

	}
}