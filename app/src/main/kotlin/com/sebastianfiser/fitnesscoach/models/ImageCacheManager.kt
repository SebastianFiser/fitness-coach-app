package com.sebastianfiser.fitnesscoach.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageCacheManager(private val context: Context) {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val memoryCache = object : LruCache<String, Bitmap>(maxMemory / 8) {
        override fun sizeOf(key: String, bitmap: Bitmap) = bitmap.byteCount / 1024
    }

    fun getFromMemory(key: String): Bitmap? = memoryCache.get(key)
    fun saveToMemory(key: String, bitmap: Bitmap) { memoryCache.put(key, bitmap) }

    suspend fun getFromDisk(key: String): Bitmap? = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "$key.png")
        if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }

    suspend fun saveToDisk(key: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "$key.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}
