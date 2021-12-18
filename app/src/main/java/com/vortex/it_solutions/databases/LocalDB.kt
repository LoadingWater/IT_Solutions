package com.vortex.it_solutions.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ListOfProducts::class, SelectedListId::class], version = 1, exportSchema = false)
@TypeConverters(ProductsTypeConverter::class)
abstract class LocalDB: RoomDatabase()
{
	abstract fun userListsDao(): UserListsDao
	abstract fun selectedListDao(): SelectedListDao

	companion object {
		// Singleton prevents multiple instances of database opening at the
		// same time.
		@Volatile
		private var INSTANCE: LocalDB? = null

		fun getDatabase(context: Context): LocalDB {
			// if the INSTANCE is not null, then return it,
			// if it is, then create the database
			return LocalDB.INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					LocalDB::class.java,
					"LocalDB"
				).build()
				LocalDB.INSTANCE = instance
				// return instance
				instance
			}
		}
	}
}