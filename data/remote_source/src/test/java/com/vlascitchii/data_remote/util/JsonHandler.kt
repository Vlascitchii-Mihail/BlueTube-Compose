package com.vlascitchii.data_remote.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.io.use
import kotlin.jvm.javaClass

class JsonHandler() {

    fun readJsonAsStringFromPath(jsonFilePath: String): String {
        val inputStream = checkNotNull(
            value = javaClass.classLoader?.getResourceAsStream(jsonFilePath),
            lazyMessage = { "File not found: $jsonFilePath" }
        )

        return inputStream.bufferedReader().use { it.readText() }
    }
}


data class User(
    val id: Int,
    val isActive: Boolean
)

class UserJsonAdapter {
    @FromJson
    fun fromJson(json: UserJson): User {
        return User(
            id = json.id.toIntOrNull() ?: 0,
            isActive = json.is_active == "yes"
        )
    }

    @ToJson
    fun toJson(user: User): UserJson {
        return UserJson(
            id = user.id.toString(),
            is_active = if (user.isActive) "yes" else "no"
        )
    }

    // Helper class for raw JSON shape
    data class UserJson(
        val id: String,
        val is_active: String
    )
}