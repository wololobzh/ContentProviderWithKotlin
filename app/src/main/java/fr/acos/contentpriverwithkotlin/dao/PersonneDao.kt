package fr.acos.dao

import android.arch.persistence.room.*
import android.database.Cursor
import fr.acos.contentpriverwithkotlin.entities.Personne

@Dao
interface PersonneDao {

    @Query("SELECT * FROM personnes")
    fun get(): List<Personne>

    @Query("SELECT * FROM personnes")
    fun getInCursor(): Cursor

    @Query("SELECT * FROM personnes WHERE id = :id")
    fun get(id: Long): Personne

    @Query("SELECT * FROM personnes WHERE id = :id")
    fun getInCursor(id: Long): Cursor

    @Insert
    fun insertAll(vararg listePersonne: Personne)

    @Insert
    fun insert(personne: Personne):Long

    @Update
    fun updatePersonne(item: Personne):Int

    @Delete
    fun deletePersonne(item: Personne):Int
}