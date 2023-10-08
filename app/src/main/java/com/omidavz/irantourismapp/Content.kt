package com.omidavz.irantourismapp
import com.google.firebase.Timestamp
data class Content(
    val title : String ,
    val description : String ,
    val imageUrl : String ,

    val userId : String,
    val timeAdded : Timestamp,
    val username : String
)
