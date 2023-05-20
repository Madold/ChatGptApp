package com.markusw.chatgptapp.ui.view.screens.main.composables

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.markusw.chatgptapp.data.model.ChatMessage
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.ui.TestTags.BOT_AVATAR
import com.markusw.chatgptapp.ui.TestTags.CHAT_ITEM
import com.markusw.chatgptapp.ui.TestTags.COPY_BOT_MESSAGE_BUTTON
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var clipboardManager: ClipboardManager

    @Test
    fun check_If_ChatItem_Is_Rendered() {
        val messageContent = "Hello"
        val messageRole = MessageRole.User

        composeTestRule.setContent {
            ChatItem(chat = ChatMessage(messageContent, messageRole))
        }

        composeTestRule.onNodeWithTag(CHAT_ITEM).assertExists()
    }

    @Test
    fun when_Message_IsFromUser_BotAvatar_IsNot_Rendered() {
        val messageContent = "Hello"
        val messageRole = MessageRole.User

        composeTestRule.setContent {
            ChatItem(chat = ChatMessage(messageContent, messageRole))
        }

        composeTestRule.onNodeWithTag(BOT_AVATAR).assertDoesNotExist()
    }

    @Test
    fun when_Message_IsFromBot_BotAvatar_Is_Rendered() {
        val messageContent = "Hi, how i can assist you?"
        val messageRole = MessageRole.Bot

        composeTestRule.setContent {
            ChatItem(chat = ChatMessage(messageContent, messageRole))
        }

        composeTestRule.onNodeWithTag(BOT_AVATAR).assertExists()
    }

    @Test
    fun when_Message_IsFromBot_CopyMessageButton_IsRendered_And_OnClick_Message_IsCopied_To_ClipBoard() {
        val messageContent = "Hi, how i can assist you?"
        val messageRole = MessageRole.Bot

        composeTestRule.setContent {
            clipboardManager = LocalClipboardManager.current
            ChatItem(chat = ChatMessage(messageContent, messageRole))
        }

        composeTestRule.onNodeWithTag(COPY_BOT_MESSAGE_BUTTON).assertExists()
        composeTestRule.onNodeWithTag(COPY_BOT_MESSAGE_BUTTON).performClick()
        assert(clipboardManager.getText()?.text == messageContent)
    }

}