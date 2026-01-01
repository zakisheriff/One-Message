package com.example.onemessage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.onemessage.ui.theme.OneMessageColors

data class Conversation(
        val id: String,
        val name: String,
        val lastMessage: String,
        val timestamp: String,
        val unreadCount: Int = 0,
        val isOnline: Boolean = false,
        val avatarInitial: String = name.firstOrNull()?.toString() ?: "?"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
        onConversationClick: (String) -> Unit = {},
        onNewChatClick: () -> Unit = {},
        onSettingsClick: () -> Unit = {}
) {
        // Sample data - in real app, this would come from ViewModel/Repository
        val conversations = remember {
                listOf(
                        Conversation(
                                "1",
                                "Alice",
                                "Hey! Have you tried One Message?",
                                "9:41 AM",
                                2,
                                true
                        ),
                        Conversation(
                                "2",
                                "Bob",
                                "The encryption is amazing",
                                "Yesterday",
                                0,
                                false
                        ),
                        Conversation("3", "Charlie", "Let's meet tomorrow", "Monday", 0, true),
                        Conversation("4", "Diana", "Thanks for the help!", "Sunday", 1, false),
                        Conversation("5", "Eve", "See you at the event", "Last week", 0, false),
                )
        }

        var searchQuery by remember { mutableStateOf("") }
        var showSearch by remember { mutableStateOf(false) }

        Scaffold(
                containerColor = OneMessageColors.Background,
                topBar = {
                        ChatListTopBar(
                                onSearchClick = { showSearch = !showSearch },
                                onSettingsClick = onSettingsClick
                        )
                },
                floatingActionButton = {
                        FloatingActionButton(
                                onClick = onNewChatClick,
                                containerColor = OneMessageColors.White,
                                contentColor = OneMessageColors.Black,
                                shape = RoundedCornerShape(16.dp)
                        ) { Icon(Icons.Default.Add, contentDescription = "New Chat") }
                }
        ) { paddingValues ->
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        // Search bar
                        if (showSearch) {
                                SearchBar(
                                        query = searchQuery,
                                        onQueryChange = { searchQuery = it },
                                        modifier =
                                                Modifier.padding(
                                                        horizontal = 16.dp,
                                                        vertical = 8.dp
                                                )
                                )
                        }

                        // Conversations list
                        LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 88.dp)
                        ) {
                                items(
                                        conversations.filter {
                                                it.name.contains(searchQuery, ignoreCase = true) ||
                                                        it.lastMessage.contains(
                                                                searchQuery,
                                                                ignoreCase = true
                                                        )
                                        }
                                ) { conversation ->
                                        ConversationItem(
                                                conversation = conversation,
                                                onClick = { onConversationClick(conversation.id) }
                                        )
                                }
                        }
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatListTopBar(onSearchClick: () -> Unit, onSettingsClick: () -> Unit) {
        TopAppBar(
                title = {
                        Column {
                                Text(
                                        text = "Messages",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = OneMessageColors.TextPrimary
                                )
                        }
                },
                actions = {
                        IconButton(onClick = onSearchClick) {
                                Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = OneMessageColors.TextSecondary
                                )
                        }
                        IconButton(onClick = onSettingsClick) {
                                Icon(
                                        Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        tint = OneMessageColors.TextSecondary
                                )
                        }
                },
                colors =
                        TopAppBarDefaults.topAppBarColors(
                                containerColor = OneMessageColors.Background
                        )
        )
}

@Composable
private fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        modifier: Modifier = Modifier
) {
        TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search messages...", color = OneMessageColors.TextMuted) },
                leadingIcon = {
                        Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = OneMessageColors.TextMuted
                        )
                },
                modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                colors =
                        TextFieldDefaults.colors(
                                focusedContainerColor = OneMessageColors.GlassBg,
                                unfocusedContainerColor = OneMessageColors.GlassBg,
                                focusedIndicatorColor = OneMessageColors.GlassBorder,
                                unfocusedIndicatorColor = OneMessageColors.GlassBorder,
                                cursorColor = OneMessageColors.White,
                                focusedTextColor = OneMessageColors.TextPrimary,
                                unfocusedTextColor = OneMessageColors.TextPrimary
                        ),
                singleLine = true
        )
}

@Composable
private fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .clickable(onClick = onClick)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Avatar with online indicator
                Box {
                        Box(
                                modifier =
                                        Modifier.size(56.dp)
                                                .background(
                                                        color = OneMessageColors.SurfaceVariant,
                                                        shape = CircleShape
                                                )
                                                .border(
                                                        width = 1.dp,
                                                        color = OneMessageColors.GlassBorder,
                                                        shape = CircleShape
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = conversation.avatarInitial,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OneMessageColors.TextPrimary
                                )
                        }

                        // Online indicator
                        if (conversation.isOnline) {
                                Box(
                                        modifier =
                                                Modifier.size(14.dp)
                                                        .align(Alignment.BottomEnd)
                                                        .background(
                                                                OneMessageColors.Online,
                                                                CircleShape
                                                        )
                                                        .border(
                                                                2.dp,
                                                                OneMessageColors.Background,
                                                                CircleShape
                                                        )
                                )
                        }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = conversation.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight =
                                                if (conversation.unreadCount > 0)
                                                        FontWeight.SemiBold
                                                else FontWeight.Medium,
                                        color = OneMessageColors.TextPrimary
                                )
                                Text(
                                        text = conversation.timestamp,
                                        style = MaterialTheme.typography.bodySmall,
                                        color =
                                                if (conversation.unreadCount > 0)
                                                        OneMessageColors.White
                                                else OneMessageColors.TextMuted
                                )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = conversation.lastMessage,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color =
                                                if (conversation.unreadCount > 0)
                                                        OneMessageColors.TextSecondary
                                                else OneMessageColors.TextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                )

                                // Unread badge
                                if (conversation.unreadCount > 0) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                                modifier =
                                                        Modifier.size(24.dp)
                                                                .background(
                                                                        OneMessageColors.White,
                                                                        CircleShape
                                                                ),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Text(
                                                        text = conversation.unreadCount.toString(),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = OneMessageColors.Black
                                                )
                                        }
                                }
                        }
                }
        }

        // Divider
        HorizontalDivider(
                modifier = Modifier.padding(start = 88.dp),
                color = OneMessageColors.GlassBorder,
                thickness = 0.5.dp
        )
}
