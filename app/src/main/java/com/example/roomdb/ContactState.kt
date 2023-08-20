package com.example.roomdb

data class ContactState(
    val contact: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName : String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
)
