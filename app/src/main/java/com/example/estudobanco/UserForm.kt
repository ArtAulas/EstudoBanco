package com.example.estudobanco

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Greeting2(name: String,navController : NavController, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp)
        )
        Button(onClick = { navController.navigate("main") }) {
            Text("Go to First Screen")
        }
        Form()
    }
}

@Composable
fun Form(modifier: Modifier = Modifier){
    val textState = remember { mutableStateOf("") }
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text("Enter a value") }
    )
}