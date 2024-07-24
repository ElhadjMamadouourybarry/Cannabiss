package com.cours.cannabiss;

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class ExportDatabaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_database)

        // Code pour exporter la base de données
        exportDatabase()
    }

    private fun exportDatabase() {
        val dbFile = getDatabasePath("cannabiss.db")
        val exportFile = File(getExternalFilesDir(null), "cannabiss_backup.db")

        dbFile.inputStream().use { input ->
            FileOutputStream(exportFile).use { output ->
                input.copyTo(output)
            }
        }

        Toast.makeText(this, "Base de données exportée avec succès", Toast.LENGTH_SHORT).show()
    }
}
