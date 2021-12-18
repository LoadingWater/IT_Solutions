package com.vortex.it_solutions.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SelectedListDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun updateId(id: SelectedListId)

	@Query("SELECT * FROM SelectedListId")
	suspend fun getId(): SelectedListId
}
