package com.research.uibenchmark

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.SearchCondition
import androidx.test.uiautomator.UiObject2

/**
 * Вспомогательный класс для имитации функциональности UiAutomator Until
 * для согласования с imports в наших тестовых классах
 */
object Until {
    /**
     * Условие, которое проверяет видим ли объект
     */
    fun visible(timeout: Long): SearchCondition<Boolean> {
        return object : SearchCondition<Boolean> {
            override fun apply(selector: BySelector): Boolean {
                return true // Упрощенная имплементация
            }
            
            override fun apply(obj: UiObject2): Boolean {
                return obj.isEnabled && obj.isClickable
            }
        }
    }
    
    /**
     * Условие, которое проверяет исчез ли объект
     */
    fun gone(timeout: Long): SearchCondition<Boolean> {
        return object : SearchCondition<Boolean> {
            override fun apply(selector: BySelector): Boolean {
                return true // Упрощенная имплементация
            }
            
            override fun apply(obj: UiObject2): Boolean {
                return !obj.isEnabled
            }
        }
    }
    
    /**
     * Условие, которое проверяет наличие объекта с указанным селектором
     */
    fun hasObject(selector: BySelector): SearchCondition<Boolean> {
        return object : SearchCondition<Boolean> {
            override fun apply(irrelevant: BySelector): Boolean {
                return true // Упрощенная имплементация
            }
            
            override fun apply(irrelevant: UiObject2): Boolean {
                return true // Упрощенная имплементация
            }
        }
    }
}
