package com.markusw.chatgptapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.markusw.chatgptapp.core.common.Constants.USER_CHATS_DB
import com.markusw.chatgptapp.data.database.entities.ChatHistoryItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM $USER_CHATS_DB")
    fun getChatHistory(): Flow<MutableList<ChatHistoryItemEntity>>

    @Query("DELETE FROM $USER_CHATS_DB")
    suspend fun deleteAllChats()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChat(chat: ChatHistoryItemEntity)

    @Query("DELETE FROM $USER_CHATS_DB WHERE id = :id")
    suspend fun deleteChatById(id: Int)
}






