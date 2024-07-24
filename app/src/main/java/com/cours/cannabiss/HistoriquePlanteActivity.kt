package com.cours.cannabiss

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.cours.cannabiss.db.DatabaseHelper
import android.util.Log

class HistoriquePlanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historique_plante)

        try {
            val listView: ListView = findViewById(R.id.listView)
            Log.d("HistoriquePlante", "ListView initialized.")
            val db = DatabaseHelper(this)
            Log.d("HistoriquePlante", "DatabaseHelper initialized.")
            val historiqueList = db.getAllPlanteHistory()
            Log.d("HistoriquePlante", "History list retrieved. Size: ${historiqueList.size}")
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, historiqueList)
            listView.adapter = adapter
            Log.d("HistoriquePlante", "Adapter set to ListView.")
        } catch (e: Exception) {
            Log.e("HistoriquePlante", "Error: ${e.message}")
        }
    }
}
