package com.example.srodenas.example_with_catalogs.data.users.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.srodenas.example_with_catalogs.data.users.database.entities.AlertEntity
import com.example.srodenas.example_with_catalogs.data.users.database.entities.UserEntity
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM tblusers WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM tblusers")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM tblusers WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("UPDATE tblusers SET name = :nuevoNombre WHERE id = :userId")
    suspend fun updateUserName(userId: Int, nuevoNombre: String)
}



@Dao
interface AlertDao {
    @Insert
    suspend fun insertAlert(alert: AlertEntity): Long

    @Query("SELECT * FROM tblalerts WHERE userid = :userId")
    suspend fun getAlertsForUser(userId: Int): List<AlertEntity>

    @Query("SELECT * FROM tblalerts WHERE id = :alertId")
    suspend fun getAlertById(alertId: Int): AlertEntity?

    @Query("DELETE FROM tblalerts WHERE id = :alertId")
    suspend fun deleteAlertById(alertId: Int)
}