import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class PaintApp extends JPanel {

    private BufferedImage canvas;
    private final BufferedImage imagemOriginal;
    private final boolean usarFila;
    private int contadorFloodFill = 0;
    private boolean preenchendo = false;
    private int versaoExecucao = 0;

    private static final String IMAGEM_BASE_PNG = "imagem-base.png";

    //construtor que recebe a escolha do usuário sobre qual estrutura de dados usar (fila ou pilha)
    public PaintApp(boolean usarFila) {
        this.usarFila = usarFila;
        imagemOriginal = criarGrid();
        canvas = copiarImagem(imagemOriginal);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                executarFloodFill(e.getX(), e.getY());
            }
        });
    }

    //cria a imagem de fundo com formas geométricas
    private BufferedImage criarGrid() {
        try {
            BufferedImage imagemPng = ImageIO.read(new File(IMAGEM_BASE_PNG));

            if (imagemPng == null) {
                throw new IllegalArgumentException("Arquivo PNG inválido: " + IMAGEM_BASE_PNG);
            }

            return copiarImagem(imagemPng);
        } catch (Exception ex) {
            throw new RuntimeException("Nao foi possivel carregar a imagem base PNG na raiz: " + IMAGEM_BASE_PNG, ex);
        }
    }

    //cria uma cópia da imagem original para ser usada como canvas de desenho
    private BufferedImage copiarImagem(BufferedImage original) {
        BufferedImage copia = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copia.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return copia;
    }

    //método que executa o algoritmo de preenchimento quando o usuário clica em uma célula
    private void executarFloodFill(int x, int y) {
        if (preenchendo) return;
        preenchendo = true;
        int versaoAtual = ++versaoExecucao;
        String pastaFrames = "frames/fill_" + (++contadorFloodFill);

        try {
            File temp = File.createTempFile("canvas_", ".png");
            ImageIO.write(canvas, "png", temp);
            FloodFill ff = new FloodFill(temp.getAbsolutePath());

            Thread threadFloodFill = new Thread(() -> {
                try {
                    if (usarFila) ff.fillComFila(x, y);
                    else ff.fillComPilha(x, y);
                    ff.salvarFrames(pastaFrames);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            Thread threadRender = new Thread(() -> {
                ArrayList<BufferedImage> frames = ff.getFrames();
                int indiceAtual = 0;

                while (threadFloodFill.isAlive() || indiceAtual < frames.size()) {
                    BufferedImage frame = null;
                    synchronized (frames) {
                        if (indiceAtual < frames.size()) frame = frames.get(indiceAtual++);
                    }

                    if (frame != null) {
                        desenharNoCanvas(frame, versaoAtual);
                    } else {
                        try { Thread.sleep(5); }
                        catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
                    }
                }

                BufferedImage resultado = ff.getImagem();
                SwingUtilities.invokeLater(() -> {
                    if (versaoAtual != versaoExecucao) return;
                    desenharNoCanvas(resultado, versaoAtual);
                    preenchendo = false;
                    System.out.println("Fill #" + contadorFloodFill + " concluído. Frames salvos em: " + pastaFrames);
                });
            });

            threadFloodFill.start();
            threadRender.start();

        } catch (Exception ex) {
            if (versaoAtual == versaoExecucao) preenchendo = false;
            ex.printStackTrace();
        }
    }

    private void desenharNoCanvas(BufferedImage imagem, int versaoAtual) {
        SwingUtilities.invokeLater(() -> {
            if (versaoAtual != versaoExecucao) return;
            Graphics2D g = canvas.createGraphics();
            g.drawImage(imagem, 0, 0, null);
            g.dispose();
            repaint();
        });
    }

    //sobrescreve o método de pintura para desenhar o canvas atualizado
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    //método para limpar o canvas e resetar a imagem para o estado original
    public void limpar() {
        versaoExecucao++;
        preenchendo = false;
        canvas = copiarImagem(imagemOriginal);
        contadorFloodFill = 0;
        repaint();
    }

    //método para mostrar a janela de escolha entre fila e pilha antes de abrir o grid
    public static void mostrarEscolha() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Flood Fill");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Escolha a estrutura de dados:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btnFila = new JButton("Fila");
        JButton btnPilha = new JButton("Pilha");

        btnFila.setFont(new Font("Arial", Font.PLAIN, 14));
        btnPilha.setFont(new Font("Arial", Font.PLAIN, 14));
        btnFila.setPreferredSize(new Dimension(120, 50));
        btnPilha.setPreferredSize(new Dimension(120, 50));

        btnFila.addActionListener(e -> {
            dialog.dispose();
            abrirGrid(true);
        });

        btnPilha.addActionListener(e -> {
            dialog.dispose();
            abrirGrid(false);
        });

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoes.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        botoes.add(btnFila);
        botoes.add(btnPilha);

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    //método para abrir a janela principal do grid com base na escolha do usuário (fila ou pilha)
    public static void abrirGrid(boolean usarFila) {
        JFrame frame = new JFrame("Flood Fill - " + (usarFila ? "Fila" : "Pilha"));
        PaintApp app = new PaintApp(usarFila);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> app.limpar());

        JPanel toolbar = new JPanel();
        toolbar.add(new JLabel("Clique em uma célula para iniciar o Flood Fill  |  Estrutura: " + (usarFila ? "Fila" : "Pilha")));
        toolbar.add(btnLimpar);

        frame.setLayout(new BorderLayout());
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(app, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(PaintApp::mostrarEscolha);
    }
}