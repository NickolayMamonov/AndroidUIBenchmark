package com.research.uibenchmark.bdui.node

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.research.uibenchmark.model.Item
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Расширение для преобразования dp в пиксели
fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

/**
 * Узел для отображения элемента списка в декларативном стиле
 */
class ItemNode(private var item: Item) {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var timestampTextView: TextView
    
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    
    /**
     * Создает представление для отображения элемента
     */
    fun createView(context: Context): View {
        // Создаем MaterialCardView аналогично XML версии
        cardView = MaterialCardView(context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                val margin = context.dpToPx(8)
                setMargins(margin, margin, margin, margin)
            }
            radius = context.dpToPx(12).toFloat() // Увеличиваем радиус скругления
            elevation = context.dpToPx(4).toFloat()
            cardElevation = context.dpToPx(4).toFloat()
            useCompatPadding = true // Включаем поддержку совместимых отступов
            setCardBackgroundColor(Color.WHITE) // Добавляем белый фон для лучшего вида
        }
        
        // Создаем контейнер ConstraintLayout
        val constraintLayout = ConstraintLayout(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val padding = context.dpToPx(16)
            setPadding(padding, padding, padding, padding)
        }
        
        // Создаем ImageView
        imageView = ImageView(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                context.dpToPx(60),
                context.dpToPx(60)
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageResource(android.R.drawable.ic_menu_gallery)
            // Добавляем круглые края для изображения
            clipToOutline = true
            background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
            }
        }
        
        // Создаем TextView для заголовка по аналогии с XML
        titleTextView = TextView(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 18f
            setTextColor(Color.BLACK)
            text = item.title
            setTypeface(null, android.graphics.Typeface.BOLD) // Делаем текст жирным
        }
        
        // Создаем TextView для описания
        descriptionTextView = TextView(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            maxLines = 2
            ellipsize = android.text.TextUtils.TruncateAt.END
            text = item.description
            setTextColor(Color.DKGRAY) // Делаем текст темно-серым для лучшего контраста
            textSize = 14f
        }
        
        // Создаем TextView для времени по аналогии с XML
        timestampTextView = TextView(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 12f
            setTextColor(Color.GRAY) // Светло-серый цвет для даты
            setTypeface(null, android.graphics.Typeface.ITALIC) // Делаем текст курсивом
            text = dateFormat.format(Date(item.timestamp))
        }
        
        // Добавляем все элементы в контейнер
        constraintLayout.addView(imageView)
        constraintLayout.addView(titleTextView)
        constraintLayout.addView(descriptionTextView)
        constraintLayout.addView(timestampTextView)
        
        // Устанавливаем constraints
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        
        // ImageView constraints
        constraintSet.connect(imageView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        
        // Title constraints
        constraintSet.connect(titleTextView.id, ConstraintSet.START, imageView.id, ConstraintSet.END, context.dpToPx(16))
        constraintSet.connect(titleTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(titleTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        
        // Description constraints
        constraintSet.connect(descriptionTextView.id, ConstraintSet.START, imageView.id, ConstraintSet.END, context.dpToPx(16))
        constraintSet.connect(descriptionTextView.id, ConstraintSet.TOP, titleTextView.id, ConstraintSet.BOTTOM, context.dpToPx(8))
        constraintSet.connect(descriptionTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        
        // Timestamp constraints
        constraintSet.connect(timestampTextView.id, ConstraintSet.TOP, descriptionTextView.id, ConstraintSet.BOTTOM, context.dpToPx(8))
        constraintSet.connect(timestampTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        
        constraintSet.applyTo(constraintLayout)
        
        // Добавляем контейнер в CardView
        cardView.addView(constraintLayout)
        
        // Обновляем отображение данных
        update(item)
        
        return cardView
    }
    
    /**
     * Обновляет данные элемента
     */
    fun update(newItem: Item) {
        this.item = newItem
        
        if (::titleTextView.isInitialized) {
            titleTextView.text = newItem.title
            descriptionTextView.text = newItem.description
            timestampTextView.text = dateFormat.format(Date(newItem.timestamp))
            
            // Здесь должна быть загрузка изображения с помощью библиотеки
            // Для простоты этот код опущен
        }
    }
}
