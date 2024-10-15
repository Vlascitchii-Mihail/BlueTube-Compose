package com.appelier.bluetubecompose.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class CustomNavTypeSerializer<T: Parcelable>(
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>
): NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz) as T
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun put(bundle: Bundle, key: String, value: T) {
        return bundle.putParcelable(key, value)
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(serializer, Uri.decode(value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(serializer, value))
    }

    override val name: String = clazz.name
}