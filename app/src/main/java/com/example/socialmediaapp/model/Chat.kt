package com.example.socialmediaapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L  // Menyimpan waktu dalam bentuk epoch time
) : Parcelable