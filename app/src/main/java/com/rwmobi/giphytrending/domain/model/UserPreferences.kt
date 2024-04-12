package com.rwmobi.giphytrending.domain.model

data class UserPreferences(
    val apiRequestLimit: Int?,
    val rating: Rating?,
) {
    fun isFullyConfigured(): Boolean {
        return apiRequestLimit != null && rating != null
    }
}
