package com.research.uibenchmark.plugins

import com.research.uibenchmark.routes.configureUIRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("SDUI API is running!")
        }

        configureUIRoutes()
    }
}