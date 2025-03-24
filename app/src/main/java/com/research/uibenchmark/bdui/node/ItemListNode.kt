package com.research.uibenchmark.bdui.node

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.research.uibenchmark.model.Item

/**
 * Состояние для отображения списка элементов
 */
data class ItemsState(
    val items: List<Item>,
    val isLoading: Boolean
)

/**
 * Узел для отображения списка элементов в декларативном стиле
 */
class ItemListNode(
    private var state: ItemsState,
    private val onItemClick: (Item) -> Unit,
    private val onRefresh: () -> Unit,
    private val onAddClick: () -> Unit
) {
    private lateinit var rootView: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: ItemNodeAdapter
    
    /**
     * Создает представление для отображения списка - более похоже на XML версию
     */
    fun createView(context: Context): View {
        // Создаем корневой ConstraintLayout как в XML версии
        rootView = ConstraintLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // Устанавливаем светло-серый фон как в Compose версии
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
        
        // Создаем SwipeRefreshLayout
        swipeRefreshLayout = SwipeRefreshLayout(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                0, // width = 0, чтобы растянуть по ограничениям
                0  // height = 0, чтобы растянуть по ограничениям
            ).apply {
                // Устанавливаем ограничения для заполнения всего пространства
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            setOnRefreshListener { onRefresh() }
        }
        
        // Создаем RecyclerView
        recyclerView = RecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(0, 0, 0, 0) // Удаляем отступы у RecyclerView 
            clipToPadding = false // Разрешаем прокрутку под отступы
            layoutManager = LinearLayoutManager(context)
            adapter = ItemNodeAdapter(state.items, onItemClick).also {
                this@ItemListNode.adapter = it
            }
        }
        
        // Создаем ProgressBar как в XML версии
        progressBar = ProgressBar(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // Центрируем ProgressBar
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            visibility = if (state.isLoading) View.VISIBLE else View.GONE
        }
        
        // Добавляем FloatingActionButton
        val fab = FloatingActionButton(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // Размещаем кнопку в правом нижнем углу
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                bottomMargin = (16 * context.resources.displayMetrics.density).toInt()
                rightMargin = (16 * context.resources.displayMetrics.density).toInt()
            }
            setOnClickListener { onAddClick() }
            setImageResource(android.R.drawable.ic_input_add)
            // Устанавливаем бирюзовый цвет как в Compose
            backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#00BCD4"))
            // Делаем иконку белой
            imageTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
        }
        
        // Компонуем представление
        swipeRefreshLayout.addView(recyclerView)
        rootView.addView(swipeRefreshLayout)
        rootView.addView(progressBar)
        rootView.addView(fab)
        
        return rootView
    }
    
    /**
     * Обновляет список элементов
     */
    fun updateItems(items: List<Item>) {
        state = state.copy(items = items)
        adapter.updateItems(items)
    }
    
    /**
     * Обновляет состояние загрузки
     */
    fun updateLoading(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
        swipeRefreshLayout.isRefreshing = isLoading
        if (::progressBar.isInitialized) {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    /**
     * Прокручивает список для тестирования производительности
     */
    fun scrollBy(dx: Int, dy: Int) {
        recyclerView.scrollBy(dx, dy)
    }
}

/**
 * Адаптер для отображения элементов списка
 */
private class ItemNodeAdapter(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemNodeAdapter.ItemNodeViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNodeViewHolder {
        val itemNode = ItemNode(Item(0, "", "", null, 0))
        return ItemNodeViewHolder(itemNode.createView(parent.context), itemNode)
    }
    
    override fun onBindViewHolder(holder: ItemNodeViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    fun updateItems(newItems: List<Item>) {
        // Используем DiffUtil для более эффективного обновления списка
        val diffCallback = object : androidx.recyclerview.widget.DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == newItems[newItemPosition].id
            }
            
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
            
            override fun getOldListSize() = items.size
            
            override fun getNewListSize() = newItems.size
        }
        
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
    
    inner class ItemNodeViewHolder(view: View, private val itemNode: ItemNode) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Добавим анимацию при нажатии для лучшего пользовательского опыта
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction {
                            view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start()
                            onItemClick(items[position])
                        }
                        .start()
                }
            }
        }
        
        fun bind(item: Item) {
            itemNode.update(item)
        }
    }
}
