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
	//NOTE: For some reason it just doesn't work if you pass 0 here
	// Every other number is fine.
	// What's with auto increment?
	@PrimaryKey(autoGenerate = true)
	var id: Int = 1
}