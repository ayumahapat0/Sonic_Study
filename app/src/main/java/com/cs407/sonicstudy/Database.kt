package com.cs407.sonicstudy

class DataModels {

    data class CreateTableRequest(
        val tableName: String,
        val columns: List<String>,
        val primaryKey: String
    )

    data class InsertDataRequest(
        val tableName: String,
        val question: String,
        val answer: String
    )

    data class UpdateDataRequest(
        val tableName: String,
        val question: String,
        val answer: String,
        val condition: String
    )

    data class DeleteDataRequest(
        val tableName: String,
        val condition: String
    )

    data class RetrieveDataRequest(
        val tableName: String
    )

    data class RetrieveDataResponse(
        val data: List<Map<String, Any>>
    )

    data class DeleteTableRequest(
        val tableName: String
    )



    data class ApiResponse(
        val message: String,
        val error: String?
    )
}