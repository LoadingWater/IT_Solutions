package com.vortex.it_solutions

import android.app.Application
import com.vortex.it_solutions.databases.LocalDB

class CustomApplication: Application()
{
	val localDB by lazy { LocalDB.getDatabase(this) }
}