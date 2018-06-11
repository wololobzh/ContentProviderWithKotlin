package fr.acos.contentpriverwithkotlin.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import fr.acos.contentpriverwithkotlin.entities.Personne
import fr.acos.dao.MaBaseDeDonnees

/**
 * Classe représentant le ContentProvider
 */
class MonContentProvider : ContentProvider()
{
    companion object
        {
            private val TAG = "XXX"
            /*
            * Nom de content provider
            */
            private val AUTHORITY = "fr.acos.MonContentProvider"

            /**
             * Code indiquant qu'il y a un id de personne dans l'URI
             */
            private val CODE_PERSONNE_DIR = 1

            /**
             * Code indiquant qu'il n'y a pas d'id de personne dans l'URI
             */
            private val CODE_PERSONNE_ITEM = 2

            /**
             * Objet de type UriMatcher permettant d'analyser facilement les URI.
             */
            private val MATCHER = UriMatcher (UriMatcher.NO_MATCH)

            /**
             * Utilisé pour la fonction getType()
             */
            private val TYPE_MIME_PERSONNE = "vnd.android.cursor.item/fr.acos.contentpriverwithkotlin.entities.Personne";
        }

    init
    {
        //Initialisation de l'objet MATCHER
        //Le code CODE_PERSONNE_DIR est associé à l'uri content://fr.acos.MonContentProvider/personnes
        MATCHER.addURI(
                AUTHORITY,
                Personne.TABLE_NAME,
                CODE_PERSONNE_DIR
        );
        //Le code CODE_PERSONNE_DIR est associé à l'uri content://fr.acos.MonContentProvider/personnes/*
        // * représente n'importe quel nombre. Ce nombre correcpond à un identifiant de personne.
        MATCHER.addURI(
                AUTHORITY,
                "${Personne.TABLE_NAME}/*",
                CODE_PERSONNE_ITEM
        );
    }

    /**
     * Permet d’initialiser les ressources pour le ContentProvider.
     *
     * @return Boolean
     */
    override fun onCreate(): Boolean
    {
        return false
    }

    /**
     * Fonction permettant d'ajouter une personne
     *
     * @param uri uri du content provider
     * @param values informations relatives à la personne à inserer.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri?
    {
        Log.i(TAG,"Fonction insert. uri = $uri")
        when (MATCHER.match(uri))
        {
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes
            CODE_PERSONNE_DIR ->
            {
                //Création d'un objet représentant la base de données.
                val db = MaBaseDeDonnees.getInstance(context)
                //Création d'un objet représentant la DAO pour les personnes.
                val dao = db.personneDao()
                //Transformation du ContentValue en objet de type Personne.
                val personneAInserer = itemBuilder(values)
                //Insertion en base. id est égal à l'identifiant du nouvel enregistrement.
                val id = dao.insert(personneAInserer)
                //Retourn l'uri avec le nouvel identifiant à la fin.
                return ContentUris.withAppendedId(uri,id)
            }
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes/*
            CODE_PERSONNE_ITEM -> throw IllegalArgumentException("Pas d'ID nécessaire pour l'insertion")
            else -> throw IllegalArgumentException("Informations erronées")
        }
    }

    /**
     * Fonction permettant de supprimer une personne.
     *
     * @param uri uri du content provider
     * @param selection inutile
     * @param selectionArgs inutile
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int
    {
        Log.i(TAG,"Fonction delete. uri = $uri")

        when (MATCHER.match(uri))
        {
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes/*
            CODE_PERSONNE_ITEM ->
            {
                //Récupération de l'identifiant à la fin de l'uri
                val id = uri.lastPathSegment
                //Création d'un objet représentant la base de données.
                val db =  MaBaseDeDonnees.getInstance(context)
                //Création d'un objet représentant la DAO pour les personnes.
                val dao = db.personneDao()
                //Création d'un objet de type Personne avec seulement un identifiant.
                val personneASupprimer = Personne(id.toLong())
                //Suppression de la base de données.
                val nombreDeSuppression = dao.deletePersonne(personneASupprimer)
                //retourne le nombre de ligne supprimées.
                return nombreDeSuppression
            }
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes
            CODE_PERSONNE_DIR ->throw IllegalArgumentException("ID nécessaire pour la suppression")
            else -> throw IllegalArgumentException("Informations erronées")
        }
    }

    /**
     * Fonction permettant de récupérer des personnes.
     *
     * @param uri uri du content provider
     * @param projection inutile
     * @param selection inutile
     * @param selectionArgs inutile
     * @param sortOrder inutile
     */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor?
    {
        Log.i(TAG,"Fonction query. uri = $uri")
        Log.i(TAG,"Fonction query. Matcher.match(uri) = ${MATCHER.match(uri)}")
        var cursor:Cursor?
        //Création d'un objet représentant la base de données.
        val db = MaBaseDeDonnees.getInstance(context)
        //Création d'un objet représentant la DAO pour les personnes.
        val dao = db.personneDao()

        when (MATCHER.match(uri))
        {
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes/*
            CODE_PERSONNE_ITEM ->
            {
                //Récupération de l'identifiant à la fin de l'uri
                val id = uri.lastPathSegment
                //Retourne un objet de type Cursor contenant une personne.
                cursor = dao.getInCursor(id.toLong())
            }
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes
            CODE_PERSONNE_DIR ->
            {
                //Retourne un objet de type Cursor contenant plusieurs personnes.
                cursor = dao.getInCursor()
            }
            else -> throw IllegalArgumentException("Informations erronées")
        }
        return cursor;
    }

    /**
     * Fonction permettant de mettre à jour des personnes
     *
     * @param uri uri du content provider
     * @param values  informations relatives à la personne à modifier.
     * @param selection inutile
     * @param selectionArgs inutile
     */
    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        Log.i(TAG,"Fonction update. uri = $uri")
        Log.i(TAG,"Fonction update. Matcher.match(uri) = ${MATCHER.match(uri)}")

        when (MATCHER.match(uri))
        {
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes/*
            CODE_PERSONNE_DIR ->
            {
                //Création d'un objet représentant la base de données.
                val db = MaBaseDeDonnees.getInstance(context)
                //Création d'un objet représentant la DAO pour les personnes.
                val dao = db.personneDao()
                //Transformation du ContentValue en objet de type Personne.
                val personneAMettreAJour = itemBuilder(values);
                //Mise à jour en base de données.
                val nombreDeMiseAjour = dao.updatePersonne(personneAMettreAJour)
                //Retourne le nombre d'élément mis à jour.
                return nombreDeMiseAjour
            }
            //Si l'uri est égal à content://fr.acos.MonContentProvider/personnes
            CODE_PERSONNE_ITEM -> throw IllegalArgumentException("ID non nécessaire pour la mise à jour")
            else -> throw IllegalArgumentException("Informations erronées")
        }
    }

    /**
     * Fonction permettant d'indiquer le type des données traité par le ContentProvider.
     *
     * @param uri URI du content provider
     */
    override fun getType(uri: Uri) =
            when (MATCHER.match(uri))
            {
                CODE_PERSONNE_DIR,CODE_PERSONNE_ITEM -> TYPE_MIME_PERSONNE;
                else -> null
            }

    /**
     * Permet de créer un objet de type Personne à l'aide d'un ContentValue
     *
     * @param valeurs objet de type ContentValues contenant les informations sur une personne.
     *
     * @return Un objet de type Personne
     */
    fun itemBuilder(valeurs:ContentValues?)  = Personne (
                valeurs?.getAsLong("id")!!,
                valeurs?.getAsString("nom"),
                valeurs?.getAsString("prenom"))
}
