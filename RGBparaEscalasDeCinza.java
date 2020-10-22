import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.ImagePlus;

public class RGBparaEscalasDeCinza implements PlugIn, DialogListener {

    public int validar(int aux) {
        if (aux > 255) {
            return 255;
        }
        if (aux < 0) {
            return 0;
        }
        return aux;
    }

    public int average(int[] pixel) {
        int aux = (pixel[0] + pixel[1] + pixel[2]) / 3;
        return validar(aux);
    }

    public int analogLum(int[] pixel) {
        int aux = (int) ((0.299 * pixel[0]) + (0.587 * pixel[1]) + (0.114 * pixel[2]));
        return validar(aux);
    }

    public int digitalLum(int[] pixel) {
        int aux = (int) ((0.2125 * pixel[0]) + (0.7154 * pixel[1]) + (0.072 * pixel[2]));
        return validar(aux);
    }

    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Conversion to Gray Scale");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = { "Average", "Analog Luminance", "Digital Luminance" };
        interfaceGrafica.addMessage("Conversion to Gray Scale");
        interfaceGrafica.addRadioButtonGroup("Choose the strategy:", estrategia, 3, 1, "Average");
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                String s1 = interfaceGrafica.getNextRadioButton();

                ImagePlus imagem = IJ.getImage();
                ImageProcessor processador = imagem.getProcessor();
                int pixel[] = new int[3];

                if (s1.equals("Average")) {
                    ImagePlus cinza1 = IJ.createImage("Average", "8-bit", processador.getWidth(),
                            processador.getHeight(), 1);
                    ImageProcessor procCinza1 = cinza1.getProcessor();

                    for (int i = 0; i < processador.getWidth(); i++) {
                        for (int j = 0; j < processador.getHeight(); j++) {
                            pixel = processador.getPixel(i, j, pixel);
                            procCinza1.putPixel(i, j, average(pixel));
                        }
                    }
                    cinza1.show();
                }

                else if (s1.equals("Analog Luminance")) {
                    ImagePlus cinza2 = IJ.createImage("Analog Luminance", "8-bit", processador.getWidth(),
                            processador.getHeight(), 1);
                    ImageProcessor procCinza2 = cinza2.getProcessor();

                    for (int i = 0; i < processador.getWidth(); i++) {
                        for (int j = 0; j < processador.getHeight(); j++) {
                            pixel = processador.getPixel(i, j, pixel);
                            procCinza2.putPixel(i, j, analogLum(pixel));
                        }
                    }
                    cinza2.show();
                }

                else if (s1.equals("Digital Luminance")) {
                    ImagePlus cinza3 = IJ.createImage("Digital Luminance", "8-bit", processador.getWidth(),
                            processador.getHeight(), 1);
                    ImageProcessor procCinza3 = cinza3.getProcessor();

                    for (int i = 0; i < processador.getWidth(); i++) {
                        for (int j = 0; j < processador.getHeight(); j++) {
                            pixel = processador.getPixel(i, j, pixel);
                            procCinza3.putPixel(i, j, digitalLum(pixel));
                        }
                    }
                    cinza3.show();
                }
            }
        }
    }

    public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
        if (interfaceGrafica.wasCanceled())
            return false;
        return true;
    }
}
