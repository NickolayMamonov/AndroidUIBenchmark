package com.research.uibenchmark.bdui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.research.uibenchmark.bdui.viewmodel.BDUIViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BDUIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SuccessScreen(viewModel)
            }
        }
    }

    override fun onBackPressed() {
        if (!viewModel.navigateBack()) {
            super.onBackPressed()
        }
    }
}