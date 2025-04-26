package com.example.frontend.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class ImageRepository {

    // Upload hình lên server
    suspend fun uploadImageToServer(file: File): String? {
        val client = OkHttpClient()

        val mediaType = "image/*".toMediaTypeOrNull()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", file.name, file.asRequestBody(mediaType))
            .build()

        val request = Request.Builder()
            .url("https://10.0.2.2:3000/api/images/upload")
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                val json = JSONObject(body ?: "")
                json.getString("url") // Backend trả về "url", không phải "imageUrl"
            } else {
                null
            }
        }
    }

    // Lấy URL hình từ server dựa trên imageId
    suspend fun getImageUrl(imageId: String): String? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://10.0.2.2:3000/api/images/$imageId")
            .get()
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                val json = JSONObject(body ?: "")
                json.getString("url") // Backend trả về "url"
            } else {
                null
            }
        }
    }
}