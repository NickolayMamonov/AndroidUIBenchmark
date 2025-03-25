package com.research.uibenchmark.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.research.uibenchmark.models.Item
import com.research.uibenchmark.services.ItemService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ItemRoutes")

fun Application.configureItemRoutes() {
    logger.info("Configuring item routes")
    
    routing {
        // Дублируем маршруты с /api префиксом и без него
        
        // UI запросы для клиентских приложений - без префикса
        get("/ui/main") {
            logger.info("Received request to /ui/main")
            call.respond(mapOf(
                "screen" to "main",
                "title" to "Main Screen",
                "components" to listOf(mapOf(
                    "type" to "text",
                    "id" to "title",
                    "text" to "Welcome to UI Benchmark",
                    "style" to "header"
                ))
            ))
        }
        
        get("/ui/items") {
            call.respond(mapOf(
                "screen" to "items",
                "title" to "Items Screen",
                "components" to listOf(mapOf(
                    "type" to "list",
                    "id" to "itemsList",
                    "items" to ItemService.getAllItems()
                ))
            ))
        }
        
        get("/ui/items/{id}") {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respondText("Invalid ID")
            val item = ItemService.getItemById(id)
            
            if (item != null) {
                call.respond(mapOf(
                    "screen" to "itemDetail",
                    "title" to "Item Detail",
                    "components" to listOf(
                        mapOf(
                            "type" to "text",
                            "id" to "detailTitle",
                            "text" to item.title,
                            "style" to "header"
                        ),
                        mapOf(
                            "type" to "text",
                            "id" to "detailDescription",
                            "text" to item.description
                        )
                    )
                ))
            } else {
                call.respondText("Item with ID $id not found")
            }
        }
        
        get("/ui/create-item") {
            call.respond(mapOf(
                "screen" to "createItem",
                "title" to "Create New Item",
                "components" to listOf(
                    mapOf(
                        "type" to "text",
                        "id" to "createTitle",
                        "text" to "Create New Item",
                        "style" to "header"
                    ),
                    mapOf(
                        "type" to "input",
                        "id" to "titleInput",
                        "hint" to "Enter title"
                    ),
                    mapOf(
                        "type" to "input",
                        "id" to "descriptionInput",
                        "hint" to "Enter description"
                    ),
                    mapOf(
                        "type" to "button",
                        "id" to "submitButton",
                        "text" to "Submit",
                        "action" to "/api/items/create"
                    )
                )
            ))
        }
        
        get("/ui/benchmark") {
            call.respond(mapOf(
                "screen" to "benchmark",
                "title" to "Benchmark",
                "components" to listOf(
                    mapOf(
                        "type" to "text",
                        "id" to "benchmarkTitle",
                        "text" to "Benchmark Results",
                        "style" to "header"
                    ),
                    mapOf(
                        "type" to "text",
                        "id" to "benchmarkDescription",
                        "text" to "Performance metrics for different UI approaches"
                    )
                )
            ))
        }
        
        // UI запросы с префиксом /api
        get("/api/ui/main") {
            logger.info("Received request to /api/ui/main")
            call.respond(mapOf(
                "screen" to "main",
                "title" to "Main Screen",
                "components" to listOf(mapOf(
                    "type" to "text",
                    "id" to "title",
                    "text" to "Welcome to UI Benchmark",
                    "style" to "header"
                ))
            ))
        }
        
        get("/api/ui/items") {
            logger.info("Received request to /api/ui/items")
            call.respond(mapOf(
                "screen" to "items",
                "title" to "Items Screen",
                "components" to listOf(mapOf(
                    "type" to "list",
                    "id" to "itemsList",
                    "items" to ItemService.getAllItems()
                ))
            ))
        }
        
        get("/api/ui/items/{id}") {
            logger.info("Received request to /api/ui/items/{id}")
            val id = call.parameters["id"]?.toLong() ?: return@get call.respondText("Invalid ID")
            val item = ItemService.getItemById(id)
            
            if (item != null) {
                call.respond(mapOf(
                    "screen" to "itemDetail",
                    "title" to "Item Detail",
                    "components" to listOf(
                        mapOf(
                            "type" to "text",
                            "id" to "detailTitle",
                            "text" to item.title,
                            "style" to "header"
                        ),
                        mapOf(
                            "type" to "text",
                            "id" to "detailDescription",
                            "text" to item.description
                        )
                    )
                ))
            } else {
                call.respondText("Item with ID $id not found")
            }
        }
        
        get("/api/ui/create-item") {
            logger.info("Received request to /api/ui/create-item")
            call.respond(mapOf(
                "screen" to "createItem",
                "title" to "Create New Item",
                "components" to listOf(
                    mapOf(
                        "type" to "text",
                        "id" to "createTitle",
                        "text" to "Create New Item",
                        "style" to "header"
                    ),
                    mapOf(
                        "type" to "input",
                        "id" to "titleInput",
                        "hint" to "Enter title"
                    ),
                    mapOf(
                        "type" to "input",
                        "id" to "descriptionInput",
                        "hint" to "Enter description"
                    ),
                    mapOf(
                        "type" to "button",
                        "id" to "submitButton",
                        "text" to "Submit",
                        "action" to "/api/items/create"
                    )
                )
            ))
        }
        
        get("/api/ui/benchmark") {
            logger.info("Received request to /api/ui/benchmark")
            call.respond(mapOf(
                "screen" to "benchmark",
                "title" to "Benchmark",
                "components" to listOf(
                    mapOf(
                        "type" to "text",
                        "id" to "benchmarkTitle",
                        "text" to "Benchmark Results",
                        "style" to "header"
                    ),
                    mapOf(
                        "type" to "text",
                        "id" to "benchmarkDescription",
                        "text" to "Performance metrics for different UI approaches"
                    )
                )
            ))
        }
        
        // API запросы для работы с данными
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