package com.cours.cannabiss

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cours.cannabiss.db.DatabaseHelper

class ModifierPlanteActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var planteId: Long = -1

    private lateinit var spinnerEtatSante: Spinner
    private lateinit var editTextDate: EditText
    private lateinit var editTextIdentification: EditText
    private lateinit var editTextProvenance: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerStade: Spinner
    private lateinit var spinnerEntreposage: Spinner
    private lateinit var spinnerActifInactif: Spinner
    private lateinit var editTextDateRetrait: EditText
    private lateinit var spinnerRaisonRetrait: Spinner
    private lateinit var spinnerResponsableDecontamination: Spinner
    private lateinit var editTextNote: EditText
    private lateinit var buttonEnregistrer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier_plante)

        dbHelper = DatabaseHelper(this)

        spinnerEtatSante = findViewById(R.id.spinnerEtatSante)
        editTextDate = findViewById(R.id.editTextDate)
        editTextIdentification = findViewById(R.id.editTextIdentification)
        editTextProvenance = findViewById(R.id.editTextProvenance)
        editTextDescription = findViewById(R.id.editTextDescription)
        spinnerStade = findViewById(R.id.spinnerStade)
        spinnerEntreposage = findViewById(R.id.spinnerEntreposage)
        spinnerActifInactif = findViewById(R.id.spinnerActifInactif)
        editTextDateRetrait = findViewById(R.id.editTextDateRetrait)
        spinnerRaisonRetrait = findViewById(R.id.spinnerRaisonRetrait)
        spinnerResponsableDecontamination = findViewById(R.id.spinnerResponsableDecontamination)
        editTextNote = findViewById(R.id.editTextNote)
        buttonEnregistrer = findViewById(R.id.buttonEnregistrer)

        setupSpinners()

        planteId = intent.getLongExtra("PLANTE_ID", -1)
        if (planteId != -1L) {
            loadPlanteData(planteId)
        }

        buttonEnregistrer.setOnClickListener {
            if (validateInputs()) {
                updatePlanteData(planteId)
            }
        }
    }

    private fun setupSpinners() {
        setupSpinner(spinnerEtatSante, R.array.etat_sante_array)
        setupSpinner(spinnerStade, R.array.stade_array)
        setupSpinner(spinnerEntreposage, R.array.entreposage_array)
        setupSpinner(spinnerActifInactif, R.array.actif_inactif_array)
        setupSpinner(spinnerRaisonRetrait, R.array.raison_retrait_array)
        setupSpinner(spinnerResponsableDecontamination, R.array.responsable_decontamination_array)
    }


    private fun setupSpinner(spinner: Spinner, arrayId: Int) {
        ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun loadPlanteData(id: Long) {
        val cursor: Cursor? = try {
            dbHelper.readableDatabase.query(
                "Plantes",
                null,
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
            null
        }

        cursor?.let {
            if (it.moveToFirst()) {
                val etatSante = it.getString(it.getColumnIndex("etat_sante"))
                val date = it.getString(it.getColumnIndex("date_arrivee"))
                val identification = it.getString(it.getColumnIndex("identification"))
                val provenance = it.getString(it.getColumnIndex("provenance"))
                val description = it.getString(it.getColumnIndex("description"))
                val stade = it.getString(it.getColumnIndex("stade"))
                val entreposage = it.getString(it.getColumnIndex("entreposage"))
                val actifInactif = it.getString(it.getColumnIndex("actif_inactif"))
                val dateRetrait = it.getString(it.getColumnIndex("date_retrait"))
                val raisonRetrait = it.getString(it.getColumnIndex("raison_retrait"))
                val responsableDecontamination = it.getString(it.getColumnIndex("responsable_decontamination"))
                val note = it.getString(it.getColumnIndex("note"))

                spinnerEtatSante.setSelection(getIndex(spinnerEtatSante, etatSante))
                editTextDate.setText(date)
                editTextIdentification.setText(identification)
                editTextProvenance.setText(provenance)
                editTextDescription.setText(description)
                spinnerStade.setSelection(getIndex(spinnerStade, stade))
                spinnerEntreposage.setSelection(getIndex(spinnerEntreposage, entreposage))
                spinnerActifInactif.setSelection(getIndex(spinnerActifInactif, actifInactif))
                editTextDateRetrait.setText(dateRetrait)
                spinnerRaisonRetrait.setSelection(getIndex(spinnerRaisonRetrait, raisonRetrait))
                spinnerResponsableDecontamination.setSelection(getIndex(spinnerResponsableDecontamination, responsableDecontamination))
                editTextNote.setText(note)
            }
            it.close()
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private fun validateInputs(): Boolean {
        // Ajoutez une validation des champs ici, si nécessaire
        return true
    }

    private fun updatePlanteData(id: Long) {
        val etatSante = spinnerEtatSante.selectedItem.toString()
        val date = editTextDate.text.toString()
        val identification = editTextIdentification.text.toString()
        val provenance = editTextProvenance.text.toString()
        val description = editTextDescription.text.toString()
        val stade = spinnerStade.selectedItem.toString()
        val entreposage = spinnerEntreposage.selectedItem.toString()
        val actifInactif = spinnerActifInactif.selectedItem.toString()
        val dateRetrait = editTextDateRetrait.text.toString()
        val raisonRetrait = spinnerRaisonRetrait.selectedItem.toString()
        val responsableDecontamination = spinnerResponsableDecontamination.selectedItem.toString()
        val note = editTextNote.text.toString()

        val contentValues = ContentValues().apply {
            put("etat_sante", etatSante)
            put("date_arrivee", date)
            put("identification", identification)
            put("provenance", provenance)
            put("description", description)
            put("stade", stade)
            put("entreposage", entreposage)
            put("actif_inactif", actifInactif)
            put("date_retrait", dateRetrait)
            put("raison_retrait", raisonRetrait)
            put("responsable_decontamination", responsableDecontamination)
            put("note", note)
        }

        try {
            val rowsUpdated = dbHelper.writableDatabase.update(
                "Plantes",
                contentValues,
                "id = ?",
                arrayOf(id.toString())
            )

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Plante mise à jour avec succès", Toast.LENGTH_SHORT).show()
                finish() // Ferme l'activité et revient à la précédente
            } else {
                Toast.makeText(this, "Erreur lors de la mise à jour de la plante", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur lors de la mise à jour de la plante", Toast.LENGTH_SHORT).show()
        }
    }
}
