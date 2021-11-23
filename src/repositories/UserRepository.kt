package com.example.repositories

import com.example.data.model.User
import com.example.data.table.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*


class UserRepository(val db:Database) {

    suspend fun register(user:User) = withContext(Dispatchers.IO){
        val result=db.insert(UserEntity) {
            set(it.username, user.username)
            set(it.email, user.email)
            set(it.haspassord, user.hashedPassword())
            set(it.image, user.image)
        }
        result
    }

    suspend fun findUserByEmail(email:String)=withContext(Dispatchers.IO){
        // this fun check if user email exist or not and if exists return user info
        val user = db.from(UserEntity)
            .select()
            .where {
                UserEntity.email eq email
            }.map {
                rowToUser(it)
            }.firstOrNull()

        user
    }

    suspend fun findUserById(userId:Int)=withContext(Dispatchers.IO){
        // this fun check if user email exist or not and if exists return user info
        val user = db.from(UserEntity)
            .select()
            .where {
                UserEntity.userId eq userId
            }.map {
                rowToUser(it)
            }.firstOrNull()

        user
    }

    private fun rowToUser(row:QueryRowSet?):User?{
        return if (row==null){
             null
        }else{
            val id = row[UserEntity.userId]?:-1
            val email = row[UserEntity.email]?:""
            val username = row[UserEntity.username] ?:""
            val image = row[UserEntity.image] ?:""
            val haspassord = row[UserEntity.haspassord] ?:""
            User(id, username, email, image,haspassord)
        }
    }
}