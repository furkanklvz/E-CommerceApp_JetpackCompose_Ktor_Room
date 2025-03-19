package com.klavs.e_commerceapp.helper

fun fixImageUrl(imageUrl: String?): String?{
    return imageUrl?.let { "http://10.0.2.2:5077/images/$it" }
}