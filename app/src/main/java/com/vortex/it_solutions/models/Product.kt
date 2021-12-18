package com.vortex.it_solutions.models

data class Product(
	var id: Int = 0,
	var title: String = "N/A",
	var price: Float = 0.0f,
	var description: String = "N/A",
	var category: String = "N/A",
	var image: String = "N/A",
	var rating: Rating = Rating()
)
{
	data class Rating(
		var rate: Float = 0.0f,
		var count: Int = 0
	)
}