package com.example.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao: ContactDao,
) : ViewModel() {
    private val _contact = dao.getContactsOrderedByFirstName().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(), emptyList()
    )
    private val _state = MutableStateFlow(ContactState())

    val state = combine(_state,_contact){state,contact ->
        state.copy(
            contact = contact,
        )
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(), ContactState())
    fun onEvent(event: ContactEvent){
        when(event){
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                dao.deleteContact(event.contact)
                }
            }
            is ContactEvent.HideDialog -> {
                _state.update {it.copy(
                 isAddingContact = false
                )}
            }
            is ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return
                }
                val contact = Contact(firstName,lastName,phoneNumber)
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update { it.copy(
                    isAddingContact = false,
                    firstName = "",
                    lastName = "",
                    phoneNumber = ""
                ) }
            }
            is ContactEvent.SetFirstName -> {
                _state.update {it.copy(
                  firstName = event.firstName
                ) }
            }
            is ContactEvent.SetLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update { it.copy(
                    phoneNumber = event.phoneNumber
                ) }
            }
            is ContactEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingContact = true
                ) }
            }
        }
    }
}