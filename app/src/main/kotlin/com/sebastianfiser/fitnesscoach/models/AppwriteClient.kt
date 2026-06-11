package com.sebastianfiser.fitnesscoach.models

import android.content.Context
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.*
import io.appwrite.services.*
import com.sebastianfiser.fitnesscoach.BuildConfig

object Appwrite {
    lateinit var client: Client
    lateinit var account: Account

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(BuildConfig.APPWRITE_ENDPOINT)
            .setProject(BuildConfig.APPWRITE_PROJECT_ID)
        account = Account(client)
    }

    suspend fun onLogin(
        email: String,
        password: String,
    ): Session {
        return account.createEmailPasswordSession(
            email,
            password,
        )
    }

    suspend fun onRegister(
        email: String,
        password: String,
        name: String? = null,
    ): User<Map<String, Any>> {
        return account.create(
            userId = ID.unique(),
            email,
            password,
            name = name
        )
    }

    suspend fun onLogout() {
        account.deleteSession("current")
    }

    suspend fun onCheckSession() : Boolean {
        return try {
            account.get()
            true
        } catch (e: Throwable) {
            false
        }
    }

    suspend fun ParseErrorMsg(errorMsg: String): String {
        return when {
            errorMsg.contains("Invalid email or password") -> "Invalid email or password"
            errorMsg.contains("User already exists") -> "User with this email already exists"
            else -> "Unknown error: $errorMsg"
        }
    }

    suspend fun getCurrentUser(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: Throwable) {
            null
        }
    }
}