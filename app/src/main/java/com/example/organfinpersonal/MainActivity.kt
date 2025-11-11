package com.example.organfinpersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.organfinpersonal.data.AppDatabase
import com.example.organfinpersonal.navigation.NavGraph
import com.example.organfinpersonal.repository.TransacaoRepository
import com.example.organfinpersonal.ui.theme.OrganFinPersonalTheme
import com.example.organfinpersonal.viewmodel.TransacaoViewModel
import com.example.organfinpersonal.viewmodel.TransacaoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = TransacaoRepository(database.transacaoDao())
        val viewModelFactory = TransacaoViewModelFactory(repository)
        
        setContent {
            OrganFinPersonalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: TransacaoViewModel = viewModel(factory = viewModelFactory)
                    
                    NavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}