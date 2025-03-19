package com.klavs.e_commerceapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.klavs.e_commerceapp.data.model.entity.Token

@Database(version = 1, entities = [Token::class])
abstract class ECommerceDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao

    companion object {
        private var INSTANCE: ECommerceDatabase? = null

        fun getInstance(context: Context): ECommerceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, ECommerceDatabase::class.java, "eCommerce")
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}