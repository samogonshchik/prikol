package com.example.prikol.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms_table")
data class Term(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String = "",
    val name: String = "",
    val definition: String = ""
)
