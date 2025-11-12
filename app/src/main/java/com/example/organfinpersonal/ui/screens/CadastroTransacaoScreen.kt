package com.example.organfinpersonal.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.organfinpersonal.data.TipoTransacao
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroTransacaoScreen(
    tipoTransacao: TipoTransacao,
    onSalvar: (
        titulo: String,
        valor: Double,
        categoria: String,
        data: Long,
        recorrente: Boolean,
        quantidadeParcelas: Int,
        observacao: String?
    ) -> Boolean,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var dataSelecionada by remember { mutableStateOf(Calendar.getInstance()) }
    var recorrente by remember { mutableStateOf(false) }
    var quantidadeParcelas by remember { mutableStateOf("1") }
    var observacao by remember { mutableStateOf("") }
    var mostrarErro by remember { mutableStateOf(false) }
    
    val context = LocalContext.current

    val categorias = listOf(
        "Alimentação", "Transporte", "Moradia", "Saúde", "Educação",
        "Lazer", "Compras", "Outros"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (tipoTransacao == TipoTransacao.DESPESA) "Nova Despesa" else "Nova Receita") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Valor
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = { Text("Ex: 100.50") }
            )
            
            // Categoria
            var categoriaExpandida by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoriaExpandida,
                onExpandedChange = { categoriaExpandida = it }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpandida) }
                )
                ExposedDropdownMenu(
                    expanded = categoriaExpandida,
                    onDismissRequest = { categoriaExpandida = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                categoriaExpandida = false
                            }
                        )
                    }
                }
            }
            
            // Data
            val dataFormatada = remember(dataSelecionada) {
                String.format(
                    "%02d/%02d/%04d",
                    dataSelecionada.get(Calendar.DAY_OF_MONTH),
                    dataSelecionada.get(Calendar.MONTH) + 1,
                    dataSelecionada.get(Calendar.YEAR)
                )
            }
            
            OutlinedTextField(
                value = dataFormatada,
                onValueChange = {},
                label = { Text("Data") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val novaData = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month)
                                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                dataSelecionada = novaData
                            },
                            dataSelecionada.get(Calendar.YEAR),
                            dataSelecionada.get(Calendar.MONTH),
                            dataSelecionada.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Selecionar data"
                        )
                    }
                }
            )
            
            // Recorrente (apenas para despesas)
            if (tipoTransacao == TipoTransacao.DESPESA) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Despesa Recorrente")
                    Switch(
                        checked = recorrente,
                        onCheckedChange = { recorrente = it }
                    )
                }
                
                if (recorrente) {
                    OutlinedTextField(
                        value = quantidadeParcelas,
                        onValueChange = { quantidadeParcelas = it },
                        label = { Text("Quantidade de Parcelas") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            
            // Observação
            OutlinedTextField(
                value = observacao,
                onValueChange = { observacao = it },
                label = { Text("Observação (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
            
            if (mostrarErro) {
                Text(
                    text = "Preencha todos os campos obrigatórios",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Botões
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                Button(
                    onClick = {
                        if (titulo.isBlank() || valor.isBlank() || categoria.isBlank()) {
                            mostrarErro = true
                        } else {
                            val valorDouble = valor.replace(",", ".").toDoubleOrNull() ?: 0.0
                            if (valorDouble > 0) {
                                val sucesso = onSalvar(
                                    titulo,
                                    valorDouble,
                                    categoria,
                                    dataSelecionada.timeInMillis,
                                    recorrente,
                                    quantidadeParcelas.toIntOrNull() ?: 1,
                                    observacao.ifBlank { null }
                                )
                                if (sucesso) {
                                    onCancelar()
                                }
                            } else {
                                mostrarErro = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}

