package com.salary.shopmaker.model

data class Product(
    val code: String,
    val name: String,
    val description: String?,
    val manufacturer: String,
    val price: Price?,
    val discountPrice: Price?,
    val rating: Double?,
    val subtitle: String,
    val showcases: List<Showcase>?,
    val listingImage: Image?
)

data class Price(
    val value: String
)

data class Showcase(
    val discountPercent: Int
)

data class Image(
    val url: String?
)

data class ApiResponse(
    val pagination: Pagination,
    val results: List<Product>
)

data class Pagination(
    val totalPages: Int
)