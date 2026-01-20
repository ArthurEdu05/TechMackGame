# Caos na Estrada 🚚💨

**Caos na Estrada** é um jogo desenvolvido em **Java** com o framework **libGDX**, projetado para auxiliar pessoas que estão em processo de reabilitação motora. O jogo transforma o exercício físico em uma experiência gamificada e engajadora, integrando-se a dispositivos de IoT para converter pedaladas de uma bicicleta ergométrica em comandos dentro do jogo.

## 💡 O Conceito

Muitas atividades terapêuticas de reabilitação física são repetitivas e cansativas. **Caos na Estrada** busca quebrar essa barreira utilizando a gamificação para aumentar o engajamento do paciente/jogador, focando no fortalecimento das pernas, coordenação motora e tempo de reação.

## 📖 A História
Você está de mudança, mas algo deu errado: a tampa do caminhão abriu no meio da estrada! Agora, seus pertences (roupas, móveis e objetos diversos) estão caindo um a um. Sua missão é correr atrás do caminhão e recuperar tudo antes que os itens se quebrem ou se percam na rodovia.

## ⚙️ Mecânicas e Integração IoT

O diferencial técnico do projeto é a substituição dos controles convencionais pelo movimento físico real:

- **Sistema de Energia por Pedalada:** O personagem principal acumula energia apenas quando o usuário pedala na bicicleta.
- **Velocidade Dinâmica:** Quanto mais rápido e consistente você pedala, mais energia acumula para alcançar o caminhão. Se parar de pedalar, a energia cai e o personagem perde velocidade podendo até ficar parado caso a energia acabe.
- **Simulação de Input:** Para fins de teste e portabilidade, o jogo possui uma lógica de input adaptável, permitindo simular o pedal através das teclas `A` e `D` que recarrega a energia do personagem.

## 🎮 Estrutura do Jogo

O projeto conta com **3 fases progressivas**, onde a precisão e o tempo de reação são fundamentais para o sucesso:

1.  **Nível Fácil:** O caminhão anda devagar, dando mais tempo para o jogador reagir e coletar os objetos.
2.  **Nível Médio:** O caminhão aumenta a velocidade, o volume de objetos a serem resgatados é maior e novos obstáculos surgem na pista.
3.  **Nível Difícil:** Velocidade máxima! O caminhão se distancia rapidamente e a câmera muda de ângulo para desafiar os reflexos dos jogadores mais avançados.

### Exemplos de Jogada:
- **Arco Menor:** Objeto cai próximo ao caminhão; o jogador deve acelerar rapidamente.
- **Arco Maior:** Objeto cai longe do caminhão; o jogador deve desacelerar para coletar o item no ar sem deixá-lo cair.
- **Recarregar energia:** A energia do personagem cai progressivamente; exige esforço nas pedaladas para recarregamento da energia.

## 🏆 Condições de Jogo

Para avançar nas fases e completar o jogo, o jogador deve estar atento ao sistema de pontuação e às metas estabelecidas.

### Sistema de Pontuação
O jogador inicia cada nível com uma pontuação de **0 pontos**. A pontuação é dinâmica e varia de acordo com os itens:
- **Ganho de pontos:** O jogador recebe pontos ao coletar os objetos que caem do caminhão antes que atinjam o chão.
- **Perda de pontos:** Cada objeto que atinge o chão resulta em uma penalidade na pontuação atual.

### Vitória 🥇
Cada nível possui uma **meta de pontos específica**. 
- Ao atingir o objetivo da fase, o jogador vence o desafio e desbloqueia o acesso ao próximo nível.

### Derrota (Game Over) 🛑
O jogo exige constância e precisão desde o primeiro segundo.
- **Pontuação Negativa:** Caso a pontuação do jogador fique abaixo de 0 ($pontos < 0$), a partida é encerrada imediatamente e a tela de **Game Over** é exibida. 
- Como o jogo inicia com a pontuação zerada, deixar os primeiros objetos caírem resulta em derrota instantânea, incentivando o foco imediato no exercício.

## 🛠️ Tecnologias e Ferramentas

- Java & [libGDX](https://libgdx.com/)
- **Prototipagem de cenários:** Figma
- **Assets:** Design visual de objetos 100% originais, criado com Gemini e Nano Banana Pro.
- **Hardware:** Possibilidade com integração futura de sensores de pedalada para coleta de inputs em tempo real.

## 🚀 Como Jogar

###  **Controles:** 
- Use as teclas `A` e `D` para simular as pedaladas caso não esteja conectado ao hardware de IoT. A alternância entre as teclas faz com que o personagem recarregue suas energias.
- Use as setas `<-` e `->` para fazer com que o personagem se movimente para frente ou para trás.
- Use o **mouse** para navegar entre as funcionalidades e botões disponibilizados dentro do jogo.

## 🎓 Contexto Acadêmico

Este projeto foi desenvolvido para atender o **Projeto Final da matéria de Jogos Digitais**, com foco em aplicar os conceitos estudados durante a aula.
