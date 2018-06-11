package fr.acos.contentpriverwithkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues
import android.net.Uri
import android.util.Log


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "XXX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG,"CREATE");

                /**
         * Manipulation du ContentProvider personnel
         */
        //Récupération du contentResolver
        val cr = contentResolver

        /**
         * BOUCHON
         */
        val personneAInserer = ContentValues()
        personneAInserer.put("id", 0)
        personneAInserer.put("nom", "nom_test")
        personneAInserer.put("prenom", "prenom_test")

        val personneAUpdater = ContentValues()
        personneAUpdater.put("id", 3)
        personneAUpdater.put("nom", "modif_nom")
        personneAUpdater.put("prenom", "modif_prenom")

        /**
         * INSERT
         *
         * Insertion de trois personne
         */
        var URL_INSERT = "content://fr.acos.MonContentProvider/personnes"
        Thread({
            cr.insert(Uri.parse(URL_INSERT), personneAInserer)
            cr.insert(Uri.parse(URL_INSERT), personneAInserer)
            cr.insert(Uri.parse(URL_INSERT), personneAInserer)
        }).start()

        /**
         * DELETE
         *
         * Suppression de la personne ayant l'id 1.
         */
        var URL_DELETE = "content://fr.acos.MonContentProvider/personnes/1"
        Thread(
                {
                    Log.i(TAG, "DELETE Retour : ${cr.delete(Uri.parse(URL_DELETE), null, null)}")
                }).start()

        /**
         * UPDATE
         *
         * Mise à jour de la personne ayant l'id 3
         */
        var URL_UPDATE = "content://fr.acos.MonContentProvider/personnes"
        Thread({ cr.update(Uri.parse(URL_UPDATE), personneAUpdater,null,null) }).start()

        /**
         * QUERY ALL
         *
         * Récupération de toutes les personness.
         */
        var URL_RECUP = "content://fr.acos.MonContentProvider/personnes"
        Thread(
                {
                    val cursor = cr.query(Uri.parse(URL_RECUP), null, null, null, null)

                    while (cursor.moveToNext()) {
                        Log.i(TAG, "${cursor.getString(0)} - ${cursor.getString(1)} - ${cursor.getString(2)}");
                    }
                }).start()

        /**
         * QUERY ONE
         *
         * Récupération de la personne ayant l'id 2
         */
        var URL_RECUP_ONE = "content://fr.acos.MonContentProvider/personnes/1"
        Thread(
                {
                    val cursor = cr.query(Uri.parse(URL_RECUP_ONE), null, null, null, null)

                    while (cursor.moveToNext()) {
                        Log.i(TAG, "${cursor.getString(0)} - ${cursor.getString(1)} - ${cursor.getString(2)}");
                    }
                }).start()
    }

}

