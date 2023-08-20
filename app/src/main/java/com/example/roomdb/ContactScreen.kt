package com.example.roomdb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.ShowDialog)
            }){
               Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Contact"
               )
            }
        }
    )
    {padding->
        if(state.isAddingContact) {
            AddContactDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item { 
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ){
                    Text(
                        text = "Contacts Sorted By First Name",
                        fontSize = 24.sp
                        )
                }
            }
            items(state.contact){contact->
                Row (
                    modifier = Modifier
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${contact.firstname} ${contact.lastname}",
                            fontSize = 20.sp
                        )
                        Text(text = contact.phoneNumber, fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        onEvent(ContactEvent.DeleteContact(contact)) })
                    {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Contact")
                    }
                }
            }
        }
    }
}