package com.klavs.e_commerceapp.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    companion object {
        fun getClient(): HttpClient {
            return HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
    }
}