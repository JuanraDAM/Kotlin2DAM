package com.example.srodenas.example_with_catalogs.domain.alerts.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tblalerts")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,  // Valor por defecto 0, Room lo reemplazará por un id generado
    val userId: Int,
    val textShort: String,
    val message: String,
    val alertDate: LocalDate
)
