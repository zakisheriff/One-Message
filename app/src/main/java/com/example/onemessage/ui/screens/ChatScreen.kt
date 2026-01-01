package com.example.onemessage.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.onemessage.ui.theme.OneMessageColors

data class Message(
        val id: String,
        val content: String,
        val timestamp: String,
        val isSent: Boolean,
        val isDelivered: Boolean = false,
        val isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
        conversationId: String,
        contactName: String = "Alice",
        isOnline: Boolean = true,
        onBackClick: () -> Unit = {}
) {
        // Sample messages - in real app from ViewModel
        var messages by remember {
                mutableStateOf(
                        listOf(
                                Message(
                                        "1",
                                        "Hey! Have you tried One Message?",
                                        "9:38 AM",
                                        false,
                                        true,
                                        true
                                ),
                                Message("2", "Yes! Just installed it", "9:39 AM", true, true, true),
                                Message(
                                        "3",
                                        "The encryption is amazing",
                                        "9:40 AM",
                                        true,
                                        true,
                                        true
                                ),
                                Message(
                                        "4",
                                        "And it works offline too!",
                                        "9:41 AM",
                                        false,
                                        true,
                                        true
                                ),
                                Message(
                                        "5",
                                        "That's incredible! How does the offline mode work?",
                                        "9:42 AM",
                                        true,
                                        true,
                                        false
                                ),
                        )
                )
        }

        var inputText by remember { mutableStateOf("") }
        var isTyping by remember { mutableStateOf(false) }
        val listState = rememberLazyListState()

        Scaffold(
                containerColor = OneMessageColors.Background,
                topBar = {
                        ChatTopBar(
                                contactName = contactName,
                                isOnline = isOnline,
                                onBackClick = onBackClick
                        )
                }
        ) { paddingValues ->
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        // Encryption banner
                        EncryptionBanner()

                        // Messages list
                        LazyColumn(
                                modifier = Modifier.weight(1f),
                                state = listState,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                reverseLayout = false
                        ) {
                                items(messages) { message ->
                                        MessageBubble(message = message)
                                        Spacer(modifier = Modifier.height(8.dp))
                                }

                                // Typing indicator
                                if (isTyping) {
                                        item { TypingIndicator() }
                                }
                        }

                        // Input area
                        MessageInput(
                                text = inputText,
                                onTextChange = { inputText = it },
                                onSend = {
                                        if (inputText.isNotBlank()) {
                                                val newMessage =
                                                        Message(
                                                                id = (messages.size + 1).toString(),
                                                                content = inputText.trim(),
                                                                timestamp = "Now",
                                                                isSent = true,
                                                                isDelivered = false
                                                        )
                                                messages = messages + newMessage
                                                inputText = ""
                                        }
                                }
                        )
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(contactName: String, isOnline: Boolean, onBackClick: () -> Unit) {
        TopAppBar(
                navigationIcon = {
                        IconButton(onClick = onBackClick) {
                                Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = OneMessageColors.TextPrimary
                                )
                        }
                },
                title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                // Avatar
                                Box(
                                        modifier =
                                                Modifier.size(40.dp)
                                                        .background(
                                                                OneMessageColors.SurfaceVariant,
                                                                CircleShape
                                                        )
                                                        .border(
                                                                1.dp,
                                                                OneMessageColors.GlassBorder,
                                                                CircleShape
                                                        ),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = contactName.first().toString(),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = OneMessageColors.TextPrimary
                                        )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                        Text(
                                                text = contactName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = OneMessageColors.TextPrimary
                                        )
                                        Text(
                                                text = if (isOnline) "Online" else "Offline",
                                                style = MaterialTheme.typography.bodySmall,
                                                color =
                                                        if (isOnline) OneMessageColors.Online
                                                        else OneMessageColors.TextMuted
                                        )
                                }
                        }
                },
                actions = {
                        // Encryption indicator
                        IconButton(onClick = { /* Show encryption info */}) {
                                Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Encrypted",
                                        tint = OneMessageColors.Online,
                                        modifier = Modifier.size(20.dp)
                                )
                        }
                },
                colors =
                        TopAppBarDefaults.topAppBarColors(containerColor = OneMessageColors.Surface)
        )
}

@Composable
private fun EncryptionBanner() {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(OneMessageColors.GlassBg)
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                ) {
                        Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = OneMessageColors.TextMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                                text = "Messages are end-to-end encrypted",
                                style = MaterialTheme.typography.labelSmall,
                                color = OneMessageColors.TextMuted
                        )
                }
        }
}

@Composable
private fun MessageBubble(message: Message) {
        val bubbleColor =
                if (message.isSent) OneMessageColors.SentBubble else OneMessageColors.ReceivedBubble
        val textColor =
                if (message.isSent) OneMessageColors.SentBubbleText
                else OneMessageColors.ReceivedBubbleText
        val alignment = if (message.isSent) Alignment.CenterEnd else Alignment.CenterStart

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
                Column(
                        horizontalAlignment = if (message.isSent) Alignment.End else Alignment.Start
                ) {
                        Box(
                                modifier =
                                        Modifier.widthIn(max = 280.dp)
                                                .background(
                                                        color = bubbleColor,
                                                        shape =
                                                                RoundedCornerShape(
                                                                        topStart = 18.dp,
                                                                        topEnd = 18.dp,
                                                                        bottomStart =
                                                                                if (message.isSent)
                                                                                        18.dp
                                                                                else 4.dp,
                                                                        bottomEnd =
                                                                                if (message.isSent)
                                                                                        4.dp
                                                                                else 18.dp
                                                                )
                                                )
                                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                                Text(
                                        text = message.content,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = textColor
                                )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                                Text(
                                        text = message.timestamp,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OneMessageColors.TextMuted
                                )

                                if (message.isSent) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                                imageVector =
                                                        when {
                                                                message.isRead ->
                                                                        Icons.Default.DoneAll
                                                                message.isDelivered ->
                                                                        Icons.Default.DoneAll
                                                                else -> Icons.Default.Done
                                                        },
                                                contentDescription =
                                                        when {
                                                                message.isRead -> "Read"
                                                                message.isDelivered -> "Delivered"
                                                                else -> "Sent"
                                                        },
                                                modifier = Modifier.size(14.dp),
                                                tint =
                                                        if (message.isRead) OneMessageColors.Online
                                                        else OneMessageColors.TextMuted
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun TypingIndicator() {
        Box(
                modifier =
                        Modifier.background(
                                        OneMessageColors.ReceivedBubble,
                                        RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(3) { index ->
                                Box(
                                        modifier =
                                                Modifier.size(8.dp)
                                                        .background(
                                                                OneMessageColors.TextMuted,
                                                                CircleShape
                                                        )
                                )
                        }
                }
        }
}

@Composable
private fun MessageInput(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(OneMessageColors.Surface)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Attachment button
                IconButton(
                        onClick = { /* TODO: Open attachment picker */},
                        modifier = Modifier.size(40.dp)
                ) {
                        Icon(
                                Icons.Default.Add,
                                contentDescription = "Attach",
                                tint = OneMessageColors.TextSecondary
                        )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text input
                Box(
                        modifier =
                                Modifier.weight(1f)
                                        .background(
                                                OneMessageColors.GlassBg,
                                                RoundedCornerShape(24.dp)
                                        )
                                        .border(
                                                1.dp,
                                                OneMessageColors.GlassBorder,
                                                RoundedCornerShape(24.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                        BasicTextField(
                                value = text,
                                onValueChange = onTextChange,
                                textStyle =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                color = OneMessageColors.TextPrimary
                                        ),
                                modifier = Modifier.fillMaxWidth(),
                                decorationBox = { innerTextField ->
                                        Box {
                                                if (text.isEmpty()) {
                                                        Text(
                                                                text = "Message",
                                                                color = OneMessageColors.TextMuted,
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyLarge
                                                        )
                                                }
                                                innerTextField()
                                        }
                                }
                        )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Send button
                AnimatedVisibility(visible = text.isNotBlank()) {
                        IconButton(
                                onClick = onSend,
                                modifier =
                                        Modifier.size(44.dp)
                                                .background(OneMessageColors.White, CircleShape)
                        ) {
                                Icon(
                                        Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = OneMessageColors.Black,
                                        modifier = Modifier.size(20.dp)
                                )
                        }
                }
        }
}
