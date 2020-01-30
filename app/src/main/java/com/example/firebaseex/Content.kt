package com.example.firebaseex

data class Content(
    var content: String? = null,
    var content_type: String? = null,
    var date: String? = null,
    var from: String? = null,
    var has_send: ArrayList<String>? = null
)