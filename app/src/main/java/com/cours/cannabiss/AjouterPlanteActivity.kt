package com.cours.cannabiss

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cours.cannabiss.db.DatabaseHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject

class AjouterPlanteActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var qrCodeImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_plante)

        dbHelper = DatabaseHelper(this)
        qrCodeImageView = findViewById(R.id.qrCodeImageView)

        val spinnerEtatSante = findViewById<Spinner>(R.id.spinnerEtatSante)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextIdentification = findViewById<EditText>(R.id.editTextIdentification)
        val editTextProvenance = findViewById<EditText>(R.id.editTextProvenance)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val spinnerStade = findViewById<Spinner>(R.id.spinnerStade)
        val spinnerEntreposage = findViewById<Spinner>(R.id.spinnerEntreposage)
        val spinnerActifInactif = findViewById<Spinner>(R.id.spinnerActifInactif)
        val editTextDateRetrait = findViewById<EditText>(R.id.editTextDateRetrait)
        val spinnerRaisonRetrait = findViewById<Spinner>(R.id.spinnerRaisonRetrait)
        val spinnerResponsableDecontamination = findViewById<Spinner>(R.id.spinnerResponsableDecontamination)
        val editTextNote = findViewById<EditText>(R.id.editTextNote)
        val buttonEnregistrer = findViewById<Button>(R.id.buttonEnregistrer)

        // Adapter pour les spinners
        setupSpinner(spinnerEtatSante, R.array.etat_sante_array)
        setupSpinner(spinnerStade, R.array.stade_array)
        setupSpinner(spinnerEntreposage, R.array.entreposage_array)
        setupSpinner(spinnerActifInactif, R.array.actif_inactif_array)
        setupSpinner(spinnerRaisonRetrait, R.array.raison_retrait_array)
        setupSpinner(spinnerResponsableDecontamination, R.array.responsable_decontamination_array)

        buttonEnregistrer.setOnClickListener {
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

            Log.d("AjouterPlanteActivity", "Inserting: $etatSante, $date, $identification, $provenance, $description, $stade, $entreposage, $actifInactif, $dateRetrait, $raisonRetrait, $responsableDecontamination, $note")

            // Insérer les données dans la base de données
            val db = dbHelper.writableDatabase
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
                val newRowId = db.insert("Plantes", null, contentValues)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Plante ajoutée avec succès", Toast.LENGTH_SHORT).show()

                    // Générer le QR code avec les informations de la plante
                    val planteData = JSONObject().apply {
                        put("identification", identification)
                        put("etat_sante", etatSante)
                        put("date_arrivee", date)
                        put("provenance", provenance)
                        put("description", description)
                        put("stade", stade)
                        put("entreposage", entreposage)
                        put("actif_inactif", actifInactif)
                        put("date_retrait", dateRetrait)
                        put("raison_retrait", raisonRetrait)
                        put("responsable_decontamination", responsableDecontamination)
                        put("note", note)
                    }.toString()

                    generateQRCode(planteData)

                    // Lancez ConsulterPlanteActivity
                    val intent = Intent(this, ConsulterPlanteActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Erreur lors de l'ajout de la plante", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AjouterPlanteActivity", "Error inserting data: ${e.message}")
                Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
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

    private fun generateQRCode(data: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400)
            qrCodeImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
