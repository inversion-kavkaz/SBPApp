package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id: Long? =null,
    var legalId: String,
    var name: String,
    var ogrn: String? = null,
    var inn: String? = null,
    var firstEnter: Int? = 0,
    var acc: String? = null
): Serializable
