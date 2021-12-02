package ru.inversion.sbpapplication.utils

public object AppConstants {

    const val plusTimeZone : Long = 10_800_000L
    const val BASE_URL = "http://10.0.2.2:5000/"

    const val tempOGRN  = "1064205040668"
    const val tempPIN = "1234"

    /**Permission*/
    val permissionList: Map<String, String> = mapOf(
        "ADMIN" to "Администратор системы",
        "DEPARTMENTBOSS" to "Руководитель торговой точки",
        "MERCHANT" to "Продавец"
    )
}