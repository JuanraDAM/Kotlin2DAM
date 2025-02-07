package com.example.srodenas.example_with_catalogs.data.users.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tblusers")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String,  // Antes: passw
    val phone: String,
    val imag: String       // Antes: imagen
)

@Entity(tableName = "tblalerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "userid") val userId: Int,
    @ColumnInfo(name = "textshort") val textShort: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "alertDate") val alertDate: LocalDate
)
