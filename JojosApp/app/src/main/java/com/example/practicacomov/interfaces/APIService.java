package com.example.practicacomov.interfaces;

import com.example.practicacomov.models.Message;
import com.example.practicacomov.models.ModifyPetition;
import com.example.practicacomov.models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonPrimitive> login(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("message")
    Call<JsonObject> sendMessage(@Body Message message, @Header ("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @POST("user")
    Call<JsonObject> register(@Body User newUser);

    @Headers("Content-Type: application/json")
    @GET("message/{username}/{lastMessageDate}")
    Call<JsonObject> getMessages(@Path("username") String username, @Path("lastMessageDate") String lastMessageReceived, @Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET("messagefromto/{from}/{to}")
    Call<JsonObject> getMessagesFromTo(@Path("from") String username, @Path("to") String lastMessageReceived, @Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @GET("userbyname/{user}")
    Call<JsonObject> getUserByName(@Path("user") String username, @Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @POST("modifyuser")
    Call<JsonObject> modifyUser(@Body ModifyPetition petition, @Header("Authorization") String authToken);

    @Headers("Content-Type: application/json")
    @DELETE("user/{name}")
    Call<JsonObject> deleteUser(@Path("name") String name, @Header("Authorization") String authToken);
}
