import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.ImagePlus;
import ij.plugin.filter.Convolver;

public class FiltrosLineares implements PlugIn, DialogListener {
    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Exemplo de uso do Generic Dialog");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = { "passa-baixas de média", "passa-altas", "borda" };
        interfaceGrafica.addMessage("passa-baixas de média 1/10 * ({ 1, 1, 1 }, { 1, 2, 1 }, { 1, 1, 1 })");
        interfaceGrafica.addMessage("passa-altas ({ -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 })");
        interfaceGrafica.addMessage("realce de bordas NORTE ({ 1, 1, 1 }, { 1, -2, 1 }, { -1, -1, -1 })");
        interfaceGrafica.addRadioButtonGroup("Botões para escolher uma dentre várias estratégias", estrategia, 1, 3,
                "passa-baixas de média");
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                String s1 = interfaceGrafica.getNextRadioButton();

                ImagePlus image1 = IJ.getImage();
                ImageProcessor processor1 = image1.getProcessor();
                float pixel;
                int width = processor1.getWidth();
                int height = processor1.getHeight();

                if (s1.equals("passa-baixas de média")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[][] kernel = { { 1, 1, 1 }, { 1, 2, 1 }, { 1, 1, 1 } };
                    float[][] matriz = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            processor2.putPixel(i, j, processor1.getPixel(i, j));
                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {
                                pixel = 0;
                                matriz[0][0] = processor1.getPixel(i - 1, j - 1) * kernel[0][0];
                                matriz[0][1] = processor1.getPixel(i - 1, j) * kernel[0][1];
                                matriz[0][2] = processor1.getPixel(i - 1, j + 1) * kernel[0][2];
                                matriz[1][0] = processor1.getPixel(i, j - 1) * kernel[1][0];
                                matriz[1][1] = processor1.getPixel(i, j) * kernel[1][1];
                                matriz[1][2] = processor1.getPixel(i, j + 1) * kernel[1][2];
                                matriz[2][0] = processor1.getPixel(i + 1, j - 1) * kernel[2][0];
                                matriz[2][1] = processor1.getPixel(i + 1, j) * kernel[2][1];
                                matriz[2][2] = processor1.getPixel(i + 1, j + 1) * kernel[2][2];
                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        pixel += matriz[k][l] * 0.1;
                                    }
                                }
                                processor2.putPixel(i, j, (int) pixel);
                            }

                        }
                    }

                    image2.show();
                }

                else if (s1.equals("passa-altas")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[][] kernel = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };
                    float[][] matriz = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {

                            processor2.putPixel(i, j, processor1.getPixel(i, j));

                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {
                                pixel = 0;

                                matriz[0][0] = processor1.getPixel(i - 1, j - 1) * kernel[0][0];
                                matriz[0][1] = processor1.getPixel(i - 1, j) * kernel[0][1];
                                matriz[0][2] = processor1.getPixel(i - 1, j + 1) * kernel[0][2];
                                matriz[1][0] = processor1.getPixel(i, j - 1) * kernel[1][0];
                                matriz[1][1] = processor1.getPixel(i, j) * kernel[1][1];
                                matriz[1][2] = processor1.getPixel(i, j + 1) * kernel[1][2];
                                matriz[2][0] = processor1.getPixel(i + 1, j - 1) * kernel[2][0];
                                matriz[2][1] = processor1.getPixel(i + 1, j) * kernel[2][1];
                                matriz[2][2] = processor1.getPixel(i + 1, j + 1) * kernel[2][2];

                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        pixel += matriz[k][l];
                                    }
                                }

                                processor2.putPixel(i, j, (int) pixel);
                            }

                        }
                    }

                    image2.show();
                }

                else if (s1.equals("borda")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    int[][] kernel = { { 1, 1, 1 }, { 1, -2, 1 }, { -1, -1, -1 } }; // NORTE
                    float[][] matriz = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {

                            processor2.putPixel(i, j, processor1.getPixel(i, j));

                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {
                                pixel = 0;

                                matriz[0][0] = processor1.getPixel(i - 1, j - 1) * kernel[0][0];
                                matriz[0][1] = processor1.getPixel(i - 1, j) * kernel[0][1];
                                matriz[0][2] = processor1.getPixel(i - 1, j + 1) * kernel[0][2];
                                matriz[1][0] = processor1.getPixel(i, j - 1) * kernel[1][0];
                                matriz[1][1] = processor1.getPixel(i, j) * kernel[1][1];
                                matriz[1][2] = processor1.getPixel(i, j + 1) * kernel[1][2];
                                matriz[2][0] = processor1.getPixel(i + 1, j - 1) * kernel[2][0];
                                matriz[2][1] = processor1.getPixel(i + 1, j) * kernel[2][1];
                                matriz[2][2] = processor1.getPixel(i + 1, j + 1) * kernel[2][2];

                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        pixel += matriz[k][l];
                                    }
                                }

                                processor2.putPixel(i, j, (int) pixel);
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