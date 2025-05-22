package com.proyecto.libroschill.data.model

data class Volume(
    val id: String = "",
    val volumeInfo: VolumeInfo = VolumeInfo("", null, "", null)
)
data class VolumeInfo(
    val title: String = "",
    val authors: List<String>? = emptyList(),
    val description: String = "",
    val categories: List<String>? = emptyList(),
    val imageLinks: ImageLinks? = ImageLinks("", ""),
    val industryIdentifiers: List<IndustryIdentifier>? = emptyList()
)
data class ImageLinks(
        val smallThumbnail: String? = "",
        val thumbnail: String = ""
)
data class IndustryIdentifier(
    val type: String,
    val identifier: String
)