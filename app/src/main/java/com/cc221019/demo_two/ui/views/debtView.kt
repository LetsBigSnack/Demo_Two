package com.cc221019.demo_two.ui.views

import android.view.View
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.PopupProperties
import com.cc221019.demo_two.R
import com.cc221019.demo_two.data.Debt
import com.cc221019.demo_two.data.Person
import com.cc221019.demo_two.ui.views.model.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editDebtModal(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val context= LocalContext.current;

    if(state.value.openDebtEditDialog){
        var name by rememberSaveable { mutableStateOf(state.value.editDebt.name) }
        var reason by rememberSaveable { mutableStateOf(state.value.editDebt.reason) }
        var amount by rememberSaveable { mutableStateOf(state.value.editDebt.amount) }

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
                        value = name,
                        onValueChange = { newText -> name = newText },
                        label = { Text(text = "Name" ) }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = reason,
                        onValueChange = { newText -> reason = newText },
                        label = { Text(text = "Reason") }
                    )
                    NumberInputField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = amount.toString(),
                        onValueChange = { amount = it.toFloat() },
                        label = { Text("Enter Amount") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if(name.isNullOrEmpty() || amount.toString().isNullOrEmpty() || reason.isNullOrEmpty()){
                            val text = "Please fill out all options"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context , text, duration) // in Activity
                            toast.show()
                        }else{
                            mainViewModel.saveDebt(
                                Debt(
                                    name,
                                    amount,
                                    reason,
                                    state.value.editDebt.personID,
                                    state.value.editDebt.lendingDate,
                                    state.value.editDebt.id
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
fun createDebtModal(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val context= LocalContext.current;
    if(state.value.openDebtCreateDialog){

        var name by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
            TextFieldValue("")
        ) }
        var reason by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(
            TextFieldValue("")
        ) }
        var expanded by remember { mutableStateOf(false) }
        var person by remember { mutableStateOf<Person?>(null) }
        var people = state.value.people
        var amount by remember { mutableStateOf("") }

        // https://developer.android.com/jetpack/compose/components/dialog
        AlertDialog(
            onDismissRequest = {
                mainViewModel.dismissDialog()
            },



            text = {
                Column {
                    Text(text = "Add Debt", fontWeight = FontWeight.Bold)
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose

                    println(people)
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    Box (
                    ){
                        Button(onClick = { expanded = true }) {
                            if(person != null){
                                Text(text = "${person!!.firstName} ${person!!.lastName}")
                            }else{
                                Text(text = "Select Person")
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            properties = PopupProperties(focusable = true)
                        ) {
                            people.forEach { obj ->
                                DropdownMenuItem(
                                    onClick = {
                                        person = obj
                                        expanded = false
                                    }
                                ) {
                                    Text(text = "${obj.firstName} ${obj.lastName}")
                                }
                            }


                        }
                    }
                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = name,
                        onValueChange = { newText -> name = newText },
                        label = { Text(text = "Name") }
                    )

                    NumberInputField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Enter Amount") }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = reason,
                        onValueChange = { newText -> reason = newText },
                        label = { Text(text = "Reason") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if(amount.isNullOrEmpty() || person == null || name.text.isNullOrEmpty() || reason.text.isNullOrEmpty()){
                            val text = "Please fill out all options"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context , text, duration) // in Activity
                            toast.show()
                        }else{
                            mainViewModel.createDebt(
                                Debt(
                                    name.text,
                                    amount.toFloat(),
                                    reason.text,
                                    person!!.id,
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
fun displayDebts(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()

    // https://developer.android.com/jetpack/compose/lists
    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item{
            Text(
                text = "Debts",
                fontWeight = FontWeight.Bold
            )
        }

        items(state.value.debts){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { mainViewModel.editDebt(it) }
            ){
                Column (modifier = Modifier.weight(1f)) {
                    Text(text = "${it.name}", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Reason: ${it.reason}")
                    Text(text = "Amount: ${it.amount} €")
                    Text(text = "Date: ${it.lendingDate}", fontSize = 14.sp, fontWeight = FontWeight.Light)
                }
                IconButton(onClick = { mainViewModel.debtDelete(it)}) {
                    Icon(Icons.Default.Delete,"Delete")
                }
            }
        }
    }
    Column {
        editDebtModal(mainViewModel)
    }
    Column {
        createDebtModal(mainViewModel)
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
    {
        IconButton(onClick = { mainViewModel.openCreateDebt() }, modifier = Modifier
            .size(50.dp)
        ) {
            Icon(
                Icons.Default.AddCircle,"Add",modifier = Modifier
                .size(50.dp)
            )
        }
    }
}


