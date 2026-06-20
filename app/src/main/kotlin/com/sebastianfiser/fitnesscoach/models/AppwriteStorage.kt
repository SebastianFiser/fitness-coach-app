package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client
import io.appwrite.services.Storage
import android.content.Context
import android.net.Uri
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.models.InputFile

class AppwriteStorage(private val client: Client) {
    private val storage = Storage(client)

    companion object {
        private const val BUCKET_ID = "submission-videos"
    }

    suspend fun uploadVideo(context: Context, uri: Uri, userId: String): Result<String> {
        return runCatching {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: throw Exception("Failed to read file")
            val response = storage.createFile(
                bucketId = BUCKET_ID,
                file = InputFile.fromBytes(bytes, "${ID.unique()}.mp4"),
                fileId = ID.unique(),
                permissions = listOf(
                    Permission.read(Role.user(userId))
                )
            )
            response.id
        }
    }

}