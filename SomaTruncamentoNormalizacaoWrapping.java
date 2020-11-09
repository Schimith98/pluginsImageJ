import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.WindowManager;
import ij.process.ImageProcessor;
import ij.ImagePlus;

public class SomaTruncamentoNormalizacaoWrapping implements PlugIn, DialogListener {
    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Adição de imagens");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = { "Truncamento", "Normalizacao", "Wrapping" };
        interfaceGrafica.addMessage("Adição de imagens");
        interfaceGrafica.addRadioButtonGroup("Escolha a estrategia:", estrategia, 1, 3, "Estratégia 2");
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                String s1 = interfaceGrafica.getNextRadioButton();

                ImagePlus imagem1 = IJ.getImage();
                ImageProcessor processor1 = imagem1.getProcessor();
                WindowManager.putBehind();
                ImagePlus imagem2 = IJ.getImage();
                ImageProcessor processor2 = imagem2.getProcessor();

                // verifica se as imagens são do mesmo tamanho
                if (processor1.getWidth() == processor2.getWidth()
                        && processor1.getHeight() == processor2.getHeight()) {
                    ImagePlus imagem3 = IJ.createImage("Result", "8-bit", processor1.getWidth(), processor1.getHeight(),
                            1);
                    ImageProcessor processor3 = imagem3.getProcessor();

                    if (s1.equals("Truncamento")) {
                        int pixel;
                        for (int i = 0; i < processor3.getWidth(); i++) {
                            for (int j = 0; j < processor3.getHeight(); j++) {

                                pixel = processor1.getPixel(i, j) + processor2.getPixel(i, j);
                                processor3.putPixel(i, j, truncamento(pixel));
                            }
                        }
                    } else if (s1.equals("Normalizacao")) {
                        int[][] matrizAux = new int[processor3.getWidth()][processor3.getHeight()];
                        int fMax = 0;
                        int fMin = 510;

                        for (int i = 0; i < processor3.getWidth(); i++) {
                            for (int j = 0; j < processor3.getHeight(); j++) {
                                matrizAux[i][j] = processor1.getPixel(i, j) + processor2.getPixel(i, j);
                                if (matrizAux[i][j] > fMax) {
                                    fMax = matrizAux[i][j];
                                }
                                if (matrizAux[i][j] < fMin) {
                                    fMin = matrizAux[i][j];
                                }
                            }
                        }
                        IJ.log("maiorpixel" + fMax);
                        IJ.log("menorPixel:" + fMin);
                        for (int i = 0; i < processor3.getWidth(); i++) {
                            for (int j = 0; j < processor3.getHeight(); j++) {
                                processor3.putPixel(i, j, normalizacao(matrizAux[i][j], fMax, fMin));
                            }
                        }
                    } else if (s1.equals("Wrapping")) {
                        int pixel;
                        for (int i = 0; i < processor3.getWidth(); i++) {
                            for (int j = 0; j < processor3.getHeight(); j++) {

                                pixel = processor1.getPixel(i, j) + processor2.getPixel(i, j);
                                processor3.putPixel(i, j, wrapping(pixel));
                            }
                        }
                    }
                    imagem3.show();
                }
                else{
                    IJ.showMessage("As imagens devem ter o mesmo tamanho!");
                }
                IJ.showMessage("Plugin encerrado com sucesso!");
            }
        }
    }

    @Override
    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
        if (interfaceGrafica.wasCanceled())
            return false;
        return true;
    }

    int truncamento(int pixel) {
        if (pixel > 255) {
            return 255;
        }
        if (pixel < 0) {
            return 0;
        }
        return pixel;
    }

    int normalizacao(int pixel, int fMax, int fMin) {
        pixel = (int) ((510 / (fMax - fMin)) * (pixel - fMin));
        //IJ.log("normalizacao:" + pixel);
        return pixel;
    }

    int wrapping(int pixel) {
        if (pixel > 255) {
            pixel = pixel - 255;
        } else if (pixel < 0) {
            pixel = pixel + 255;
        }
        return pixel;
    }
}