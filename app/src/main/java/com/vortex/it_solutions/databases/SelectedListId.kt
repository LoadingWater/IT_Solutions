package com.vortex.it_solutions.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SelectedListId(
	@PrimaryKey val id: Int = 1
)