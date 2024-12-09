package com.cs407.sonicstudy
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class Database {
    object ConnectionToDB {
        private const val DATABASE_NAME = "awsdatabase"
        private const val HOST = "awsdatabase.cxo2wiu4udmp.us-east-2.rds.amazonaws.com"
        private const val PORT = 3306
        private const val USERNAME = "admin"
        private const val PASSWORD = "ui3cBo6mRwcaXDEMo8OD"

        fun connectDatabase(): Connection? {
            val URL = "jdbc:mysql://$HOST:$PORT/$DATABASE_NAME"
            return DriverManager.getConnection(URL, USERNAME, PASSWORD)
        }
    }

    // TODO: Create a table with following parameters: CREATE TABLE table_name (
    //    id INT AUTO_INCREMENT PRIMARY KEY,
    //    question TEXT NOT NULL,
    //    answer TEXT NOT NULL,
    //    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    // TODO: Add a success message if the table is created successfully
    //);
    fun createTable(tableName: String, columns: List<String>, primaryKey: String) {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val columnDefinitions = columns.joinToString(", ")
        val query = "CREATE TABLE IF NOT EXISTS $tableName ($columnDefinitions, PRIMARY KEY ($primaryKey))"
        println(query)

        statement?.executeUpdate(query)
        statement?.close()
        connection?.close()
    }

    fun insertData(tableName: String, question: String, answer: String) {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val query = "INSERT INTO $tableName (question, answer) VALUES ('$question', '$answer')"
        statement?.executeUpdate(query)
        statement?.close()
        connection?.close()
    }

    fun deleteData(tableName: String, condition: String) {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val query = "DELETE FROM $tableName WHERE $condition"
    }

    fun updateData(tableName: String, question: String, answer: String, condition: String) {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val query = "UPDATE $tableName SET question = '$question', answer = '$answer' WHERE $condition"
        println("Table created successfully")
        statement?.executeUpdate(query)
        statement?.close()
        connection?.close()
    }

    fun retrieveData(tableName: String, condition: String): List<Map<String, Any>> {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val query = "SELECT * FROM $tableName WHERE $condition"

        val resultSet = statement?.executeQuery(query)
        val columns = resultSet?.metaData?.columnCount ?: 0

        val data = mutableListOf<Map<String, Any>>()
        while (resultSet?.next() == true) {
            val row = mutableMapOf<String, Any>()

            for (i in 1..columns) {
                val columnName = resultSet.metaData.getColumnName(i)
                val columnValue = resultSet.getObject(columnName)
                row[columnName] = columnValue
            }
            data.add(row)
        }
        resultSet?.close()
        statement?.close()
        connection?.close()
        return data
    }

    fun deleteTable(tableName: String) {
        val connection = ConnectionToDB.connectDatabase()
        val statement = connection?.createStatement()
        val query = "DROP TABLE IF EXISTS $tableName"
        statement?.executeUpdate(query)
        statement?.close()
        connection?.close()
    }
}