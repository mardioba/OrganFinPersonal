package com.example.organfinpersonal.repository

import com.example.organfinpersonal.data.*
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TransacaoRepository(private val transacaoDao: TransacaoDao) {
    
    fun getAllTransacoes(): Flow<List<Transacao>> = transacaoDao.getAllTransacoes()
    
    fun getTransacoesPorMesAno(mes: Int, ano: Int): Flow<List<Transacao>> {
        val mesAno = String.format("%04d-%02d", ano, mes)
        return transacaoDao.getTransacoesPorMesAno(mesAno)
    }
    
    suspend fun getTotalDespesasPorMesAno(mes: Int, ano: Int): Double {
        val mesAno = String.format("%04d-%02d", ano, mes)
        return transacaoDao.getTotalDespesasPorMesAno(mesAno) ?: 0.0
    }
    
    suspend fun getTotalReceitasPorMesAno(mes: Int, ano: Int): Double {
        val mesAno = String.format("%04d-%02d", ano, mes)
        return transacaoDao.getTotalReceitasPorMesAno(mesAno) ?: 0.0
    }
    
    suspend fun insertTransacao(transacao: Transacao): Long {
        val id = transacaoDao.insertTransacao(transacao)
        
        // Se for recorrente, criar próximas parcelas
        if (transacao.recorrente && transacao.parcelaAtual < transacao.quantidadeParcelas) {
            criarProximasParcelas(transacao)
        }
        
        return id
    }
    
    private suspend fun criarProximasParcelas(transacaoOriginal: Transacao) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transacaoOriginal.data
        
        for (i in 2..transacaoOriginal.quantidadeParcelas) {
            calendar.add(Calendar.MONTH, 1)
            
            val novaParcela = transacaoOriginal.copy(
                id = 0, // Nova transação
                data = calendar.timeInMillis,
                parcelaAtual = i
            )
            
            transacaoDao.insertTransacao(novaParcela)
        }
    }
    
    suspend fun updateTransacao(transacao: Transacao) {
        transacaoDao.updateTransacao(transacao)
    }
    
    suspend fun deleteTransacao(transacao: Transacao) {
        transacaoDao.deleteTransacao(transacao)
    }
    
    suspend fun getSaldoAtual(): Double {
        val calendar = Calendar.getInstance()
        val mes = calendar.get(Calendar.MONTH) + 1
        val ano = calendar.get(Calendar.YEAR)
        
        val receitas = getTotalReceitasPorMesAno(mes, ano)
        val despesas = getTotalDespesasPorMesAno(mes, ano)
        
        return receitas - despesas
    }
}

