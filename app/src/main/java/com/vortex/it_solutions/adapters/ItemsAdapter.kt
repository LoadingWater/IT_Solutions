package com.vortex.it_solutions.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vortex.it_solutions.R
import com.vortex.it_solutions.fragments.ProductsScreenFragment
import com.vortex.it_solutions.models.Product
import com.vortex.it_solutions.view_models.SharedViewModel

class ItemsAdapter(var products: List<Product>, val fragment: ProductsScreenFragment): RecyclerView.Adapter<ItemsAdapter.ViewHolder>()
{
	val TAG = CategoriesAdapter::class.java.name
	val sharedViewModel = ViewModelProvider(fragment.requireActivity()).get(SharedViewModel::class.java)
	val itemsCopy = products

	inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
	{
		val item = Product()
		val image = view.findViewById<ImageView>(R.id.itemImage_si)
		var title = view.findViewById<TextView>(R.id.title_si)
		var rating = view.findViewById<TextView>(R.id.rating_si)
		var price = view.findViewById<TextView>(R.id.price_si)
		val root = image.rootView
		init
		{
			root.setOnClickListener {
				sharedViewModel.clickedProduct.value = item
				val nav = Navigation.createNavigateOnClickListener(R.id.action_itemsScreenFragment_to_detailedItemFragment)
				nav.onClick(root)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val view = LayoutInflater.from(parent.context).inflate(R.layout.product_on_the_net_item, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val item = products[position]
		holder.apply {
			title.text = item.title
			rating.text = "${item.rating.rate} (${item.rating.count})"
			price.text = "${item.price} $"
		}

		holder.item.apply {
			id = item.id
			title = item.title
			price = item.price
			description = item.description
			category = item.category
			image = item.image
			rating = item.rating
		}

		Glide.with(holder.image)
			.load(item.image)
			.transition(DrawableTransitionOptions.withCrossFade())
			.into(holder.image)
	}

	override fun getItemCount(): Int
	{
		return products.size
	}

	/**
	 * Function searches products in "itemsCopy" and not modifying it
	 * So every time we type in searchView we search in initial products
	 * Deleting characters from queue will trigger search with less amount of characters
	 * At the end, if use delete everything from searchView, search will be with "" and
	 * It will return initial products setup from api response.
	 */
	fun findItem(title: String)
	{
		val newItems = mutableListOf<Product>()
		itemsCopy.forEach {
			if (it.title.contains(title, true))
			{
				newItems.add(it)
			}
		}
		products = if (newItems.isEmpty()) emptyList() else newItems
		notifyDataSetChanged()
	}
}