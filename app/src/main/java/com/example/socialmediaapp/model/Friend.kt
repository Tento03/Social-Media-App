package com.example.socialmediaapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(var uid:String, var fullname:String, var username:String,
                  var image:String, var bio:String) : Parcelable {
    constructor():this("","","","","")}
