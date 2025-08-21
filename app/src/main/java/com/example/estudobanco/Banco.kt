package com.example.estudobanco

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun deleteUser(db : AppDatabase,userDelete:User){
    CoroutineScope(Dispatchers.IO).launch {
        db.userDao().delete(userDelete)
    }
}

fun updateUser(userUpdate:User,navController : NavController,sharedViewModel: SharedViewModel){
    sharedViewModel.selectedUser = userUpdate
    navController.navigate("second")
}

@Composable
fun List(db : AppDatabase,navController : NavController,sharedViewModel: SharedViewModel,modifier: Modifier = Modifier) {
    val usersFlow = db.userDao().getAll()
    val users by usersFlow.collectAsState(initial = emptyList())
    if (users.isEmpty()){
      Text(
          text="Não há usuários cadastrados",
          modifier=modifier.padding(vertical=24.dp,horizontal=12.dp)
      )
    }else{
        Column(modifier=modifier
            .padding(vertical=24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            for (user in users){
                Row(modifier = modifier
                    .padding(vertical=24.dp)
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "${user.id}:",
                        modifier=modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = user.firstName.toString()
                    )
                    Text(
                        text = " ${user.lastName}"
                    )
                    Button(onClick = { updateUser(user,navController,sharedViewModel) },
                        modifier=modifier.padding(start = 24.dp)) {
                        Text("^")
                    }
                    Button(onClick = { deleteUser(db,user) },
                        modifier=modifier.padding(horizontal = 12.dp)) {
                    Text("X")
                    }
                }
            }
        }
    }
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id:Int=0,
    @ColumnInfo(name="first_name") val firstName:String?,
    @ColumnInfo(name="last_name") val lastName:String?
)

@Dao
interface UserDao{
    @Query("SELECT * FROM users")
    fun getAll() : Flow<List<User>>

    @Query("SELECT * FROM users WHERE id in (:userIds)")
    fun loadAllByIds(userIds: IntArray) : List<User>

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg user:User)

    @Delete
    fun delete(user:User)
}

@Database(entities=[User::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}