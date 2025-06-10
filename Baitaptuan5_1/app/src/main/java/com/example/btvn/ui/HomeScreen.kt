package com.example.btvn.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.btvn.navigation.Screen
import com.example.btvn.viewmodel.AuthViewModel

/**
 * Màn hình chính sau khi đăng nhập thành công.
 * Hiển thị thông tin chào mừng và các thao tác điều hướng.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tiêu đề chào mừng
        Text(
            text = "Welcome to SmartTasks!",
            style = MaterialTheme.typography.headlineMedium
        )

        // Mô tả nhiệm vụ
        Text(
            text = "Your tasks will appear here.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Nút điều hướng tới màn hình hồ sơ
        Button(
            onClick = {
                navController.navigate(Screen.ProfileScreen.route)
            },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text("Go to Profile")
        }

        // Nút đăng xuất
        Button(
            onClick = {
                authViewModel.signOut()
                navController.navigate(Screen.LoginScreen.route) {
                    // Xoá toàn bộ back stack để tránh quay lại Home
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Sign Out")
        }
    }
}
