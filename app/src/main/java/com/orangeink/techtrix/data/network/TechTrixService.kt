package com.orangeink.techtrix.data.network

import com.orangeink.techtrix.common.SuccessResponse
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.home.data.model.HomeResponse
import com.orangeink.techtrix.login.data.model.Participant
import com.orangeink.techtrix.login.data.model.UpdateParticipant
import com.orangeink.techtrix.misc.data.model.Sponsor
import com.orangeink.techtrix.misc.data.model.Team
import com.orangeink.techtrix.registrations.data.model.Registration
import com.orangeink.techtrix.search.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.*

interface TechTrixService {

    companion object {
        const val ENDPOINT = "https://techtrix22.herokuapp.com/"
    }

    //Participant
    @POST("/participants")
    suspend fun createParticipant(@Body participant: Participant): Response<Participant>

    @GET("/participants/{email}")
    suspend fun getParticipant(@Path("email") email: String): Response<Participant>

    @PUT("/participants/{email}")
    suspend fun updateParticipant(
        @Path("email") email: String,
        @Body participant: UpdateParticipant
    ): Response<SuccessResponse>

    //Home
    @GET("/home")
    suspend fun loadHome(@Query("version_code") code: Int): Response<HomeResponse>

    //Search
    @GET("/search/{query}")
    suspend fun search(@Path("query") query: String): Response<SearchResponse>

    //Category
    @GET("/search/search_category/{category_name}")
    suspend fun eventsByCategory(@Path("category_name") category: String): Response<List<Event>>

    //Event
    @GET("/events/find_by_id/{id}")
    suspend fun eventDetails(@Path("id") eventId: Int): Response<Event>

    @GET("/events")
    suspend fun allEvents(): Response<List<Event>>

    //Sponsor
    @GET("/sponsors")
    suspend fun allSponsors(): Response<List<Sponsor>>

    //Team
    @GET("/teams")
    suspend fun allTeam(): Response<List<Team>>

    //Registrations
    @GET("/registrations/email/{email}")
    suspend fun getRegistrations(@Path("email") email: String): Response<List<Registration>>

    @POST("/registrations")
    suspend fun register(@Body registration: Registration): Response<SuccessResponse>
}