package com.example.helpers

import org.ktorm.database.Database

object ConnectionDataBase {

    val database = Database.connect(


        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "Gamal@@2172001"
    )

}