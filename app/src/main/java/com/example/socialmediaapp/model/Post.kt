package com.example.socialmediaapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(var postId:String,val image:String,var publisher:String,var description: String):Parcelable {
    constructor():this("","","","")
}