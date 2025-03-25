//package com.research.uibenchmark.bdui.model
//
///**
// * Базовый интерфейс для всех компонентов UI
// */
//interface UIComponent {
//    val id: String
//    val type: String
//}
//
///**
// * Контейнер для группирования компонентов
// */
//data class ContainerComponent(
//    override val id: String,
//    override val type: String,
//    val orientation: String = "vertical",
//    val components: List<UIComponentWrapper> = emptyList(),
//    val background: String? = null,
//    val padding: Padding? = null,
//    val margin: Margin? = null
//) : UIComponent
//
///**
// * Компонент для отображения текста
// */
//data class TextComponent(
//    override val id: String,
//    override val type: String,
//    val text: String,
//    val textSize: Int? = null,
//    val textColor: String? = null,
//    val fontWeight: String? = null,
//    val textAlign: String? = null,
//    val padding: Padding? = null,
//    val margin: Margin? = null
//) : UIComponent
//
///**
// * Компонент кнопки
// */
//data class ButtonComponent(
//    override val id: String,
//    override val type: String,
//    val text: String,
//    val action: Action,
//    val textColor: String? = null,
//    val backgroundColor: String? = null,
//    val cornerRadius: Int? = null,
//    val padding: Padding? = null,
//    val margin: Margin? = null,
//    val enabled: Boolean = true
//) : UIComponent
//
///**
// * Компонент изображения
// */
//data class ImageComponent(
//    override val id: String,
//    override val type: String,
//    val url: String,
//    val contentScale: String? = null,
//    val width: Int? = null,
//    val height: Int? = null,
//    val cornerRadius: Int? = null,
//    val padding: Padding? = null,
//    val margin: Margin? = null
//) : UIComponent
//
///**
// * Компонент списка
// */
//data class ListComponent(
//    override val id: String,
//    override val type: String,
//    val items: List<UIComponentWrapper>,
//    val orientation: String = "vertical",
//    val dividerEnabled: Boolean = false,
//    val dividerColor: String? = null,
//    val padding: Padding? = null,
//    val margin: Margin? = null
//) : UIComponent
//
///**
// * Вспомогательные классы для стилизации компонентов
// */
//data class Padding(
//    val start: Int = 0,
//    val top: Int = 0,
//    val end: Int = 0,
//    val bottom: Int = 0
//)
//
//data class Margin(
//    val start: Int = 0,
//    val top: Int = 0,
//    val end: Int = 0,
//    val bottom: Int = 0
//)
//
///**
// * Действие для кнопок и других интерактивных элементов
// */
//data class Action(
//    val type: String, // navigation, api_call, и т.д.
//    val url: String?,
//    val payload: Map<String, String>?
//)
