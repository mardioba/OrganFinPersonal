package com.example.organfinpersonal.data

import androidx.room.TypeConverter

class TipoTransacaoConverter {
    @TypeConverter
    fun fromTipoTransacao(tipo: TipoTransacao): String {
        return tipo.name
    }
    
    @TypeConverter
    fun toTipoTransacao(tipo: String): TipoTransacao {
        return TipoTransacao.valueOf(tipo)
    }
}

