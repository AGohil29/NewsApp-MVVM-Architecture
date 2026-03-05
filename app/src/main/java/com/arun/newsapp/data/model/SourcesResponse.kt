package com.arun.newsapp.data.model


import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("sources")
    val sources: List<SourceData>,
    @SerializedName("status")
    val status: String
)

data class SourceData(
    @SerializedName("category")
    val category: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)