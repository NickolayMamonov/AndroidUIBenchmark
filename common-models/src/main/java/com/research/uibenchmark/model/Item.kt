package com.research.uibenchmark.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("timestamp") val timestamp: Long
)
