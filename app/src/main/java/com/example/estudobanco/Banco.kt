package com.example.estudobanco

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Preview(showSystemUi = true)
@Composable
fun List1(modifier: Modifier = Modifier) {
    val lettersArray = arrayOf("Art","Pier","Banana","Joker")
    Column(modifier=modifier
                    .padding(24.dp)
    ){
        for (letter in lettersArray){
            Row(modifier = modifier
                .padding(24.dp)
            ) {
                Text(
                    text = "1:",
                    modifier=modifier.padding(end = 24.dp)
                )
                Text(
                    text = letter
                )
                Text(
                    text = "\n X"
                )
            }
        }
    }
}

@Composable
fun List(db : AppDatabase,modifier: Modifier = Modifier) {
    val usersFlow = db.userDao().getAll()
    val users by usersFlow.collectAsState(initial = emptyList())
    if (users.isEmpty()){
      Text(
          text="Não há usuários cadastrados",
          modifier=modifier.padding(24.dp)
      )
    }else{
        Column(modifier=modifier
            .padding(24.dp)
        ){
            for (user in users){
                Row(modifier = modifier
                    .padding(24.dp)
                ) {
                    Text(
                        text = "${user.id}:",
                        modifier=modifier.padding(end = 24.dp)
                    )
                    Text(
                        text = user.firstName.toString()
                    )
                    Text(
                        text = " ${user.lastName}"
                    )
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

    @Insert
    fun insertAll(vararg user:User)

    @Delete
    fun delete(user:User)
}

@Database(entities=[User::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}