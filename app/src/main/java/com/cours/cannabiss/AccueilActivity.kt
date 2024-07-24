package com.cours.cannabiss

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AccueilActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_ajouter -> {
                startActivity(Intent(this, AjouterPlanteActivity::class.java))
            }
            R.id.nav_consulter -> {
                startActivity(Intent(this, ConsulterPlanteActivity::class.java))
            }
            R.id.nav_modifier -> {
                startActivity(Intent(this, ModifierPlanteActivity::class.java))
            }
            R.id.nav_historique -> {
                startActivity(Intent(this, HistoriquePlanteActivity::class.java))
            }
            R.id.nav_archiver -> {
                startActivity(Intent(this, ArchiverPlanteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
