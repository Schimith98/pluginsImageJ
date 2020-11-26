import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.ImagePlus;
import java.util.Arrays;

public class FiltrosNaoLineares implements PlugIn, DialogListener {
    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Filtros Nao Lineares");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = { "Mediana", "Maximo", "Moda" };
        interfaceGrafica.addRadioButtonGroup("Filtro de:", estrategia, 1, 3, "Mediana");
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                String s1 = interfaceGrafica.getNextRadioButton();

                ImagePlus image1 = IJ.getImage();
                ImageProcessor processor1 = image1.getProcessor();
                int width = processor1.getWidth();
                int height = processor1.getHeight();

                if (s1.equals("Mediana")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[] vetor = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            processor2.putPixel(i, j, processor1.getPixel(i, j));
                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {

                                vetor[0] = processor1.getPixel(i - 1, j - 1);
                                vetor[1] = processor1.getPixel(i - 1, j);
                                vetor[2] = processor1.getPixel(i - 1, j + 1);
                                vetor[3] = processor1.getPixel(i, j - 1);
                                vetor[4] = processor1.getPixel(i, j);
                                vetor[5] = processor1.getPixel(i, j + 1);
                                vetor[6] = processor1.getPixel(i + 1, j - 1);
                                vetor[7] = processor1.getPixel(i + 1, j);
                                vetor[8] = processor1.getPixel(i + 1, j + 1);

                                Arrays.sort(vetor);

                                processor2.putPixel(i, j, vetor[4]);
                            }

                        }
                    }

                    image2.show();
                }

                else if (s1.equals("Maximo")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[] vetor = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            processor2.putPixel(i, j, processor1.getPixel(i, j));
                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {

                                vetor[0] = processor1.getPixel(i - 1, j - 1);
                                vetor[1] = processor1.getPixel(i - 1, j);
                                vetor[2] = processor1.getPixel(i - 1, j + 1);
                                vetor[3] = processor1.getPixel(i, j - 1);
                                vetor[4] = processor1.getPixel(i, j);
                                vetor[5] = processor1.getPixel(i, j + 1);
                                vetor[6] = processor1.getPixel(i + 1, j - 1);
                                vetor[7] = processor1.getPixel(i + 1, j);
                                vetor[8] = processor1.getPixel(i + 1, j + 1);

                                Arrays.sort(vetor);

                                processor2.putPixel(i, j, vetor[8]);
                            }

                        }
                    }

                    image2.show();
                }

                else if (s1.equals("Moda")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[] vetor = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    int repet = 0;
                    int pixel = 0;
                    int aux = 0;

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            processor2.putPixel(i, j, processor1.getPixel(i, j));
                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {

                                vetor[0] = processor1.getPixel(i - 1, j - 1);
                                vetor[1] = processor1.getPixel(i - 1, j);
                                vetor[2] = processor1.getPixel(i - 1, j + 1);
                                vetor[3] = processor1.getPixel(i, j - 1);
                                vetor[4] = processor1.getPixel(i, j);
                                vetor[5] = processor1.getPixel(i, j + 1);
                                vetor[6] = processor1.getPixel(i + 1, j - 1);
                                vetor[7] = processor1.getPixel(i + 1, j);
                                vetor[8] = processor1.getPixel(i + 1, j + 1);

                                for (int k = 0; k < 9; k++) {
                                    repet = 0;
                                    for (int l = 0; l < 9; l++) {
                                        if (vetor[k] == vetor[l]) {
                                            repet++;
                                        }
                                    }
                                    if (repet >= aux) {
                                        aux = repet;
                                        pixel = vetor[k];
                                    }
                                }
                                aux = 0;
                                processor2.putPixel(i, j, pixel);
                            }

                        }
                    }
                    image2.show();
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
}