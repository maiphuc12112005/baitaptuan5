package com.example.btvn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btvn.ui.HomeScreen
import com.example.btvn.ui.LoginScreen
import com.example.btvn.ui.ProfileScreen

/**
 * Sealed class đại diện cho tất cả các màn hình trong ứng dụng.
 */
sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object ProfileScreen : Screen("profile_screen")
    object HomeScreen : Screen("home_screen")
}

/**
 * Khởi tạo NavGraph cho ứng dụng sử dụng Jetpack Navigation Compose.
 *
 * @param navController Bộ điều khiển điều hướng.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route // Bắt đầu từ màn hình đăng nhập
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
    }
}
