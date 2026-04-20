package org.example.project.models

data class ParameterRow(
    val pn: String,
    val name: String,
    val description: String,
    val unit: String,
    val baseHex: String,
    val basePhys: String,
    val devHex: String,
    val devPhys: String
)