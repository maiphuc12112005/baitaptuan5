package com.example.baith2.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    // Tạo một logging interceptor để in log mạng.
    // Đặt level là BODY để xem tất cả chi tiết. Trong một ứng dụng thực tế,
    // bạn có thể muốn đặt nó thành NONE hoặc BASIC khi phát hành.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Tạo một OkHttpClient tùy chỉnh và thêm logging interceptor vào.
    // Đồng thời, thêm thời gian chờ kết nối và đọc dữ liệu.
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Thêm logger
        .connectTimeout(30, TimeUnit.SECONDS) // Thời gian chờ kết nối
        .readTimeout(30, TimeUnit.SECONDS)    // Thời gian chờ đọc
        .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian chờ ghi
        .build()

    // Sử dụng lazy để tạo instance của Retrofit một lần duy nhất.
    // Nó sẽ sử dụng okHttpClient tùy chỉnh ở trên.
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://mock.apidog.com/")
            .client(okHttpClient) // Sử dụng client đã có logger và timeout
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
