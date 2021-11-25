package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "employee")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var firstName: String? = "Администратор",
    var secondName: String? = "",
    var phone: String? = "",
    var pinCode : String,
    var login : String? = "",
    var isChanged: Int? = 0,
    var permissionGroupName:String? = "",
    var terminalId: Long? = null,
    var DepartmentId: Long? = null
) : Serializable
