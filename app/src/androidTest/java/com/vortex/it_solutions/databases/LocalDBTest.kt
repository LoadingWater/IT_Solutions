package com.vortex.it_solutions.databases

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vortex.it_solutions.models.Product
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDBTest
{

	private lateinit var db: LocalDB
	private lateinit var listsDao: UserListsDao

	@Before
	fun setUp()
	{
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, LocalDB::class.java).build()
		listsDao = db.userListsDao()
	}

	@After
	fun tearDown()
	{
		db.close()
	}

	@Test
	fun insert_list_and_return_it_from_db()
	{
		val product = Product()
		product.price = 13231f
		product.title = "Title product"
		product.category = "category"

		val itemsList = ListOfProducts("Title", listOf(product, product, product), "Now")
		itemsList.id = 1

		runBlocking {
			listsDao.insertOneList(itemsList)
			val result = listsDao.getListById(1)
			if (result != null)
			{
				assertEquals(itemsList.products.size, result.products.size)
			}
		}
	}

	@Test
	fun insert_list_then_update_it_and_return_new()
	{
		val product = Product()
		product.price = 13231f
		product.title = "Test product"
		product.category = "Test category"
		val listOne = listOf(product, product, product)

		val itemsList = ListOfProducts("Test", listOne, "Now")
		itemsList.id = 1

		runBlocking {
			listsDao.insertOneList(itemsList)

			listsDao.updateListProducts(listOf(product), 1)
			val result = listsDao.getListById(1)
			assertEquals(1, result?.products?.size)
		}
	}
}