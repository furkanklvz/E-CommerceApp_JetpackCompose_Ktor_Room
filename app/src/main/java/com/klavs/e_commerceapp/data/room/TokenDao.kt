package com.klavs.e_commerceapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klavs.e_commerceapp.data.model.entity.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: Token)

    @Query ("SELECT * FROM tokens LIMIT 1")
    fun getTokenFlow(): Flow<Token?>

    @Query ("DELETE FROM tokens WHERE value = :token")
    suspend fun deleteToken(token: String)

}