package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "department")
data class Department(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = "",
    var address: String? = "",
    var phone: String? ="",
    var bossName: String? = ""
) : Serializable
