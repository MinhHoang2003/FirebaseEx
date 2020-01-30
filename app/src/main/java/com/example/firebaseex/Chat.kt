package com.example.firebaseex

data class Chat(
    var contents: HashMap<String, Content>? = null,
    var participants: ArrayList<String>? = null
)