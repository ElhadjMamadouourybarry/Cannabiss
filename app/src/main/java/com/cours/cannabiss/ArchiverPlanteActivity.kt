package com.cours.cannabiss

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cours.cannabiss.db.DatabaseHelper
import android.util.Log

class ArchiverPlanteActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var planteList: MutableList<Pair<Int, String>>
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedPlanteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archiver_plante)

        db = DatabaseHelper(this)
        listView = findViewById(R.id.listView)

        updatePlanteList()

        listView.setOnItemLongClickListener { parent, view, position, id ->
            // Get the ID of the selected plant
            selectedPlanteId = planteList[position].first
            showPopupMenu(view)
            true
        }
    }

    private fun updatePlanteList() {
        try {
            planteList = db.getAllPlantesToArchive().toMutableList()
            val displayList = planteList.map { it.second }
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
            listView.adapter = adapter
            Log.d("ArchiverPlante", "Adapter set to ListView.")
        } catch (e: Exception) {
            Log.e("ArchiverPlante", "Error: ${e.message}")
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    confirmDelete()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Voulez-vous vraiment supprimer cette plante ?")
            .setPositiveButton("Oui") { _, _ ->
                deletePlante()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun deletePlante() {
        try {
            selectedPlanteId?.let { id ->
                val success = db.archivePlante(id)
                if (success) {
                    Log.d("ArchiverPlante", "Plante supprimée avec succès.")
                    updatePlanteList()
                } else {
                    Log.e("ArchiverPlante", "Échec de la suppression de la plante.")
                }
            }
        } catch (e: Exception) {
            Log.e("ArchiverPlante", "Error during deletion: ${e.message}")
        }
    }
}
