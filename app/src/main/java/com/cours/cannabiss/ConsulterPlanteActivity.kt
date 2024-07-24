package com.cours.cannabiss

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cours.cannabiss.db.DatabaseHelper

class ConsulterPlanteActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var adapter: PlanteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter_plante)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listViewPlantes)

        // Configurez l'adaptateur pour la ListView
        adapter = PlanteAdapter()
        listView.adapter = adapter

        // Chargez les données
        loadPlantes()
    }

    private fun loadPlantes() {
        val cursor: Cursor = dbHelper.readableDatabase.query(
            "Plantes",
            null,
            null,
            null,
            null,
            null,
            null
        )

        val plantesList = mutableListOf<Plante>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val etatSante = cursor.getString(cursor.getColumnIndex("etat_sante"))
            val identification = cursor.getString(cursor.getColumnIndex("identification"))
            plantesList.add(Plante(id, etatSante, identification))
        }
        cursor.close()

        // Mettez à jour l'adaptateur avec les nouvelles données
        adapter.setPlantes(plantesList)
    }

    inner class PlanteAdapter : BaseAdapter() {

        private var plantesList = mutableListOf<Plante>()

        fun setPlantes(plantes: List<Plante>) {
            this.plantesList.clear()
            this.plantesList.addAll(plantes)
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return plantesList.size
        }

        override fun getItem(position: Int): Any {
            return plantesList[position]
        }

        override fun getItemId(position: Int): Long {
            return plantesList[position].id
        }

        override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            val plante = getItem(position) as Plante
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = "ID: ${plante.id}, État de santé: ${plante.etatSante}, Identification: ${plante.identification}"

            view.setOnClickListener {
                showPopupMenu(it, plante)
            }

            return view
        }

        private fun showPopupMenu(view: View, plante: Plante) {
            val popupMenu = PopupMenu(this@ConsulterPlanteActivity, view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_modifier -> {
                        val intent = Intent(this@ConsulterPlanteActivity, ModifierPlanteActivity::class.java)
                        intent.putExtra("PLANTE_ID", plante.id)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_supprimer -> {
                        supprimerPlante(plante.id)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }


        private fun supprimerPlante(id: Long) {
            dbHelper.writableDatabase.delete("Plantes", "id=?", arrayOf(id.toString()))
            loadPlantes()
            Toast.makeText(this@ConsulterPlanteActivity, "Plante supprimée", Toast.LENGTH_SHORT).show()
        }
    }

    data class Plante(val id: Long, val etatSante: String, val identification: String)
}
