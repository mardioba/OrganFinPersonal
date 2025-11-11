package com.example.organfinpersonal.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object PdfExporter {
    
    fun exportarRelatorio(
        context: Context,
        mes: Int,
        ano: Int,
        totalDespesas: Double,
        totalReceitas: Double,
        saldoFinal: Double
    ) {
        try {
            val meses = listOf(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            )
            
            val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val nomeArquivo = "Relatorio_${meses[mes - 1]}_$ano.pdf"
            
            val file = File(context.getExternalFilesDir(null), nomeArquivo)
            val outputStream = FileOutputStream(file)
            
            // Criar conteúdo do PDF (simplificado - em produção, usar biblioteca como iText ou PdfDocument)
            val conteudo = buildString {
                appendLine("RELATÓRIO MENSAL - ORGANFIN PERSONAL")
                appendLine("=".repeat(50))
                appendLine()
                appendLine("Período: ${meses[mes - 1]} de $ano")
                appendLine()
                appendLine("Total de Despesas: ${formatoMoeda.format(totalDespesas)}")
                appendLine("Total de Receitas: ${formatoMoeda.format(totalReceitas)}")
                appendLine("Saldo Final: ${formatoMoeda.format(saldoFinal)}")
                appendLine()
                appendLine("Gerado em: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date())}")
            }
            
            outputStream.write(conteudo.toByteArray())
            outputStream.close()
            
            // Compartilhar arquivo
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Relatório Mensal - ${meses[mes - 1]}/$ano")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(shareIntent, "Exportar PDF"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

