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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

@Composable
fun MyApp(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            Greeting(db,navController)
        }
        composable("second") {
            Greeting2("Arthur",navController)
        }
    }
}

@Composable
fun Greeting(db : AppDatabase,navController : NavController,modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "List of Users:",
            modifier = modifier.padding(24.dp)
        )
        List(db)
        Button(onClick = { navController.navigate("second") }) {
            Text("Go to Second Screen")
        }
    }
}