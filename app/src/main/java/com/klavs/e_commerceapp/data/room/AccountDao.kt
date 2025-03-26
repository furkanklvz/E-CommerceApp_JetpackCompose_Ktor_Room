package com.klavs.e_commerceapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klavs.e_commerceapp.data.model.entity.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAccount(account: Account)

    @Query ("SELECT * FROM accounts LIMIT 1")
    fun getAccountFlow(): Flow<Account?>

    @Query ("DELETE FROM accounts WHERE token = :token")
    suspend fun deleteAccount(token: String)

}