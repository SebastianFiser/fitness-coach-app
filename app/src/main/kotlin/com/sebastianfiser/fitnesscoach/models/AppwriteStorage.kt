package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client
import io.appwrite.services.Storage
import android.content.Context
import android.net.Uri
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.models.InputFile
import io.appwrite.models.File


class AppwriteStorage(private val client: Client) {
    private val storage = Storage(client)

    companion object {
        private const val VID_BUCKET_ID = "submission-videos"
        private const val IMG_BUCKET_ID = "6a4f6a49000bb69ef75a"
    }

    suspend fun uploadVideo(context: Context, uri: Uri, userId: String): Result<String> {
        return runCatching {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: throw Exception("Failed to read file")
            val response = storage.createFile(
                bucketId = VID_BUCKET_ID,
                file = InputFile.fromBytes(bytes, "${ID.unique()}.mp4", "video/mp4"),
                fileId = ID.unique(),
                permissions = listOf(
                    Permission.read(Role.user(userId))
                )
            )
            response.id
        }
    }

    suspend fun getVideoBytes(fileId: String): Result<ByteArray> {
        return runCatching {
            storage.getFileView(
                bucketId = VID_BUCKET_ID,
                fileId = fileId
            )
        }
    }

    suspend fun uploadImage(context: Context, uri: Uri, userId: String): Result<String> {
        return runCatching {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: throw Exception("Failed to read file")
            val response = storage.createFile(
                bucketId = IMG_BUCKET_ID,
                file = InputFile.fromBytes(bytes, "${ID.unique()}.jpg", "image/jpeg"),
                fileId = ID.unique(),
                permissions = listOf(
                    Permission.read(Role.user(userId))
                )
            )
            response.id
        }
    }

    suspend fun getImage(fileId: String): Result<ByteArray> {
        return runCatching {
            storage.getFileView(
                bucketId = IMG_BUCKET_ID,
                fileId = fileId
            )
        }
    }

}