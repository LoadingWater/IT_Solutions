package com.vortex.it_solutions.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vortex.it_solutions.models.Product

@Dao
interface UserListsDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertOneList(newListOfProducts: ListOfProducts): Long

	@Query("SELECT * FROM ListOfProducts WHERE id LIKE :id")
	suspend fun getListById(id: Int): ListOfProducts?

	@Query("SELECT * FROM ListOfProducts")
	suspend fun getAllItemsLists(): List<ListOfProducts>

	@Query("UPDATE ListOfProducts SET products = :newProducts WHERE id LIKE :listId")
	suspend fun updateListProducts(newProducts: List<Product>, listId: Int)

	@Query("UPDATE ListOfProducts SET createdAt = :newDate WHERE id LIKE :listId")
	suspend fun updateListCreationDate(newDate: String, listId: Int)

	@Query("DELETE FROM ListOfProducts WHERE :listId LIKE ListOfProducts.id")
	suspend fun deleteOne(listId: Int)

	@Query("DELETE FROM ListOfProducts")
	suspend fun deleteAll()
}
