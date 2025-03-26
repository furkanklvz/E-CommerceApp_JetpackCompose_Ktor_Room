package com.klavs.e_commerceapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("accounts")
data class Account(
    @PrimaryKey(autoGenerate = false) val token: String,
    val createdAt: Long,
    val fullName: String,
    val userName: String,
    val email: String
)
