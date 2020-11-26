import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.ImagePlus;

public class TecnicasDeMorfologia implements PlugIn, DialogListener {
    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public void dilatacao(ImageProcessor processor1, ImageProcessor processor2) {
        int width = processor1.getWidth();
        int height = processor1.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                processor2.putPixel(i, j, processor1.getPixel(i, j));
                if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {

                    if (processor1.getPixel(i, j) == 0) {
                        processor2.putPixel(i - 1, j - 1, 0);
                        processor2.putPixel(i - 1, j, 0);
                        processor2.putPixel(i - 1, j + 1, 0);
                        processor2.putPixel(i, j - 1, 0);
                        processor2.putPixel(i, j, 0);
                        processor2.putPixel(i, j + 1, 0);
                        processor2.putPixel(i + 1, j - 1, 0);
                        processor2.putPixel(i + 1, j, 0);
                        processor2.putPixel(i + 1, j + 1, 0);
                    }
                }

            }
        }
    }

    public void erosao(ImageProcessor processor1, ImageProcessor processor2) {
        int width = processor1.getWidth();
        int height = processor1.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                processor2.putPixel(i, j, processor1.getPixel(i, j));
                if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {

                    if (processor1.getPixel(i - 1, j) == 0 && processor1.getPixel(i, j - 1) == 0
                            && processor1.getPixel(i, j) == 0 && processor1.getPixel(i, j + 1) == 0
                            && processor1.getPixel(i + 1, j) == 0) {
                        processor2.putPixel(i, j, 0);
                    } else {
                        processor2.putPixel(i, j, 255);
                    }
                }

            }
        }
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Técnicas de Morfologia");
        interfaceGrafica.addDialogListener(this);

        String[] estrategia = { "Dilatacao", "Erosao", "Fechamento", "Abertura", "Borda" };
        interfaceGrafica.addRadioButtonGroup("Botoes para escolher uma dentre varias estrategias", estrategia, 1, 5,
                "Dilatacao");
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

                if (s1.equals("Dilatacao")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    dilatacao(processor1, processor2);

                    image2.show();
                }

                else if (s1.equals("Erosao")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    erosao(processor1, processor2);

                    image2.show();
                }

                else if (s1.equals("Fechamento")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    dilatacao(processor1, processor2);

                    ImagePlus image3 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor3 = image3.getProcessor();

                    erosao(processor2, processor3);

                    image3.show();
                } else if (s1.equals("Abertura")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    erosao(processor1, processor2);

                    ImagePlus image3 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor3 = image3.getProcessor();

                    dilatacao(processor2, processor3);

                    image3.show();
                } else if (s1.equals("Borda")) {
                    ImagePlus image2 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor2 = image2.getProcessor();

                    erosao(processor1, processor2);

                    ImagePlus image3 = IJ.createImage("result", "8-bit", width, height, 1);
                    ImageProcessor processor3 = image3.getProcessor();

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            processor3.putPixel(i, j, processor1.getPixel(i, j));
                            if (i != 0 && j != 0 && i != width - 1 && j != height - 1) {
                                if (processor2.getPixel(i, j) == 0) {
                                    processor3.putPixel(i, j, 255);
                                }
                            }
                        }
                    }
                    image3.show();
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