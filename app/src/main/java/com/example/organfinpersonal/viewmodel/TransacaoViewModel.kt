package com.example.organfinpersonal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.organfinpersonal.data.TipoTransacao
import com.example.organfinpersonal.data.Transacao
import com.example.organfinpersonal.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class TransacaoViewModel(private val repository: TransacaoRepository) : ViewModel() {
    
    private val _transacoes = MutableStateFlow<List<Transacao>>(emptyList())
    val transacoes: StateFlow<List<Transacao>> = _transacoes.asStateFlow()
    
    private val _saldoAtual = MutableStateFlow(0.0)
    val saldoAtual: StateFlow<Double> = _saldoAtual.asStateFlow()
    
    private val _totalDespesas = MutableStateFlow(0.0)
    val totalDespesas: StateFlow<Double> = _totalDespesas.asStateFlow()
    
    private val _totalReceitas = MutableStateFlow(0.0)
    val totalReceitas: StateFlow<Double> = _totalReceitas.asStateFlow()
    
    private val _mesSelecionado = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    val mesSelecionado: StateFlow<Int> = _mesSelecionado.asStateFlow()
    
    private val _anoSelecionado = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val anoSelecionado: StateFlow<Int> = _anoSelecionado.asStateFlow()
    
    init {
        carregarTransacoes()
        atualizarSaldo()
    }
    
    fun carregarTransacoes() {
        viewModelScope.launch {
            repository.getTransacoesPorMesAno(_mesSelecionado.value, _anoSelecionado.value)
                .collect { lista ->
                    _transacoes.value = lista
                    atualizarTotais()
                }
        }
    }
    
    fun filtrarPorMesAno(mes: Int, ano: Int) {
        _mesSelecionado.value = mes
        _anoSelecionado.value = ano
        carregarTransacoes()
    }
    
    private suspend fun atualizarTotais() {
        _totalDespesas.value = repository.getTotalDespesasPorMesAno(
            _mesSelecionado.value,
            _anoSelecionado.value
        )
        _totalReceitas.value = repository.getTotalReceitasPorMesAno(
            _mesSelecionado.value,
            _anoSelecionado.value
        )
    }
    
    fun atualizarSaldo() {
        viewModelScope.launch {
            _saldoAtual.value = repository.getSaldoAtual()
        }
    }
    
    fun inserirTransacao(
        titulo: String,
        valor: Double,
        tipo: TipoTransacao,
        categoria: String,
        data: Long,
        recorrente: Boolean,
        quantidadeParcelas: Int,
        observacao: String?
    ): Boolean {
        return try {
            viewModelScope.launch {
                val transacao = Transacao(
                    titulo = titulo,
                    valor = valor,
                    tipo = tipo,
                    categoria = categoria,
                    data = data,
                    recorrente = recorrente,
                    quantidadeParcelas = quantidadeParcelas,
                    parcelaAtual = 1,
                    observacao = observacao
                )
                repository.insertTransacao(transacao)
                carregarTransacoes()
                atualizarSaldo()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun deletarTransacao(transacao: Transacao, incluirSerie: Boolean) {
        viewModelScope.launch {
            if (incluirSerie && transacao.recorrente) {
                repository.deleteSerieRecorrente(transacao)
            } else {
                repository.deleteTransacao(transacao)
            }
            carregarTransacoes()
            atualizarSaldo()
        }
    }
}

class TransacaoViewModelFactory(private val repository: TransacaoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransacaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransacaoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

