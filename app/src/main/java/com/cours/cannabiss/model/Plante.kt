package com.cours.cannabiss.model

data class Plante(
    val id: Int = 0,
    val etatSante: String,
    val dateArrivee: String,
    val identification: String,
    val provenance: String,
    val description: String,
    val stade: String,
    val entreposage: String,
    val actifInactif: String,
    val dateRetrait: String,
    val raisonRetrait: String,
    val responsableDecontamination: String,
    val note: String
)
