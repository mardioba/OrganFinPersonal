package com.example.organfinpersonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.organfinpersonal.data.TipoTransacao
import com.example.organfinpersonal.data.Transacao
import com.example.organfinpersonal.viewmodel.TransacaoViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTransacoesScreen(
    viewModel: TransacaoViewModel,
    onVoltar: () -> Unit
) {
    val transacoes by viewModel.transacoes.collectAsState()
    val mesSelecionado by viewModel.mesSelecionado.collectAsState()
    val anoSelecionado by viewModel.anoSelecionado.collectAsState()
    
    var transacaoSelecionada by remember { mutableStateOf<Transacao?>(null) }
    var mostrarDialogoRecorrente by remember { mutableStateOf(false) }
    var mostrarDialogoSimples by remember { mutableStateOf(false) }
    
    val meses = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )
    
    var mesExpandido by remember { mutableStateOf(false) }
    var anoExpandido by remember { mutableStateOf(false) }
    
    val anos = (2020..2030).toList()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transações") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            transacaoSelecionada?.let { selecionada ->
                if (mostrarDialogoRecorrente && selecionada.recorrente) {
                    AlertDialog(
                        onDismissRequest = {
                            mostrarDialogoRecorrente = false
                            transacaoSelecionada = null
                        },
                        title = { Text("Excluir transação recorrente") },
                        text = {
                            Text("Deseja excluir apenas esta parcela ou todas as parcelas futuras desta série?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.deletarTransacao(selecionada, incluirSerie = true)
                                    mostrarDialogoRecorrente = false
                                    transacaoSelecionada = null
                                }
                            ) {
                                Text("Excluir série")
                            }
                        },
                        dismissButton = {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(
                                    onClick = {
                                        viewModel.deletarTransacao(selecionada, incluirSerie = false)
                                        mostrarDialogoRecorrente = false
                                        transacaoSelecionada = null
                                    }
                                ) {
                                    Text("Apenas esta")
                                }
                                TextButton(
                                    onClick = {
                                        mostrarDialogoRecorrente = false
                                        transacaoSelecionada = null
                                    }
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    )
                } else if (mostrarDialogoSimples) {
                    AlertDialog(
                        onDismissRequest = {
                            mostrarDialogoSimples = false
                            transacaoSelecionada = null
                        },
                        title = { Text("Excluir transação") },
                        text = { Text("Deseja realmente excluir esta transação?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.deletarTransacao(selecionada, incluirSerie = false)
                                    mostrarDialogoSimples = false
                                    transacaoSelecionada = null
                                }
                            ) {
                                Text("Excluir")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    mostrarDialogoSimples = false
                                    transacaoSelecionada = null
                                }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
            
            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mês
                ExposedDropdownMenuBox(
                    expanded = mesExpandido,
                    onExpandedChange = { mesExpandido = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = meses[mesSelecionado - 1],
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Mês") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mesExpandido) }
                    )
                    ExposedDropdownMenu(
                        expanded = mesExpandido,
                        onDismissRequest = { mesExpandido = false }
                    ) {
                        meses.forEachIndexed { index, mes ->
                            DropdownMenuItem(
                                text = { Text(mes) },
                                onClick = {
                                    viewModel.filtrarPorMesAno(index + 1, anoSelecionado)
                                    mesExpandido = false
                                }
                            )
                        }
                    }
                }
                
                // Ano
                ExposedDropdownMenuBox(
                    expanded = anoExpandido,
                    onExpandedChange = { anoExpandido = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = anoSelecionado.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ano") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = anoExpandido) }
                    )
                    ExposedDropdownMenu(
                        expanded = anoExpandido,
                        onDismissRequest = { anoExpandido = false }
                    ) {
                        anos.forEach { ano ->
                            DropdownMenuItem(
                                text = { Text(ano.toString()) },
                                onClick = {
                                    viewModel.filtrarPorMesAno(mesSelecionado, ano)
                                    anoExpandido = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Lista de Transações
            if (transacoes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma transação encontrada",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transacoes) { transacao ->
                        TransacaoItem(
                            transacao = transacao,
                            onDelete = {
                                transacaoSelecionada = transacao
                                if (transacao.recorrente) {
                                    mostrarDialogoRecorrente = true
                                } else {
                                    mostrarDialogoSimples = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransacaoItem(
    transacao: Transacao,
    onDelete: () -> Unit
) {
    val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val dataFormatada = formatoData.format(Date(transacao.data))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transacao.tipo == TipoTransacao.RECEITA)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transacao.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transacao.categoria,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = dataFormatada,
                    style = MaterialTheme.typography.bodySmall
                )
                if (transacao.recorrente) {
                    Text(
                        text = "Parcela ${transacao.parcelaAtual}/${transacao.quantidadeParcelas}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatoMoeda.format(transacao.valor),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (transacao.tipo == TipoTransacao.RECEITA)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Deletar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

