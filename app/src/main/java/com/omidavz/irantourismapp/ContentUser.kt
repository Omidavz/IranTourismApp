package com.omidavz.irantourismapp

import android.app.Application

class ContentUser : Application() {

    var username: String? = null
    var userId: String? = null


    companion object {
        var instance: ContentUser? = null
            get() {
                if (field == null) {
                    field = ContentUser()
                }
                return field
            }
            private set


    }


}