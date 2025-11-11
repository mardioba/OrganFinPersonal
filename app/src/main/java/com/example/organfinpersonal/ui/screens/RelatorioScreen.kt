package com.example.organfinpersonal.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.organfinpersonal.viewmodel.TransacaoViewModel
import com.example.organfinpersonal.util.PdfExporter
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatorioScreen(
    viewModel: TransacaoViewModel,
    onVoltar: () -> Unit
) {
    val totalDespesas by viewModel.totalDespesas.collectAsState()
    val totalReceitas by viewModel.totalReceitas.collectAsState()
    val mesSelecionado by viewModel.mesSelecionado.collectAsState()
    val anoSelecionado by viewModel.anoSelecionado.collectAsState()
    
    val saldoFinal = totalReceitas - totalDespesas
    val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val context = LocalContext.current
    
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
                title = { Text("Relatório Mensal") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            PdfExporter.exportarRelatorio(
                                context = context,
                                mes = mesSelecionado,
                                ano = anoSelecionado,
                                totalDespesas = totalDespesas,
                                totalReceitas = totalReceitas,
                                saldoFinal = saldoFinal
                            )
                        }
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Exportar PDF")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
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
            
            // Cards de Resumo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Total de Despesas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatoMoeda.format(totalDespesas),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Total de Receitas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatoMoeda.format(totalReceitas),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (saldoFinal >= 0)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Saldo Final do Mês",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (saldoFinal >= 0)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formatoMoeda.format(saldoFinal),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (saldoFinal >= 0)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

