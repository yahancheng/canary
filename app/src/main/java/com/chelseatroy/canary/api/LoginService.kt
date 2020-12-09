package com.chelseatroy.canary.api

import retrofit2.http.GET

interface LoginService {
    @GET("/v3/73a49435-02fe-42d1-a53c-643d6a3b78ac")
    suspend fun authenticate(): CanarySession
}