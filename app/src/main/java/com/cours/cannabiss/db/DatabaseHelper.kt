package com.cours.cannabiss.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.cours.cannabiss.model.Plante

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CannabisDB"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "Plantes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ETAT_SANTE = "etat_sante"
        private const val COLUMN_DATE_ARRIVEE = "date_arrivee"
        private const val COLUMN_IDENTIFICATION = "identification"
        private const val COLUMN_PROVENANCE = "provenance"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_STADE = "stade"
        private const val COLUMN_ENTREPOSAGE = "entreposage"
        private const val COLUMN_ACTIF_INACTIF = "actif_inactif"
        private const val COLUMN_DATE_RETRAIT = "date_retrait"
        private const val COLUMN_RAISON_RETRAIT = "raison_retrait"
        private const val COLUMN_RESPONSABLE_DECONTAMINATION = "responsable_decontamination"
        private const val COLUMN_NOTE = "note"
        private const val COLUMN_DATE_MODIFICATION = "date_modification"
        private const val COLUMN_ARCHIVE = "archive"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_ETAT_SANTE TEXT, "
                + "$COLUMN_DATE_ARRIVEE TEXT, "
                + "$COLUMN_IDENTIFICATION TEXT, "
                + "$COLUMN_PROVENANCE TEXT, "
                + "$COLUMN_DESCRIPTION TEXT, "
                + "$COLUMN_STADE TEXT, "
                + "$COLUMN_ENTREPOSAGE TEXT, "
                + "$COLUMN_ACTIF_INACTIF TEXT, "
                + "$COLUMN_DATE_RETRAIT TEXT, "
                + "$COLUMN_RAISON_RETRAIT TEXT, "
                + "$COLUMN_RESPONSABLE_DECONTAMINATION TEXT, "
                + "$COLUMN_NOTE TEXT, "
                + "$COLUMN_DATE_MODIFICATION TEXT, "
                + "$COLUMN_ARCHIVE INTEGER DEFAULT 0)")
        db?.execSQL(createTable)
    }


    /*
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }*/

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) { // Assurez-vous que c'est la version avant ajout des colonnes
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_DATE_MODIFICATION TEXT")
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_ARCHIVE INTEGER DEFAULT 0")
        }
        // Vous pouvez ajouter d'autres conditions pour différentes versions
    }


    fun addPlante(plante: Plante): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ETAT_SANTE, plante.etatSante)
            put(COLUMN_DATE_ARRIVEE, plante.dateArrivee)
            put(COLUMN_IDENTIFICATION, plante.identification)
            put(COLUMN_PROVENANCE, plante.provenance)
            put(COLUMN_DESCRIPTION, plante.description)
            put(COLUMN_STADE, plante.stade)
            put(COLUMN_ENTREPOSAGE, plante.entreposage)
            put(COLUMN_ACTIF_INACTIF, plante.actifInactif)
            put(COLUMN_DATE_RETRAIT, plante.dateRetrait)
            put(COLUMN_RAISON_RETRAIT, plante.raisonRetrait)
            put(COLUMN_RESPONSABLE_DECONTAMINATION, plante.responsableDecontamination)
            put(COLUMN_NOTE, plante.note)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L
    }

    fun updatePlante(plante: Plante): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ETAT_SANTE, plante.etatSante)
            put(COLUMN_DATE_ARRIVEE, plante.dateArrivee)
            put(COLUMN_IDENTIFICATION, plante.identification)
            put(COLUMN_PROVENANCE, plante.provenance)
            put(COLUMN_DESCRIPTION, plante.description)
            put(COLUMN_STADE, plante.stade)
            put(COLUMN_ENTREPOSAGE, plante.entreposage)
            put(COLUMN_ACTIF_INACTIF, plante.actifInactif)
            put(COLUMN_DATE_RETRAIT, plante.dateRetrait)
            put(COLUMN_RAISON_RETRAIT, plante.raisonRetrait)
            put(COLUMN_RESPONSABLE_DECONTAMINATION, plante.responsableDecontamination)
            put(COLUMN_NOTE, plante.note)
            put(COLUMN_DATE_MODIFICATION, System.currentTimeMillis().toString())
        }
        val result = db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(plante.id.toString()))
        db.close()
        return result > 0
    }

    fun getAllPlanteHistory(): List<String> {
        val list = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE_MODIFICATION DESC", null)

        // Vérifiez que le curseur a les colonnes attendues
        val idIndex = cursor.getColumnIndex(COLUMN_ID)
        val etatSanteIndex = cursor.getColumnIndex(COLUMN_ETAT_SANTE)
        val dateModificationIndex = cursor.getColumnIndex(COLUMN_DATE_MODIFICATION)

        if (idIndex != -1 && etatSanteIndex != -1 && dateModificationIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(idIndex)
                    val etatSante = cursor.getString(etatSanteIndex)
                    val dateModification = cursor.getString(dateModificationIndex)
                    list.add("ID: $id, Etat de Santé: $etatSante, Date de Modification: $dateModification")
                } while (cursor.moveToNext())
            }
        } else {
            // Gérez les colonnes manquantes ici si nécessaire
            Log.e("DatabaseError", "Une ou plusieurs colonnes sont manquantes dans le résultat de la requête.")
        }

        cursor.close()
        db.close()
        return list
    }


    fun getAllPlantesToArchive(): List<Pair<Int, String>> {
        val list = ArrayList<Pair<Int, String>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ARCHIVE = 0", null)

        // Vérifiez que le curseur a les colonnes attendues
        val idIndex = cursor.getColumnIndex(COLUMN_ID)
        val etatSanteIndex = cursor.getColumnIndex(COLUMN_ETAT_SANTE)

        if (idIndex != -1 && etatSanteIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(idIndex)
                    val etatSante = cursor.getString(etatSanteIndex)
                    list.add(Pair(id, "ID: $id, Etat de Santé: $etatSante"))
                } while (cursor.moveToNext())
            }
        } else {
            // Gérez les colonnes manquantes ici si nécessaire
            Log.e("DatabaseError", "Une ou plusieurs colonnes sont manquantes dans le résultat de la requête.")
        }

        cursor.close()
        db.close()
        return list
    }



    /*
    fun archivePlante(planteId: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ARCHIVE, 1)
        }
        val result = db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(planteId.toString()))
        db.close()
        return result > 0
    }
    */

    fun archivePlante(planteId: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(planteId.toString())) > 0
        db.close()
        return success
    }

    fun printTableSchema() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("PRAGMA table_info($TABLE_NAME)", null)

        // Vérifiez si le curseur contient des colonnes
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val columnType = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                Log.d("DatabaseSchema", "Column: $columnName, Type: $columnType")
            } while (cursor.moveToNext())
        } else {
            Log.e("DatabaseSchema", "Aucune colonne trouvée.")
        }

        cursor.close()
        db.close()
    }

}
