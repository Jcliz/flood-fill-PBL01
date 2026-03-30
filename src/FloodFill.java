import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FloodFill {

    private static final int NEW_COLOR_R = 123;
    private static final int NEW_COLOR_G = 45;
    private static final int NEW_COLOR_B = 167;
    private static final Color NEW_COLOR = new Color(NEW_COLOR_R, NEW_COLOR_G, NEW_COLOR_B);

    private final BufferedImage image;
    private final ArrayList<BufferedImage> frames;

    //salva um frame a cada x pixels preenchidos
    private static final int FRAME_INTERVAL = 25;

    public FloodFill(String inputPath) throws IOException {
        this.image = ImageIO.read(new File(inputPath));
        this.frames = new ArrayList<>();
    }

    //implementação do algoritmo de preenchimento usando pilha
    public void fillComPilha(int startX, int startY) {
        int corFundo = image.getRGB(startX, startY);

        if (corFundo == NEW_COLOR.getRGB()) return;

        Stack<int[]> filaPrimariaExecucao = new Stack<>();
        int[] pixelSentinela = {startX, startY};
        filaPrimariaExecucao.push(pixelSentinela);

        int controleMagenta = 0;

        while (!filaPrimariaExecucao.isEmpty()) {
            int[] atual = filaPrimariaExecucao.pop();
            int x = atual[0];
            int y = atual[1];

            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) continue;
            if (image.getRGB(x, y) != corFundo) continue;

            image.setRGB(x, y, NEW_COLOR.getRGB());
            controleMagenta++;

            if (controleMagenta % FRAME_INTERVAL == 0) {
                synchronized (frames) {
                    frames.add(copiarImagem(image));
                }
            }

            filaPrimariaExecucao.push(new int[]{x + 1, y});
            filaPrimariaExecucao.push(new int[]{x - 1, y});
            filaPrimariaExecucao.push(new int[]{x, y + 1});
            filaPrimariaExecucao.push(new int[]{x, y - 1});
        }

        synchronized (frames) {
            frames.add(copiarImagem(image));
        }
    }

    //implementação do algoritmo de preenchimento usando fila
    public void fillComFila(int startX, int startY) {
        int corFundo = image.getRGB(startX, startY);

        if (corFundo == NEW_COLOR.getRGB()) return;

        Queue<int[]> filaPrimariaExecucao = new Queue<>();
        int[] pixelSentinela = {startX, startY};
        filaPrimariaExecucao.enqueue(pixelSentinela);

        int controleMagenta = 0;

        while (!filaPrimariaExecucao.isEmpty()) {
            int[] atual = filaPrimariaExecucao.dequeue();
            int x = atual[0];
            int y = atual[1];

            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) continue;
            if (image.getRGB(x, y) != corFundo) continue;

            image.setRGB(x, y, NEW_COLOR.getRGB());
            controleMagenta++;

            if (controleMagenta % FRAME_INTERVAL == 0) {
                synchronized (frames) {
                    frames.add(copiarImagem(image));
                }
            }

            filaPrimariaExecucao.enqueue(new int[]{x + 1, y});
            filaPrimariaExecucao.enqueue(new int[]{x - 1, y});
            filaPrimariaExecucao.enqueue(new int[]{x, y + 1});
            filaPrimariaExecucao.enqueue(new int[]{x, y - 1});
        }

        synchronized (frames) {
            frames.add(copiarImagem(image));
        }
    }

    //salva os frames gerados durante o preenchimento
    public void salvarFrames(String pastaFrames) throws IOException {
        File pasta = new File(pastaFrames);
        if (!pasta.exists()) pasta.mkdirs();

        for (int i = 0; i < frames.size(); i++) { 
            File frameFile = new File(pastaFrames + "/frame_" + String.format("%05d", i) + ".png");
            ImageIO.write(frames.get(i), "png", frameFile);
        }
        System.out.println("Total de frames salvos: " + frames.size());
    }

    public ArrayList<BufferedImage> getFrames() { // Retorna a lista de frames gerados durante o preenchimento
        return frames;
    }

    public BufferedImage getImagem() { // Retorna a imagem final após o preenchimento
        return image;
    }

    //método para criar uma cópia da imagem
    private BufferedImage copiarImagem(BufferedImage original) {
        BufferedImage copia = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                original.getType()
        );
        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) { 
                copia.setRGB(x, y, original.getRGB(x, y)); 
            }
        }
        return copia;
    }
}