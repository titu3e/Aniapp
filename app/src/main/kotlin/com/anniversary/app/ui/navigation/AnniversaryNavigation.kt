package com.anniversary.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anniversary.app.ui.screens.boy.BoySetupScreen
import com.anniversary.app.ui.screens.boy.MessageBuilderScreen
import com.anniversary.app.ui.screens.girl.CodeEntryScreen
import com.anniversary.app.ui.screens.girl.WishDisplayScreen
import com.anniversary.app.ui.screens.shared.RoleSelectionScreen

@Composable
fun AnniversaryNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "role_selection"
    ) {
        composable("role_selection") {
            RoleSelectionScreen(
                onBoySelected = {
                    navController.navigate("boy_setup")
                },
                onGirlSelected = {
                    navController.navigate("code_entry")
                }
            )
        }

        composable("boy_setup") {
            BoySetupScreen(
                onSetupComplete = { anniversaryId ->
                    navController.navigate("message_builder/$anniversaryId") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("message_builder/{anniversaryId}") { backStackEntry ->
            val anniversaryId = backStackEntry.arguments?.getString("anniversaryId") ?: ""
            MessageBuilderScreen(
                anniversaryId = anniversaryId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("code_entry") {
            CodeEntryScreen(
                onCodeVerified = { anniversaryId ->
                    navController.navigate("wish_display/$anniversaryId") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("wish_display/{anniversaryId}") { backStackEntry ->
            val anniversaryId = backStackEntry.arguments?.getString("anniversaryId") ?: ""
            WishDisplayScreen(
                anniversaryId = anniversaryId,
                onBackClick = {
                    navController.navigate("role_selection") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}