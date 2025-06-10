package com.example.btvn.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.btvn.R
import com.example.btvn.navigation.Screen
import com.example.btvn.viewmodel.AuthViewModel
import com.example.btvn.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentUserData by authViewModel.currentUserData.collectAsState()
    val userProfile by userViewModel.userProfile.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val updateSuccess by userViewModel.updateSuccess.collectAsState()
    val errorMessage by userViewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    // Load profile khi user được xác thực
    LaunchedEffect(currentUserData) {
        currentUserData?.let { user ->
            userViewModel.loadUserProfile(user.uid)
        }
    }

    // Gán dữ liệu vào UI
    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.name
            email = it.email
            dateOfBirth = it.dateOfBirth
        }
    }

    // Toast thông báo khi cập nhật thành công hoặc thất bại
    LaunchedEffect(updateSuccess) {
        updateSuccess?.let {
            val message = if (it) "Profile updated successfully!" else "Failed to update profile."
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            userViewModel.resetUpdateSuccessState()
        }
    }

    // Thông báo lỗi
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            userViewModel.resetErrorState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            authViewModel.signOut()
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading && userProfile == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Ảnh đại diện
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.placeholder_profile,
                            error = painterResource(id = R.drawable.placeholder_profile)
                        ),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Change Profile Picture",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 4.dp)
                            .size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Tên
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email (readonly)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ngày sinh
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = isLoading,
                    trailingIcon = {
                        IconButton(onClick = { /* TODO: Thêm DatePicker */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Nút quay lại đăng nhập
                Button(
                    onClick = {
                        authViewModel.signOut()
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.ProfileScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Back")
                }
            }
        }
    }
}
