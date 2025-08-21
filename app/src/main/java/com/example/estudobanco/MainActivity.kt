package com.example.estudobanco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "usuarios"
        ).build()
        enableEdgeToEdge()
        setContent {
            MyApp(db)
        }
    }
}

class SharedViewModel : ViewModel() {
    var selectedUser by mutableStateOf<User?>(null)
}

@Composable
fun MyApp(db: AppDatabase) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            Greeting(db,navController,sharedViewModel)
        }
        composable("second") {
            Greeting2(db,navController,sharedViewModel)
        }
    }
}

@Composable
fun Greeting(db : AppDatabase,navController : NavController,sharedViewModel: SharedViewModel,modifier: Modifier = Modifier) {
    sharedViewModel.selectedUser = null
    Column {
        Text(
            text = "List of Users:",
            modifier = modifier.padding(24.dp)
        )
        List(db,navController,sharedViewModel)
        Button(onClick = { navController.navigate("second") }) {
            Text("Go to User Form")
        }
    }
}