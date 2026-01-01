package com.example.onemessage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onemessage.ui.screens.ChatListScreen
import com.example.onemessage.ui.screens.ChatScreen
import com.example.onemessage.ui.screens.LoginScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ChatList : Screen("chat_list")
    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }
    object Settings : Screen("settings")
    object Contacts : Screen("contacts")
}

@Composable
fun OneMessageNavigation(
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.Login.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        // Login screen
        composable(Screen.Login.route) {
            LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.ChatList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
            )
        }

        // Chat list screen
        composable(Screen.ChatList.route) {
            ChatListScreen(
                    onConversationClick = { conversationId ->
                        navController.navigate(Screen.Chat.createRoute(conversationId))
                    },
                    onNewChatClick = { navController.navigate(Screen.Contacts.route) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Individual chat screen
        composable(Screen.Chat.route) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(
                    conversationId = conversationId,
                    onBackClick = { navController.popBackStack() }
            )
        }

        // Settings screen (placeholder)
        composable(Screen.Settings.route) {
            // TODO: Implement SettingsScreen
        }

        // Contacts screen (placeholder)
        composable(Screen.Contacts.route) {
            // TODO: Implement ContactsScreen
        }
    }
}
