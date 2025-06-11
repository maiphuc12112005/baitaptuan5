package com.example.baith2.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Import sp for font sizes
import coil.compose.rememberAsyncImagePainter
import com.example.baith2.data.model.Product
import com.example.baith2.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductScreen() {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                // Giả định API trả về một sản phẩm duy nhất
                // Nếu API trả về danh sách, bạn cần điều chỉnh để lấy sản phẩm đầu tiên hoặc theo ID
                product = RetrofitClient.api.getProduct()
            } catch (e: Exception) {
                Log.e("API", "Lỗi gọi API", e)
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        product?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White) // Đảm bảo nền trắng toàn bộ màn hình
                    .padding(horizontal = 16.dp), // Padding ngang cho toàn bộ nội dung
                horizontalAlignment = Alignment.Start
            ) {
                // Không có Spacer từ TopAppBar vì đã bỏ TopAppBar

                // Thẻ chứa hình ảnh
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp) // Chiều cao cố định cho ảnh lớn hơn
                        .padding(top = 16.dp), // Khoảng cách từ mép trên
                    shape = RoundedCornerShape(12.dp),
                    // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Thêm bóng đổ nếu cần
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(it.imgURL),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize() // Ảnh lấp đầy toàn bộ Card
                            .clip(RoundedCornerShape(12.dp)), // Clip ảnh để bo góc
                        contentScale = ContentScale.Crop // Đảm bảo ảnh được cắt để vừa vặn
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa ảnh và tên sản phẩm

                Text(
                    it.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp, // Tùy chỉnh kích thước font để khớp
                        fontWeight = FontWeight.Normal, // Tùy chỉnh độ đậm
                        lineHeight = 28.sp // Tùy chỉnh khoảng cách dòng nếu tên dài
                    )
                )

                Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa tên và giá

                val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                // Loại bỏ ký hiệu tiền tệ mặc định và thêm "đ" sau số
                val formattedPrice = format.format(it.price).replace("₫", "") + "₫"

                Text(
                    "Giá: $formattedPrice",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.error, // Màu đỏ
                        fontWeight = FontWeight.Bold, // In đậm
                        fontSize = 22.sp // Kích thước font lớn hơn cho giá
                    )
                )

                Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa giá và mô tả

                // Thẻ chứa mô tả
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp), // Bo góc nhẹ cho card mô tả
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)) // Màu nền xám nhạt như ảnh
                    // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Thêm bóng đổ nếu cần
                ) {
                    Text(
                        it.des,
                        modifier = Modifier.padding(16.dp), // Padding bên trong Card mô tả
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp // Điều chỉnh khoảng cách dòng cho mô tả
                        )
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có dữ liệu.")
        }
    }
}