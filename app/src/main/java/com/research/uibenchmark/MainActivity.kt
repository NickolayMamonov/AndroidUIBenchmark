package com.research.uibenchmark

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.research.uibenchmark.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupButtons()
    }
    
    private fun setupButtons() {
        binding.btnXml.setOnClickListener {
            launchApp("com.research.uibenchmark.xml")
        }
        
        binding.btnCompose.setOnClickListener {
            launchApp("com.research.uibenchmark.compose")
        }
        
        binding.btnBdui.setOnClickListener {
            launchApp("com.research.uibenchmark.bdui")
        }
    }
    
    private fun launchApp(packageName: String) {
        try {
            // Для внутренних активити
            val activityClass = when (packageName) {
                "com.research.uibenchmark.xml" -> com.research.uibenchmark.xml.XmlActivity::class.java
                "com.research.uibenchmark.compose" -> com.research.uibenchmark.compose.ComposeActivity::class.java
                "com.research.uibenchmark.bdui" -> com.research.uibenchmark.bdui.BduiActivity::class.java
                else -> null
            }
            
            if (activityClass != null) {
                val intent = Intent(this, activityClass)
                startActivity(intent)
            } else {
                // Пытаемся найти как внешнее приложение
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                if (intent != null) {
                    startActivity(intent)
                } else {
                    showAppNotInstalledMessage(packageName)
                }
            }
        } catch (e: Exception) {
            showAppNotInstalledMessage(packageName)
        }
    }
    
    private fun showAppNotInstalledMessage(packageName: String) {
        val appName = when (packageName) {
            "com.research.uibenchmark.xml" -> "XML Benchmark"
            "com.research.uibenchmark.compose" -> "Compose Benchmark"
            "com.research.uibenchmark.bdui" -> "BDUI Benchmark"
            else -> packageName
        }
        
        // Здесь можно показать сообщение о том, что приложение не установлено
    }
}
