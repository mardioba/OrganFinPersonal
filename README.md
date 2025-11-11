# 💰 OrganFin Personal

Um aplicativo Android nativo desenvolvido em Kotlin para organização financeira pessoal. Gerencie suas despesas e receitas de forma simples e intuitiva, com suporte a transações recorrentes e relatórios mensais.

## 📱 Sobre o Projeto

O **OrganFin Personal** é um organizador financeiro pessoal que permite controlar suas finanças de forma eficiente. Com uma interface moderna e intuitiva baseada em Material Design 3, o aplicativo oferece todas as ferramentas necessárias para gerenciar seu orçamento mensal.

## ✨ Funcionalidades

### 📝 Cadastro de Transações
- **Despesas e Receitas**: Cadastre facilmente suas transações financeiras
- **Campos disponíveis**:
  - Título
  - Valor
  - Categoria (Alimentação, Transporte, Moradia, Saúde, Educação, Lazer, Compras, Outros)
  - Data
  - Tipo (Despesa ou Receita)
  - Observação (opcional)

### 🔄 Despesas Recorrentes
- Marque despesas como recorrentes
- Defina a quantidade de parcelas
- Geração automática de parcelas mensais
- Controle de parcelas atuais e totais

### 📊 Listagem e Filtros
- Visualize todas as transações em uma lista organizada
- Filtre por mês e ano
- Visualização clara com cores diferenciadas para despesas e receitas
- Exibição de informações de parcelas recorrentes

### 📈 Relatórios Mensais
- **Relatório consolidado** com:
  - Total de despesas do mês
  - Total de receitas do mês
  - Saldo final calculado automaticamente
- Filtros por mês/ano
- Exportação em PDF para compartilhamento

### 🎨 Interface Moderna
- Material Design 3 (MD3)
- Suporte a tema claro e escuro automático
- Layout intuitivo e responsivo
- Feedback visual com Snackbars
- Ícones representativos para cada tipo de transação

### 💾 Persistência de Dados
- Banco de dados local com Room
- Armazenamento seguro e eficiente
- Sincronização automática de dados

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **Banco de Dados**: Room Database
- **Navegação**: Navigation Compose
- **Coroutines**: Para operações assíncronas
- **Material Design 3**: Para interface moderna

## 📋 Requisitos

- Android Studio Hedgehog ou superior
- Android SDK 24 (Android 7.0) ou superior
- Kotlin 2.0.21 ou superior
- Gradle 8.13.1 ou superior

## 🚀 Instalação

### Pré-requisitos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/OrganFinPersonal.git
```

2. Abra o projeto no Android Studio

3. Sincronize o Gradle (o Android Studio fará isso automaticamente)

4. Execute o aplicativo em um dispositivo ou emulador Android

### Build do Projeto

```bash
./gradlew assembleDebug
```

### Instalação no Dispositivo

```bash
./gradlew installDebug
```

## 📁 Estrutura do Projeto

```
app/src/main/java/com/example/organfinpersonal/
├── data/
│   ├── Transacao.kt              # Entidade de dados
│   ├── TransacaoDao.kt           # Data Access Object
│   ├── AppDatabase.kt            # Configuração do Room Database
│   └── TipoTransacaoConverter.kt # Conversor de tipos para Room
├── repository/
│   └── TransacaoRepository.kt    # Camada de repositório
├── viewmodel/
│   └── TransacaoViewModel.kt    # ViewModel (MVVM)
├── ui/screens/
│   ├── HomeScreen.kt             # Tela principal
│   ├── CadastroTransacaoScreen.kt # Tela de cadastro
│   ├── ListaTransacoesScreen.kt  # Tela de listagem
│   └── RelatorioScreen.kt        # Tela de relatórios
├── navigation/
│   └── NavGraph.kt               # Configuração de navegação
├── util/
│   └── PdfExporter.kt            # Utilitário para exportação PDF
└── MainActivity.kt               # Activity principal
```

## 🎯 Como Usar

### Cadastrar uma Nova Transação

1. Na tela principal, toque em **"Nova Despesa"** ou **"Nova Receita"**
2. Preencha os campos obrigatórios:
   - Título
   - Valor
   - Categoria
3. (Opcional) Para despesas, marque como recorrente e defina o número de parcelas
4. Toque em **"Salvar"**

### Visualizar Transações

1. Na tela principal, toque em **"Transações"**
2. Use os filtros de mês e ano para visualizar transações específicas
3. As transações são exibidas com cores diferentes:
   - 🟢 Verde: Receitas
   - 🔴 Vermelho: Despesas

### Gerar Relatório Mensal

1. Na tela principal, toque em **"Relatório"**
2. Selecione o mês e ano desejados
3. Visualize o resumo financeiro:
   - Total de despesas
   - Total de receitas
   - Saldo final
4. Toque no ícone de PDF para exportar o relatório

### Despesas Recorrentes

1. Ao cadastrar uma despesa, marque a opção **"Despesa Recorrente"**
2. Defina a quantidade de parcelas
3. O sistema criará automaticamente todas as parcelas mensais
4. Cada parcela será exibida na lista com sua numeração (ex: "Parcela 2/12")

## 🎨 Tema Claro e Escuro

O aplicativo suporta automaticamente tema claro e escuro baseado nas configurações do sistema. O Material Design 3 garante uma experiência visual consistente em ambos os temas.

## 📦 Dependências Principais

```gradle
// Room Database
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.room:room-ktx:2.6.1"
ksp "androidx.room:room-compiler:2.6.1"

// ViewModel
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4"

// Navigation
implementation "androidx.navigation:navigation-compose:2.8.4"

// Jetpack Compose
implementation platform("androidx.compose:compose-bom:2024.09.00")
implementation "androidx.compose.ui:ui"
implementation "androidx.compose.material3:material3"
```

## 🔧 Configuração do Ambiente

1. Certifique-se de ter o Android Studio instalado
2. Instale o SDK do Android (mínimo API 24)
3. Configure o Kotlin plugin
4. Sincronize as dependências do Gradle

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer um Fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abrir um Pull Request

## 📧 Contato

Para dúvidas ou sugestões, abra uma issue no repositório.

## 🗺️ Roadmap

- [ ] Adicionar gráficos e visualizações
- [ ] Suporte a múltiplas moedas
- [ ] Backup na nuvem
- [ ] Categorias personalizáveis
- [ ] Notificações para despesas recorrentes
- [ ] Modo offline completo
- [ ] Exportação em outros formatos (CSV, Excel)

## 🙏 Agradecimentos

- Material Design 3 pela interface moderna
- Jetpack Compose pela experiência de desenvolvimento
- Comunidade Android pelo suporte contínuo

---

Desenvolvido com ❤️ usando Kotlin e Jetpack Compose

