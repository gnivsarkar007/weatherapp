package com.rtl.android.assignment.cache.database.entity

import androidx.room.Entity

@Entity(tableName = TABLE_NAME, primaryKeys = ["name", "latitude", "longitude"])
data class CityEntity constructor(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

const val TABLE_NAME = "cities"
const val SELECT = "SELECT * FROM $TABLE_NAME"
const val DELETE = "DELETE FROM $TABLE_NAME WHERE name = "