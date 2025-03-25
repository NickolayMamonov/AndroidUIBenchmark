//package com.research.uibenchmark.bdui
//
//import android.util.Log
//import com.research.uibenchmark.bdui.model.Action
//import com.research.uibenchmark.bdui.model.Margin
//import com.research.uibenchmark.bdui.model.Padding
//import com.research.uibenchmark.bdui.model.Screen
//import com.research.uibenchmark.bdui.model.UIComponentWrapper
//import com.research.uibenchmark.model.Item
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import kotlin.random.Random
//
///**
// * Имитирует серверное API для BDUI
// * В реальном приложении эти данные приходили бы от сервера
// */
//object BduiService {
//
//    private const val TAG = "BduiService"
//
//    /**
//     * Получить главный экран
//     */
//    fun getMainScreen(items: List<Item> = emptyList()): Screen {
//        Log.d(TAG, "Generating main screen with ${items.size} items")
//
//        // Создаем корневой компонент - контейнер
//        val rootComponent = UIComponentWrapper(
//            id = "main_container",
//            componentType = "container",
//            orientation = "vertical",
//            background = "#F5F5F5", // Светло-серый фон как в Compose
//            components = listOf(
//                // Список элементов
//                UIComponentWrapper(
//                    id = "items_list",
//                    componentType = "list",
//                    orientation = "vertical",
//                    padding = Padding(0, 8, 0, 8),
//                    items = items.map { item ->
//                        // Создаем компонент элемента списка, соответствующий XML/Compose версиям
//                        UIComponentWrapper(
//                            id = "item_${item.id}",
//                            componentType = "container",
//                            background = "#FFFFFF",
//                            cornerRadius = 12,
//                            margin = Margin(8, 8, 8, 8),
//                            padding = Padding(16, 16, 16, 16),
//                            components = listOf(
//                                // Заголовок элемента
//                                UIComponentWrapper(
//                                    id = "title_${item.id}",
//                                    componentType = "text",
//                                    text = item.title,
//                                    textSize = 18,
//                                    fontWeight = "bold",
//                                    textColor = "#000000"
//                                ),
//                                // Описание элемента
//                                UIComponentWrapper(
//                                    id = "description_${item.id}",
//                                    componentType = "text",
//                                    text = item.description,
//                                    textSize = 14,
//                                    textColor = "#444444",
//                                    margin = Margin(0, 8, 0, 8)
//                                ),
//                                // Дата элемента (справа)
//                                UIComponentWrapper(
//                                    id = "timestamp_${item.id}",
//                                    componentType = "text",
//                                    text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
//                                        .format(Date(item.timestamp)),
//                                    textSize = 12,
//                                    textColor = "#888888",
//                                    textAlign = "end",
//                                    fontWeight = "light"
//                                )
//                            ),
//                            // Действие при клике
//                            action = Action(
//                                type = "api_call",
//                                url = "item_click",
//                                payload = mapOf("item_id" to item.id.toString())
//                            )
//                        )
//                    }
//                ),
//
//                // Кнопка добавления нового элемента (FAB) - точно как в Compose
//                UIComponentWrapper(
//                    id = "add_button",
//                    componentType = "button",
//                    text = "+",
//                    backgroundColor = "#00BCD4", // Бирюзовый цвет как в Compose
//                    textColor = "#FFFFFF",
//                    cornerRadius = 28,
//                    margin = Margin(0, 0, 16, 16),
//                    action = Action(
//                        type = "api_call",
//                        url = "create_item",
//                        payload = null
//                    )
//                )
//            )
//        )
//
//        // Создаем экран с корневым компонентом
//        return Screen(
//            id = "main_screen",
//            title = null, // Убираем заголовок для соответствия Compose/XML
//            backgroundColor = "#F5F5F5",
//            rootComponent = rootComponent
//        )
//    }
//
//    /**
//     * Получить детальный экран для элемента
//     */
//    fun getDetailScreen(itemId: Long): Screen {
//        Log.d(TAG, "Generating detail screen for item $itemId")
//
//        // Имитируем данные элемента (в реальном приложении их бы запрашивали с сервера)
//        val item = Item(
//            id = itemId,
//            title = "Детальная информация #$itemId",
//            description = "Это подробное описание элемента №$itemId с дополнительной информацией. " +
//                    "В реальном приложении здесь был бы полный набор данных, запрошенный от сервера.",
//            imageUrl = null,
//            timestamp = System.currentTimeMillis() - Random.nextLong(0, 30 * 24 * 60 * 60 * 1000)
//        )
//
//        // Создаем корневой компонент
//        val rootComponent = UIComponentWrapper(
//            id = "detail_container",
//            componentType = "container",
//            orientation = "vertical",
//            background = "#FFFFFF",
//            components = listOf(
//                // Заголовок
//                UIComponentWrapper(
//                    id = "header",
//                    componentType = "container",
//                    background = "#00BCD4",
//                    padding = Padding(16, 16, 16, 16),
//                    components = listOf(
//                        UIComponentWrapper(
//                            id = "back_button",
//                            componentType = "button",
//                            text = "Назад",
//                            textColor = "#FFFFFF",
//                            backgroundColor = "#00BCD4",
//                            action = Action(
//                                type = "navigation",
//                                url = "/main",
//                                payload = null
//                            )
//                        ),
//                        UIComponentWrapper(
//                            id = "title",
//                            componentType = "text",
//                            text = item.title,
//                            textSize = 20,
//                            fontWeight = "bold",
//                            textColor = "#FFFFFF",
//                            margin = Margin(0, 8, 0, 0)
//                        )
//                    )
//                ),
//
//                // Содержимое
//                UIComponentWrapper(
//                    id = "content",
//                    componentType = "container",
//                    padding = Padding(16, 16, 16, 16),
//                    components = listOf(
//                        UIComponentWrapper(
//                            id = "image",
//                            componentType = "image",
//                            url = "placeholder_image",
//                            width = 200,
//                            height = 200,
//                            cornerRadius = 8
//                        ),
//                        UIComponentWrapper(
//                            id = "description_label",
//                            componentType = "text",
//                            text = "Описание:",
//                            textSize = 16,
//                            fontWeight = "bold",
//                            margin = Margin(0, 16, 0, 8)
//                        ),
//                        UIComponentWrapper(
//                            id = "description",
//                            componentType = "text",
//                            text = item.description,
//                            textSize = 14
//                        ),
//                        UIComponentWrapper(
//                            id = "timestamp_label",
//                            componentType = "text",
//                            text = "Дата создания:",
//                            textSize = 16,
//                            fontWeight = "bold",
//                            margin = Margin(0, 16, 0, 8)
//                        ),
//                        UIComponentWrapper(
//                            id = "timestamp",
//                            componentType = "text",
//                            text = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
//                                .format(Date(item.timestamp)),
//                            textSize = 14
//                        )
//                    )
//                )
//            )
//        )
//
//        return Screen(
//            id = "detail_screen",
//            title = "Подробности",
//            backgroundColor = "#FFFFFF",
//            rootComponent = rootComponent
//        )
//    }
//}
