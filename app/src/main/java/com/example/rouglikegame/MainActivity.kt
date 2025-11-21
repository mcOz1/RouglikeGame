package com.example.rouglikegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rouglikegame.data.GameRepository
import com.example.rouglikegame.ui.screens.MainScreen
import com.example.rouglikegame.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tworzymy repozytorium (konieczny context)
        val repository = GameRepository(applicationContext)

        // Fabryka ViewModelu
        val viewModel: GameViewModel by viewModels {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GameViewModel(repository) as T
                }
            }
        }

        setContent {
            MainScreen(viewModel)
        }
    }
}