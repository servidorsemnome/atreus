package com.doceazedo.atreus.gatekeeper

import com.doceazedo.atreus.Atreus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

data class GrantResponse(
    val id: Int,
    val nickname: String,
    val ip: String,
    val authorized: Boolean,
    val expirationDate: String,
)

data class FlowRequest(
    val nickname: String,
    val ip: String,
)

data class FlowResponse(
    val code: String,
    val nickname: String,
    val ip: String,
    val grantId: Int?,
    val expirationDate: String,
)

val baseURL: String = Atreus.instance!!.config.getString("gatekeeper.baseURL") + "/api"
val token: String = Atreus.instance!!.config.getString("gatekeeper.token")

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            setLenient()
        }
    }
    defaultRequest {
        header("Authorization", "Bearer $token")
    }
}

object GatekeeperClient {
    suspend fun getGrant(nickname: String, ip: String): GrantResponse? {
        val resp = client.get("$baseURL/grant") {
            url {
                parameters.append("nickname", nickname)
                parameters.append("ip", ip)
            }
        }
        Atreus.instance!!.logger.info("/grant status code: ${resp.status}")
        Atreus.instance!!.logger.info(resp.bodyAsText())
        return if (resp.bodyAsText() == "null") null else resp.body()
    }

    suspend fun createFlow(flowRequest: FlowRequest): FlowResponse? {
        val resp = client.post("$baseURL/flow") {
            contentType(ContentType.Application.Json)
            setBody(flowRequest)
        }
        Atreus.instance!!.logger.info("/flow status code: ${resp.status}")
        Atreus.instance!!.logger.info(resp.bodyAsText())
        return if (resp.bodyAsText() == "null") null else resp.body()
    }
    suspend fun getFlow(code: String): FlowResponse? {
        return try {
            val resp = client.get("$baseURL/flow/$code")
            Atreus.instance!!.logger.info("/flow status code: ${resp.status}")
            Atreus.instance!!.logger.info(resp.bodyAsText())
            if (resp.bodyAsText() == "null") null else resp.body()
        } catch (cause: Throwable) {
            null
        }
    }

    fun close() { client.close() }
}