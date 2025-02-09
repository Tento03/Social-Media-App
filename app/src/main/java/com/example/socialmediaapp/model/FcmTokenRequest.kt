package com.example.socialmediaapp.model

data class FcmTokenRequest(
    val user_id: String,
    val fcm_token: String
)