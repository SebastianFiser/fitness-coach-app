package com.sebastianfiser.fitnesscoach.models

import android.content.Context
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.*
import io.appwrite.services.*

object Appwrite {
    lateinit var client: Client
    lateinit var account: Account

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject("6a1b511e003e2af7aa7c")

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

    suspend fun deleteAccount() {
        account.delete()
    }
}