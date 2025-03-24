package com.research.uibenchmark.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.research.uibenchmark.models.Item
import com.research.uibenchmark.services.ItemService

fun Application.configureItemRoutes() {
    routing {
        // Получение всех элементов
        get("/items") {
            call.respond(ItemService.getAllItems())
        }
        
        // Получение элемента по ID
        get("/items/{id}") {
            val id = call.parameters["id"]?.toLong()
                ?: return@get call.respondText("Некорректный ID")
                
            val item = ItemService.getItemById(id)
            if (item != null) {
                call.respond(item)
            } else {
                call.respondText("Элемент с ID $id не найден")
            }
        }
        
        // Создание нового элемента
        post("/items") {
            val item = call.receive<Item>()
            val createdItem = ItemService.createItem(item)
            call.respond(createdItem)
        }
        
        // Обновление элемента
        put("/items/{id}") {
            val id = call.parameters["id"]?.toLong()
                ?: return@put call.respondText("Некорректный ID")
                
            val item = call.receive<Item>()
            val updatedItem = ItemService.updateItem(id, item)
            
            if (updatedItem != null) {
                call.respond(updatedItem)
            } else {
                call.respondText("Элемент с ID $id не найден")
            }
        }
        
        // Удаление элемента
        delete("/items/{id}") {
            val id = call.parameters["id"]?.toLong()
                ?: return@delete call.respondText("Некорректный ID")
                
            val success = ItemService.deleteItem(id)
            if (success) {
                call.respondText("Элемент с ID $id успешно удален")
            } else {
                call.respondText("Элемент с ID $id не найден")
            }
        }
    }
}
