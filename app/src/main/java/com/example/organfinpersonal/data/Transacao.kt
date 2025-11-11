package com.example.organfinpersonal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val valor: Double,
    val tipo: TipoTransacao,
    val categoria: String,
    val data: Long, // Timestamp
    val recorrente: Boolean = false,
    val quantidadeParcelas: Int = 1,
    val parcelaAtual: Int = 1,
    val observacao: String? = null
)

enum class TipoTransacao {
    DESPESA,
    RECEITA
}

