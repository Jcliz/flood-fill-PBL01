# Flood Fill - PBL01 (Estrutura de Dados)

Este é um projeto desenvolvido em Java com interface gráfica (Swing/AWT) que implementa e visualiza o algoritmo de **Flood Fill** (frequentemente conhecido como a ferramenta do "balde de tinta" em editores de imagem). 

O projeto foi construído para explorar e demonstrar a diferença prática, visual e algorítmica entre buscas em largura (BFS) e buscas em profundidade (DFS) em matrizes de pixels, utilizando **estruturas de dados criadas do zero**, sem depender das coleções nativas do Java (`java.util.*`).

## O que o projeto faz?

- Ao ser executado, o programa pergunta qual estrutura de dados você deseja usar para o algoritmo: **Pilha (Stack)** ou **Fila (Queue)**.
- Uma imagem base (`imagem-base.png`) é carregada e exibida.
- Quando o usuário clica em qualquer área da imagem, a aplicação inicia o preenchimento de cor a partir daquele ponto através de uma **Thread em background**.
- A cor da área clicada é alterada para uma cor predefinida (por exemplo, magenta) respeitando uma tolerância de cor (para lidar com pequenas variações de tons na imagem).
- Conforme o algoritmo avança, ele captura **frames (quadros)** do processo e os renderiza na tela em tempo real, gerando uma animação impressionante do preenchimento acontecendo. Os frames também são salvos localmente na pasta `frames/`.

## Estrutura do Projeto

O projeto está organizado da seguinte forma:

```text
frames/       -> Diretório onde os quadros (imagens) do processo de preenchimento são salvos.
src/          -> Código-fonte Java do projeto.
  PaintApp.java    -> Classe principal, contém a interface gráfica, eventos de mouse e threads de animação.
  FloodFill.java   -> Lógica principal do algoritmo de preenchimento (Queue vs Stack).
  ArrayList.java   -> Implementação customizada de uma Lista baseada em arrays dinâmicos (guarda os frames da animação).
  Node.java        -> Classe de nó genérica usada pela Pilha e Fila.
  Queue.java       -> Implementação customizada de uma Fila (FIFO). Utilizada na busca em largura (BFS).
  Stack.java       -> Implementação customizada de uma Pilha (LIFO). Utilizada na busca em profundidade (DFS).
```

## Como o Algoritmo Funciona (Fila vs Pilha)

O algoritmo examina a cor do pixel clicado e varre os vizinhos (cima, baixo, esquerda, direita) em busca de pixels com cores semelhantes. A ordem em que esses vizinhos são processados muda drasticamente dependendo da estrutura de dados escolhida:

1. **Preenchimento com Fila (`fillComFila`):**
   - Utiliza a estrutura **Fila (Queue)**.
   - Realiza uma **Busca em Largura (BFS - Breadth-First Search)**.
   - **Efeito visual:** O preenchimento se expande de forma circular/radial, crescendo uniformemente a partir do centro em todas as direções, pois os vizinhos mais próximos são processados primeiro (FIFO).

2. **Preenchimento com Pilha (`fillComPilha`):**
   - Utiliza a estrutura **Pilha (Stack)**.
   - Realiza uma **Busca em Profundidade (DFS - Depth-First Search)**.
   - **Efeito visual:** O preenchimento viaja o mais longe possível em uma direção (ex: seguindo bordas ou linhas de forma imprevisível) antes de retroceder, mudando os caminhos rapidamente. Ele não expande uniformemente, explorando "caminhos" profundos primeiro (LIFO).

## Como Executar

1. Certifique-se de que você possui o Java/JDK instalado em sua máquina.
2. Compile os arquivos `.java` localizados na pasta `src/`.
3. Garanta que a imagem `imagem-base.png` está no local correto esperado pelo `PaintApp`.
4. Execute a classe principal `PaintApp.java`.
5. Selecione a estrutura desejada (Fila ou Pilha), espere abrir a interface com a imagem, clique na região que deseja pintar, e observe a animação do preenchimento!
