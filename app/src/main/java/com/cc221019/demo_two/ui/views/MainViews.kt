package com.cc221019.demo_two.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cc221019.demo_two.data.BccStudent
import com.cc221019.demo_two.model.MainViewModel
import com.cc221019.demo_two.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(mainViewModel: MainViewModel){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {BottomNavigationBar(navController)}
    ) {
        NavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            startDestination = "first"
        ){
            composable("first"){ mainScreen(mainViewModel) }
            composable("second"){ displayValues(mainViewModel) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController){
    BottomNavigation (backgroundColor = MaterialTheme.colorScheme.primary) {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("first") },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "") })

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("second") },
            icon = { Icon(imageVector = Icons.Default.AccountBox, contentDescription = "") })

    }
}

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(mainViewModel: MainViewModel){
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var uid by remember { mutableStateOf(TextFieldValue("")) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "StateFlow", fontSize = 50.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Box"
        )
        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = name,
            onValueChange = {
                    newText -> name = newText
            },
            label = { Text(text = "Name" ) }
        )

        TextField(
            modifier = Modifier.padding(top = 20.dp),
            value = uid,
            onValueChange = {
                    newText -> uid = newText
            },
            label = { Text(text = "UID") }
        )

        Button(
            onClick = { mainViewModel.save(BccStudent(name.text,uid.text)) },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Send", fontSize = 20.sp)
        }
    }
}

@Composable
fun displayValues(mainViewModel: MainViewModel){
    val student = mainViewModel.bccStudentState.collectAsState()
    var name by rememberSaveable { mutableStateOf(student.value.name) }
    var uid by rememberSaveable { mutableStateOf(student.value.uid) }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "BCC Student:",fontWeight = FontWeight.Bold)
        Text(text = "Name: $name")
        Text(text = "UID: $uid")
    }
}

