package com.example.estudobanco

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Greeting2(db : AppDatabase,navController : NavController, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello User!\nInsert Data in the Database!",
            modifier = modifier.padding(24.dp)
        )
        Button(onClick = { navController.navigate("main") }) {
            Text("Go Back")
        }
        Form(db,navController=navController)
    }
}

fun registerUser(db : AppDatabase,id: Int, firstName:String, lastName:String){
    val user= User(id = id, firstName = firstName,lastName=lastName)
    db.userDao().insertAll(user)
}

@Composable
fun Form(db : AppDatabase,modifier: Modifier = Modifier,navController : NavController){
    val formID = remember { mutableIntStateOf(0) }
    val formFirstName = remember { mutableStateOf("") }
    val formLastName = remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        TextField(
            value = formID.intValue.toString(),
            onValueChange = { formID.intValue = it.toIntOrNull() ?: 0 },
            label = { Text("Enter ID") },
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier=modifier
                .padding(top=24.dp)
                .fillMaxWidth()
        )
        TextField(
            value = formFirstName.value,
            onValueChange = { formFirstName.value = it },
            label = { Text("Enter First Name") },
            shape = CircleShape,
            modifier=modifier
                .padding(top=24.dp)
                .fillMaxWidth()
        )
        TextField(
            value = formLastName.value,
            onValueChange = { formLastName.value = it },
            label = { Text("Enter Last Name") },
            shape = CircleShape,
            modifier=modifier
                .padding(vertical=24.dp)
                .fillMaxWidth()
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                registerUser(db, formID.intValue, formFirstName.value, formLastName.value)
            }
            navController.navigate("main")
        }) {
            Text("Register User and Go Back")
        }
    }
}