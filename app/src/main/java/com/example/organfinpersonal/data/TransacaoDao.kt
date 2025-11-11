package com.example.organfinpersonal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {
    @Query("SELECT * FROM transacoes ORDER BY data DESC")
    fun getAllTransacoes(): Flow<List<Transacao>>
    
    @Query("SELECT * FROM transacoes WHERE strftime('%Y-%m', datetime(data/1000, 'unixepoch')) = :mesAno ORDER BY data DESC")
    fun getTransacoesPorMesAno(mesAno: String): Flow<List<Transacao>>
    
    @Query("SELECT * FROM transacoes WHERE tipo = :tipo AND strftime('%Y-%m', datetime(data/1000, 'unixepoch')) = :mesAno")
    fun getTransacoesPorTipoEMesAno(tipo: String, mesAno: String): Flow<List<Transacao>>
    
    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'DESPESA' AND strftime('%Y-%m', datetime(data/1000, 'unixepoch')) = :mesAno")
    suspend fun getTotalDespesasPorMesAno(mesAno: String): Double?
    
    @Query("SELECT SUM(valor) FROM transacoes WHERE tipo = 'RECEITA' AND strftime('%Y-%m', datetime(data/1000, 'unixepoch')) = :mesAno")
    suspend fun getTotalReceitasPorMesAno(mesAno: String): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransacao(transacao: Transacao): Long
    
    @Update
    suspend fun updateTransacao(transacao: Transacao)
    
    @Delete
    suspend fun deleteTransacao(transacao: Transacao)
    
    @Query("SELECT * FROM transacoes WHERE recorrente = 1 AND parcelaAtual < quantidadeParcelas")
    suspend fun getTransacoesRecorrentesPendentes(): List<Transacao>
}

