package fr.acos.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import fr.acos.contentpriverwithkotlin.entities.Personne
import android.arch.persistence.room.Room
import android.content.Context


@Database(entities = [(Personne::class)], version = 1)
abstract class MaBaseDeDonnees : RoomDatabase()
{
    /**
     * Singleton
     */
    companion object {
        private var sInstance: MaBaseDeDonnees? = null

        fun getInstance(context: Context): MaBaseDeDonnees {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder(context.getApplicationContext(), MaBaseDeDonnees::class.java!!, "personnesbdd.db")
                        .build()
            }
            return sInstance as MaBaseDeDonnees
        }
    }

    abstract fun personneDao(): PersonneDao
}
