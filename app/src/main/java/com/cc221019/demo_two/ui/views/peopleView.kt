package com.cc221019.demo_two.ui.views

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cc221019.demo_two.R
import com.cc221019.demo_two.data.Person
import com.cc221019.demo_two.ui.views.model.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editPeopleModal(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val context= LocalContext.current;

    if(state.value.openPersonEditDialog){
        var lastName by rememberSaveable { mutableStateOf(state.value.editPerson.lastName) }
        var firstName by rememberSaveable { mutableStateOf(state.value.editPerson.firstName) }

        // https://developer.android.com/jetpack/compose/components/dialog
        AlertDialog(
            onDismissRequest = {
                mainViewModel.dismissDialog()
            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = firstName,
                        onValueChange = { newText -> firstName = newText },
                        label = { Text(text = "First Name" ) }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = lastName,
                        onValueChange = { newText -> lastName = newText },
                        label = { Text(text = "Last Name") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {

                        if(firstName.isNullOrEmpty() || lastName.isNullOrEmpty()){
                            val text = "Please fill out all options"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context , text, duration) // in Activity
                            toast.show()
                        }else{
                            mainViewModel.savePeople(
                                Person(
                                    firstName,
                                    lastName,
                                    state.value.editPerson.id
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.editmodal_button_save))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createPeopleModal(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val context= LocalContext.current;

    if(state.value.openPersonCreateDialog){

        var lastName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
            TextFieldValue("")
        ) }
        var firstName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
            TextFieldValue("")
        ) }

        // https://developer.android.com/jetpack/compose/components/dialog
        AlertDialog(
            onDismissRequest = {
                mainViewModel.dismissDialog()
            },
            text = {
                Column {
                    Text(text = "Add Person",fontWeight = FontWeight.Bold)
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = firstName,
                        onValueChange = { newText -> firstName = newText },
                        label = { Text(text = "First Name") }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = lastName,
                        onValueChange = { newText -> lastName = newText },
                        label = { Text(text = "Last Name") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if( firstName.text.isNullOrEmpty() || lastName.text.isNullOrEmpty()){
                            val text = "Please fill out all options"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context , text, duration) // in Activity
                            toast.show()
                        }else{
                            mainViewModel.createPerson(
                                Person(
                                    firstName.text,
                                    lastName.text,
                                )
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.editmodal_button_save))
                }
            }
        )
    }
}

@Composable
fun displayPeople(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()

    // https://developer.android.com/jetpack/compose/lists
    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item{
            Text(
                text = "People:",
                fontWeight = FontWeight.Bold
            )
        }

        items(state.value.people){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { mainViewModel.editPeople(it) }
            ){
                Column (modifier = Modifier.weight(1f)) {
                    Text(text = "${it.firstName}" , fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${it.lastName}",fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Total Debt: ${it.debtAmount} €")
                }
                IconButton(onClick = { mainViewModel.peopleDelete(it)}) {
                    Icon(Icons.Default.Delete,"Delete")
                }
            }
        }
    }
    Column {
        editPeopleModal(mainViewModel)
    }
    Column {
        createPeopleModal(mainViewModel)
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
    {
        IconButton(onClick = { mainViewModel.openCreatePerson() }, modifier = Modifier
            .size(50.dp)
        ) {
            Icon(
                Icons.Default.AddCircle,"Add",modifier = Modifier
                .size(50.dp)
            )
        }
    }
}
