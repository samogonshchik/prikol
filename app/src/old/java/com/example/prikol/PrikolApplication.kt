package com.example.prikol

import android.app.Application
import android.content.Context
import com.example.prikol.data.TermDao
import com.example.prikol.data.TermDatabase

class PrikolApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

//AppDataContainer and AppContainer

//interface AppContainer {
//    val termDao: TermsRepository
//}

class AppDataContainer(private val context: Context) {
    val termDao: TermDao by lazy {
        TermDatabase.getDatabase(context).termDao()
    }
}

