package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String
){
    companion object {
        val DEFAULT_COLORS = listOf(
            ProfileDbModel(1, "#FFFFFF", "White"),
            ProfileDbModel(2, "#E57373", "Red"),
            ProfileDbModel(3, "#F06292", "Pink"),
            ProfileDbModel(4, "#CE93D8", "Purple"),
            ProfileDbModel(5, "#2196F3", "Blue"),
            ProfileDbModel(6, "#00ACC1", "Cyan"),
            ProfileDbModel(7, "#26A69A", "Teal"),
            ProfileDbModel(8, "#4CAF50", "Green"),
            ProfileDbModel(9, "#8BC34A", "Light Green"),
            ProfileDbModel(10, "#CDDC39", "Lime"),
            ProfileDbModel(11, "#FFEB3B", "Yellow"),
            ProfileDbModel(12, "#FF9800", "Orange"),
            ProfileDbModel(13, "#BCAAA4", "Brown"),
            ProfileDbModel(14, "#9E9E9E", "Gray")
        )
        val DEFAULT_COLOR = DEFAULT_COLORS[0]
    }
}
