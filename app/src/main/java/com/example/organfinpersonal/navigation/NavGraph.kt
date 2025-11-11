package com.example.organfinpersonal.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.organfinpersonal.data.TipoTransacao
import com.example.organfinpersonal.ui.screens.*
import com.example.organfinpersonal.viewmodel.TransacaoViewModel
import kotlinx.coroutines.delay

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NovaDespesa : Screen("nova_despesa")
    object NovaReceita : Screen("nova_receita")
    object ListaTransacoes : Screen("lista_transacoes")
    object Relatorio : Screen("relatorio")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: TransacaoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNovaDespesa = { navController.navigate(Screen.NovaDespesa.route) },
                onNovaReceita = { navController.navigate(Screen.NovaReceita.route) },
                onListaTransacoes = { navController.navigate(Screen.ListaTransacoes.route) },
                onRelatorio = { navController.navigate(Screen.Relatorio.route) }
            )
        }
        
        composable(Screen.NovaDespesa.route) {
            var mostrarSucesso by remember { mutableStateOf(false) }
            var mostrarErro by remember { mutableStateOf(false) }
            
            val snackbarHostState = remember { SnackbarHostState() }
            
            if (mostrarSucesso) {
                LaunchedEffect(mostrarSucesso) {
                    snackbarHostState.showSnackbar("Transação salva com sucesso!")
                    delay(1500)
                    navController.popBackStack()
                }
            }
            
            if (mostrarErro) {
                LaunchedEffect(mostrarErro) {
                    snackbarHostState.showSnackbar("Erro ao salvar transação")
                    mostrarErro = false
                }
            }
            
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                        Snackbar(snackbarData)
                    }
                }
            ) { paddingValues ->
                CadastroTransacaoScreen(
                    tipoTransacao = TipoTransacao.DESPESA,
                    onSalvar = { titulo, valor, categoria, data, recorrente, quantidadeParcelas, observacao ->
                        val sucesso = viewModel.inserirTransacao(
                            titulo, valor, TipoTransacao.DESPESA, categoria,
                            data, recorrente, quantidadeParcelas, observacao
                        )
                        if (sucesso) {
                            mostrarSucesso = true
                        } else {
                            mostrarErro = true
                        }
                        sucesso
                    },
                    onCancelar = { navController.popBackStack() }
                )
            }
        }
        
        composable(Screen.NovaReceita.route) {
            var mostrarSucesso by remember { mutableStateOf(false) }
            var mostrarErro by remember { mutableStateOf(false) }
            
            val snackbarHostState = remember { SnackbarHostState() }
            
            if (mostrarSucesso) {
                LaunchedEffect(mostrarSucesso) {
                    snackbarHostState.showSnackbar("Transação salva com sucesso!")
                    delay(1500)
                    navController.popBackStack()
                }
            }
            
            if (mostrarErro) {
                LaunchedEffect(mostrarErro) {
                    snackbarHostState.showSnackbar("Erro ao salvar transação")
                    mostrarErro = false
                }
            }
            
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                        Snackbar(snackbarData)
                    }
                }
            ) { paddingValues ->
                CadastroTransacaoScreen(
                    tipoTransacao = TipoTransacao.RECEITA,
                    onSalvar = { titulo, valor, categoria, data, recorrente, quantidadeParcelas, observacao ->
                        val sucesso = viewModel.inserirTransacao(
                            titulo, valor, TipoTransacao.RECEITA, categoria,
                            data, false, 1, observacao
                        )
                        if (sucesso) {
                            mostrarSucesso = true
                        } else {
                            mostrarErro = true
                        }
                        sucesso
                    },
                    onCancelar = { navController.popBackStack() }
                )
            }
        }
        
        composable(Screen.ListaTransacoes.route) {
            ListaTransacoesScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Relatorio.route) {
            RelatorioScreen(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}

