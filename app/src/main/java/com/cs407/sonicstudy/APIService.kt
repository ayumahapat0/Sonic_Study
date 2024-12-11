package com.cs407.sonicstudy
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/create_table")
    fun createTable(@Body request: DataModels.CreateTableRequest): Call<DataModels.ApiResponse>

    @POST("/insert_data")
    fun insertData(@Body request: DataModels.InsertDataRequest): Call<DataModels.ApiResponse>

    @POST("/delete_data")
    fun deleteData(@Body request: DataModels.DeleteDataRequest): Call<DataModels.ApiResponse> // For dynamic conditions

    @POST("/update_data")
    fun updateData(@Body request: DataModels.UpdateDataRequest): Call<DataModels.ApiResponse>

    @POST("/retrieve_data")
    fun retrieveData(@Body request: DataModels.RetrieveDataRequest): Call<List<Map<String, Any>>>

    @POST("retrieve_tables")
    fun retrieveTables(): Call<List<String>>

    @POST("/delete_table")
    fun deleteTable(@Body request: DataModels.DeleteTableRequest): Call<DataModels.ApiResponse>
}