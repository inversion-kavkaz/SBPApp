package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terminal")
data class Terminal(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var bankId: Long? = null,
    var merchantId: Long? = null
)
