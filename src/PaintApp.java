import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class PaintApp extends JPanel {

    private BufferedImage canvas;
    private BufferedImage imagemOriginal;
    private boolean usarFila;
    private int contadorFloodFill = 0;

    private static final int LARGURA = 600;
    private static final int ALTURA = 600;
    private static final int CELULAS = 10;
    private static final int TAMANHO_CELULA = LARGURA / CELULAS;

    public PaintApp(boolean usarFila) { // Construtor que recebe a escolha do usuário sobre qual estrutura de dados usar (fila ou pilha)
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

    private BufferedImage criarGrid() { // Cria a imagem de fundo com a grade
        BufferedImage img = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, LARGURA, ALTURA);

        g.setColor(Color.BLACK);
        for (int i = 0; i <= CELULAS; i++) {
            int pos = i * TAMANHO_CELULA;
            g.drawLine(pos, 0, pos, ALTURA);
            g.drawLine(0, pos, LARGURA, pos); 
        }

        g.dispose();
        return img;
    }

    private BufferedImage copiarImagem(BufferedImage original) { // Cria uma cópia da imagem original para ser usada como canvas de desenho
        BufferedImage copia = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copia.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return copia;
    }

    private void executarFloodFill(int x, int y) { // Método que executa o algoritmo de preenchimento quando o usuário clica em uma célula
        contadorFloodFill++;
        String pastaFrames = "frames/fill_" + contadorFloodFill;

        try {
            File temp = File.createTempFile("canvas_", ".png");
            ImageIO.write(canvas, "png", temp);

            FloodFill ff = new FloodFill(temp.getAbsolutePath());

            if (usarFila) {
                ff.fillComFila(x, y);
            } else {
                ff.fillComPilha(x, y);
            }

            ff.salvarFrames(pastaFrames);

            BufferedImage resultado = ff.getImagem();
            Graphics2D g = canvas.createGraphics();
            g.drawImage(resultado, 0, 0, null);
            g.dispose();

            temp.delete();
            repaint();

            System.out.println("Fill #" + contadorFloodFill + " concluído. Frames salvos em: " + pastaFrames);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) { // Sobrescreve o método de pintura para desenhar o canvas atualizado
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() { // Define o tamanho preferido do painel para acomodar a imagem
        return new Dimension(LARGURA, ALTURA);
    }

    public void limpar() { // Método para limpar o canvas e resetar a imagem para o estado original
        canvas = copiarImagem(imagemOriginal);
        contadorFloodFill = 0;
        repaint();
    }

    public static void mostrarEscolha() { // Método para mostrar a janela de escolha entre fila e pilha antes de abrir o grid
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

    public static void abrirGrid(boolean usarFila) { // Método para abrir a janela principal do grid com base na escolha do usuário (fila ou pilha)
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
        SwingUtilities.invokeLater(() -> mostrarEscolha());
    }
}