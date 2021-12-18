package com.vortex.it_solutions.databases

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vortex.it_solutions.models.Product

@Entity
data class ListOfProducts(
	val title: String,
	val products: List<Product>,
	val createdAt: String
)
{
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	constructor(
		id: Int,
		title: String,
		products: List<Product>,
		createdAt: String
	) : this(title, products, createdAt)
}