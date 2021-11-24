package com.example.helpers

import org.ktorm.database.Database

object ConnectionDataBase {
   const val serverName="MYSQL5037.site4now.net"
    const val dataBaseName="db_a7ce11_noteapp"
    val database = Database.connect(
//        url = "jdbc:mysql://${serverName}/${dataBaseName}",
//        driver = "com.mysql.cj.jdbc.Driver",
//        user = "a7ce11_noteapp",
//        password = "Gamal2172001"

        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "Gamal@@2172001"
    )

}