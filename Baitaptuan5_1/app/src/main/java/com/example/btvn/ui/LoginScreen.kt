package com.example.btvn.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.btvn.R
import com.example.btvn.model.AuthResponse // Đảm bảo bạn có AuthResponse class này
import com.example.btvn.navigation.Screen // Đảm bảo bạn có Screen object này
import com.example.btvn.viewmodel.AuthViewModel // Đảm bảo bạn có AuthViewModel này
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel() // Sử dụng hiltViewModel để inject ViewModel
) {
    val context = LocalContext.current
    val authStatus by authViewModel.authStatus.collectAsState() // Thu thập trạng thái xác thực từ ViewModel

    // Cấu hình Google Sign-In
    // Use remember to ensure GSO and client are created once per composition
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient: GoogleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Launcher để mở Intent đăng nhập Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    // Gọi ViewModel để xử lý đăng nhập Firebase với Google ID Token
                    authViewModel.signInWithGoogle(idToken)
                } else {
                    Toast.makeText(context, "Google ID Token is null.", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                // Xử lý lỗi API của Google Sign-In
                Toast.makeText(context, "Google Sign-In failed: ${e.statusCode} - ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            // Xử lý khi người dùng hủy đăng nhập
            Toast.makeText(context, "Google Sign-In cancelled.", Toast.LENGTH_SHORT).show()
        }
    }

    // Theo dõi trạng thái xác thực để điều hướng
    LaunchedEffect(authStatus) {
        when (val status = authStatus) {
            is AuthResponse.Success -> {
                // Điều hướng đến ProfileScreen khi đăng nhập thành công
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.ProfileScreen.route) {
                    // Xóa stack để không thể quay lại màn hình đăng nhập
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            is AuthResponse.Error -> {
                // Hiển thị thông báo lỗi
                Toast.makeText(context, "Login failed: ${status.errorMessage}", Toast.LENGTH_LONG).show()
            }
            AuthResponse.Loading -> {
                // Đang tải, có thể hiển thị một UI loading nếu muốn, hiện tại đã có trong Button
            }
            AuthResponse.Idle -> {
                // Trạng thái ban đầu, không làm gì
            }
        }
    }

    // Giao diện đăng nhập
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo UTH
            Image(
                painter = painterResource(id = R.drawable.uth_logo), // Đảm bảo R.drawable.uth_logo tồn tại
                contentDescription = "UTH Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tiêu đề ứng dụng
            Text(
                text = "SmartTasks",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "A simple and efficient to-do app",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Lời chào
            Text(
                text = "Welcome",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Ready to explore? Log in to get started.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nút đăng nhập Google
            Button(
                onClick = {
                    // Sign out first to always show account picker
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = authStatus !is AuthResponse.Loading // Disable button when loading
            ) {
                if (authStatus is AuthResponse.Loading) {
                    // Show CircularProgressIndicator when loading
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    // Show icon and text when not loading
                    Image(
                        painter = painterResource(id = R.drawable.ic_google), // Ensure R.drawable.ic_google exists
                        contentDescription = "Google Sign-In",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SIGN IN WITH GOOGLE")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Copyright
            Text(
                text = "© UTHSmartTasks",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}