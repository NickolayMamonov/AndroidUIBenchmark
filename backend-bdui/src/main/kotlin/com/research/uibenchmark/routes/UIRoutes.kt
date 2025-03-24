package com.research.uibenchmark.routes

import com.research.uibenchmark.models.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.request.*

fun Route.configureUIRoutes() {
    route("/api/ui") {
        // Получение главного экрана
        get("/main") {
            call.respond(createMainScreen())
        }

        // Получение экрана деталей
        get("/details/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id parameter",
                status = HttpStatusCode.BadRequest
            )

            call.respond(createDetailScreen(id))
        }

        // Получение экрана настроек
        get("/settings") {
            call.respond(createSettingsScreen())
        }

        // Получение экрана профиля
        get("/profile") {
            call.respond(createProfileScreen())
        }

        // Обработка отправленных данных
        post("/submit") {
            val parameters = call.receiveParameters()
            // Здесь можно обработать полученные данные

            // Возвращаем экран с подтверждением
            call.respond(createConfirmationScreen())
        }
    }
}

// Функции для создания различных экранов

fun createMainScreen(): BDUIResponse {
    val listItems = (1..5).map { index ->
        ContainerComponent(
            id = "item_$index",
            orientation = "horizontal",
            padding = Padding(16, 16, 16, 16),
            components = listOf(
                ImageComponent(
                    id = "image_$index",
                    url = "https://picsum.photos/id/${100 + index}/200",
                    width = 80,
                    height = 80,
                    cornerRadius = 8
                ),
                ContainerComponent(
                    id = "item_content_$index",
                    orientation = "vertical",
                    padding = Padding(left = 16),
                    components = listOf(
                        TextComponent(
                            id = "item_title_$index",
                            text = "Заголовок элемента $index",
                            textSize = 18,
                            fontWeight = "bold"
                        ),
                        TextComponent(
                            id = "item_desc_$index",
                            text = "Описание элемента списка $index с дополнительной информацией",
                            textSize = 14,
                            textColor = "#666666",
                            margin = Margin(top = 4)
                        ),
                        ButtonComponent(
                            id = "details_button_$index",
                            text = "Подробнее",
                            action = Action(
                                type = "navigate",
                                url = "/api/ui/details/$index"
                            ),
                            margin = Margin(top = 8),
                            cornerRadius = 4,
                            backgroundColor = "#2196F3",
                            textColor = "#FFFFFF"
                        )
                    )
                )
            )
        )
    }

    val screen = Screen(
        id = "main_screen",
        title = "Главный экран",
        toolbarColor = "#2196F3",
        backgroundColor = "#F5F5F5",
        rootComponent = ContainerComponent(
            id = "main_container",
            components = listOf(
                ContainerComponent(
                    id = "header_container",
                    padding = Padding(16, 16, 16, 8),
                    background = "#2196F3",
                    components = listOf(
                        TextComponent(
                            id = "header_title",
                            text = "Добро пожаловать в приложение",
                            textSize = 24,
                            fontWeight = "bold",
                            textColor = "#FFFFFF"
                        ),
                        TextComponent(
                            id = "header_subtitle",
                            text = "Выберите элемент из списка",
                            textSize = 16,
                            textColor = "#E0E0E0",
                            margin = Margin(top = 4)
                        )
                    )
                ),
                ListComponent(
                    id = "main_list",
                    items = listItems,
                    dividerEnabled = true,
                    dividerColor = "#E0E0E0"
                )
            )
        )
    )

    return BDUIResponse(screen = screen)
}

fun createDetailScreen(id: String): BDUIResponse {
    val screen = Screen(
        id = "detail_screen_$id",
        title = "Детали элемента $id",
        toolbarColor = "#2196F3",
        backgroundColor = "#FFFFFF",
        rootComponent = ContainerComponent(
            id = "detail_container",
            padding = Padding(16, 16, 16, 16),
            components = listOf(
                ImageComponent(
                    id = "detail_image",
                    url = "https://picsum.photos/id/100/400",
                    cornerRadius = 8,
                    contentScale = "fit"
                ),
                TextComponent(
                    id = "detail_title",
                    text = "Детали элемента $id",
                    textSize = 22,
                    fontWeight = "bold",
                    margin = Margin(top = 16)
                ),
                TextComponent(
                    id = "detail_description",
                    text = "Это подробная информация об элементе $id. Здесь содержится расширенное описание элемента с дополнительными деталями, характеристиками и прочей полезной информацией.",
                    textSize = 16,
                    margin = Margin(top = 8)
                ),
                ContainerComponent(
                    id = "buttons_container",
                    orientation = "horizontal",
                    margin = Margin(top = 24),
                    components = listOf(
                        ButtonComponent(
                            id = "back_button",
                            text = "Назад",
                            action = Action(
                                type = "navigate",
                                url = "/api/ui/main"
                            ),
                            backgroundColor = "#9E9E9E",
                            textColor = "#FFFFFF",
                            cornerRadius = 4
                        ),
                        ButtonComponent(
                            id = "action_button",
                            text = "Действие",
                            action = Action(
                                type = "api_call",
                                url = "/api/action/$id",
                                payload = mapOf("item_id" to id)
                            ),
                            backgroundColor = "#2196F3",
                            textColor = "#FFFFFF",
                            cornerRadius = 4,
                            margin = Margin(left = 16)
                        )
                    )
                )
            )
        )
    )

    return BDUIResponse(screen = screen)
}

fun createSettingsScreen(): BDUIResponse {
    val screen = Screen(
        id = "settings_screen",
        title = "Настройки",
        toolbarColor = "#2196F3",
        backgroundColor = "#FFFFFF",
        rootComponent = ContainerComponent(
            id = "settings_container",
            padding = Padding(16, 16, 16, 16),
            components = listOf(
                TextComponent(
                    id = "settings_title",
                    text = "Настройки приложения",
                    textSize = 22,
                    fontWeight = "bold"
                ),
                TextComponent(
                    id = "settings_description",
                    text = "Здесь вы можете настроить параметры приложения",
                    textSize = 16,
                    margin = Margin(top = 8, bottom = 16)
                ),
                ContainerComponent(
                    id = "settings_group",
                    components = listOf(
                        TextComponent(
                            id = "theme_label",
                            text = "Темная тема",
                            textSize = 16
                        ),
                        ButtonComponent(
                            id = "theme_toggle",
                            text = "Включить",
                            action = Action(
                                type = "toggle",
                                payload = mapOf("setting" to "dark_theme")
                            ),
                            backgroundColor = "#E0E0E0",
                            margin = Margin(top = 8)
                        ),
                        TextComponent(
                            id = "notifications_label",
                            text = "Уведомления",
                            textSize = 16,
                            margin = Margin(top = 16)
                        ),
                        ButtonComponent(
                            id = "notifications_toggle",
                            text = "Выключить",
                            action = Action(
                                type = "toggle",
                                payload = mapOf("setting" to "notifications")
                            ),
                            backgroundColor = "#E0E0E0",
                            margin = Margin(top = 8)
                        ),
                        TextComponent(
                            id = "language_label",
                            text = "Язык",
                            textSize = 16,
                            margin = Margin(top = 16)
                        ),
                        ButtonComponent(
                            id = "language_ru",
                            text = "Русский",
                            action = Action(
                                type = "api_call",
                                payload = mapOf("language" to "ru")
                            ),
                            backgroundColor = "#2196F3",
                            textColor = "#FFFFFF",
                            margin = Margin(top = 8)
                        ),
                        ButtonComponent(
                            id = "language_en",
                            text = "English",
                            action = Action(
                                type = "api_call",
                                payload = mapOf("language" to "en")
                            ),
                            backgroundColor = "#E0E0E0",
                            margin = Margin(top = 8)
                        )
                    )
                ),
                ButtonComponent(
                    id = "save_settings_button",
                    text = "Сохранить настройки",
                    action = Action(
                        type = "api_call",
                        url = "/api/settings/save"
                    ),
                    backgroundColor = "#4CAF50",
                    textColor = "#FFFFFF",
                    cornerRadius = 4,
                    margin = Margin(top = 24)
                )
            )
        )
    )

    return BDUIResponse(screen = screen)
}

fun createProfileScreen(): BDUIResponse {
    val screen = Screen(
        id = "profile_screen",
        title = "Профиль",
        toolbarColor = "#2196F3",
        backgroundColor = "#FFFFFF",
        rootComponent = ContainerComponent(
            id = "profile_container",
            padding = Padding(16, 16, 16, 16),
            components = listOf(
                ImageComponent(
                    id = "profile_avatar",
                    url = "https://picsum.photos/id/237/200",
                    width = 120,
                    height = 120,
                    cornerRadius = 60
                ),
                TextComponent(
                    id = "profile_name",
                    text = "Иван Иванов",
                    textSize = 22,
                    fontWeight = "bold",
                    textAlign = "center",
                    margin = Margin(top = 16)
                ),
                TextComponent(
                    id = "profile_email",
                    text = "ivan@example.com",
                    textSize = 16,
                    textAlign = "center",
                    textColor = "#666666",
                    margin = Margin(top = 4)
                ),
                ContainerComponent(
                    id = "profile_form",
                    margin = Margin(top = 24),
                    components = listOf(
                        TextComponent(
                            id = "name_label",
                            text = "Имя",
                            textSize = 14,
                            textColor = "#666666"
                        ),
                        InputComponent(
                            id = "name_input",
                            initialValue = "Иван Иванов",
                            inputType = "text",
                            backgroundColor = "#F5F5F5",
                            cornerRadius = 4,
                            padding = Padding(12, 12, 12, 12),
                            margin = Margin(top = 4)
                        ),
                        TextComponent(
                            id = "email_label",
                            text = "Email",
                            textSize = 14,
                            textColor = "#666666",
                            margin = Margin(top = 16)
                        ),
                        InputComponent(
                            id = "email_input",
                            initialValue = "ivan@example.com",
                            inputType = "email",
                            backgroundColor = "#F5F5F5",
                            cornerRadius = 4,
                            padding = Padding(12, 12, 12, 12),
                            margin = Margin(top = 4)
                        ),
                        TextComponent(
                            id = "phone_label",
                            text = "Телефон",
                            textSize = 14,
                            textColor = "#666666",
                            margin = Margin(top = 16)
                        ),
                        InputComponent(
                            id = "phone_input",
                            initialValue = "+7 123 456 7890",
                            inputType = "text",
                            backgroundColor = "#F5F5F5",
                            cornerRadius = 4,
                            padding = Padding(12, 12, 12, 12),
                            margin = Margin(top = 4)
                        )
                    )
                ),
                ButtonComponent(
                    id = "save_profile_button",
                    text = "Сохранить",
                    action = Action(
                        type = "api_call",
                        url = "/api/profile/save"
                    ),
                    backgroundColor = "#4CAF50",
                    textColor = "#FFFFFF",
                    cornerRadius = 4,
                    padding = Padding(16, 16, 16, 16),
                    margin = Margin(top = 24)
                )
            )
        )
    )

    return BDUIResponse(screen = screen)
}

fun createConfirmationScreen(): BDUIResponse {
    val screen = Screen(
        id = "confirmation_screen",
        title = "Успешно",
        toolbarColor = "#4CAF50",
        backgroundColor = "#FFFFFF",
        rootComponent = ContainerComponent(
            id = "confirmation_container",
            padding = Padding(16, 32, 16, 16),
            components = listOf(
                TextComponent(
                    id = "confirmation_title",
                    text = "Данные успешно обработаны",
                    textSize = 22,
                    fontWeight = "bold",
                    textAlign = "center"
                ),
                TextComponent(
                    id = "confirmation_message",
                    text = "Ваш запрос был успешно обработан системой",
                    textSize = 16,
                    textAlign = "center",
                    margin = Margin(top = 16)
                ),
                ButtonComponent(
                    id = "back_to_main_button",
                    text = "Вернуться на главную",
                    action = Action(
                        type = "navigate",
                        url = "/api/ui/main"
                    ),
                    backgroundColor = "#4CAF50",
                    textColor = "#FFFFFF",
                    cornerRadius = 4,
                    padding = Padding(16, 16, 16, 16),
                    margin = Margin(top = 32)
                )
            )
        )
    )

    return BDUIResponse(screen = screen)
}
