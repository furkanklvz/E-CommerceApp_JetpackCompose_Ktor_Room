package com.klavs.e_commerceapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tokens")
data class Token(
    @PrimaryKey(autoGenerate = false) val value: String,
    val createdAt: Long,
)
