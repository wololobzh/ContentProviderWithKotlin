package fr.acos.contentpriverwithkotlin.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "personnes")
data class Personne(
        @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name="nom") var nom: String = "",
        @ColumnInfo(name="prenom") var prenom: String = ""
)
{
    companion object {
        val TABLE_NAME = "personnes"
    }
}