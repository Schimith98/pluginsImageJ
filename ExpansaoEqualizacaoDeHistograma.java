import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class ExpansaoEqualizacaoDeHistograma implements PlugIn, DialogListener {

    public void run(String arg) {
        apresentarInterfaceGrafica();
    }

    public int high(ImageProcessor processor) {
        int pixel;
        int maior = 0;
        for (int i = 0; i < processor.getWidth(); i++) {
            for (int j = 0; j < processor.getHeight(); j++) {
                pixel = processor.getPixel(i, j);
                if (pixel > maior) {
                    maior = pixel;
                }
            }
        }
        return maior;
    }

    public int low(ImageProcessor processor) {
        int pixel;
        int menor = 255;
        for (int i = 0; i < processor.getWidth(); i++) {
            for (int j = 0; j < processor.getHeight(); j++) {
                pixel = processor.getPixel(i, j);
                if (pixel < menor) {
                    menor = pixel;
                }
            }
        }
        return menor;
    }

    public void apresentarInterfaceGrafica() {
        GenericDialog interfaceGrafica = new GenericDialog("Histogram: Expansion and Equalize");
        interfaceGrafica.addDialogListener(this);

        interfaceGrafica.addMessage("Image Adjust:");
        interfaceGrafica.addCheckbox("histogram expansion", false);
        interfaceGrafica.addCheckbox("histogram equalizate", false);
        interfaceGrafica.showDialog();

        if (interfaceGrafica.wasCanceled()) {
            IJ.showMessage("PlugIn cancelado!");
        } else {
            if (interfaceGrafica.wasOKed()) {
                ImagePlus image = IJ.getImage();
                ImageProcessor processor = image.getProcessor();

                // Expansao
                if (interfaceGrafica.getNextBoolean() == true) {
                    int pixel;
                    int pixelHigh = high(processor);
                    int pixelLow = low(processor);
                    int max = 255;
                    int min = 0;

                    for (int i = 0; i < processor.getWidth(); i++) {
                        for (int j = 0; j < processor.getHeight(); j++) {
                            pixel = processor.getPixel(i, j);

                            pixel = min + (pixel - pixelLow) * ((max - min) / (pixelHigh - pixelLow));

                            processor.putPixel(i, j, pixel);

                        }
                    }
                    image.updateAndDraw();
                }

                // Equalizacao
                if (interfaceGrafica.getNextBoolean() == true) {
                    int[] pixelIntensities = new int[256];
                    int[] noPixel = new int[256];
                    double totalNoPixel = processor.getWidth() * processor.getHeight();
                    double[] probability = new double[256];
                    double[] cumulativeProbability = new double[256];
                    double[] cumulativeProbabilityRange = new double[256];
                    int[] newIntensity = new int[256];
                    int range = 128;

                    //preenchendo Pixel Intensities
                    for (int i = 0; i < 256; i++) {
                        pixelIntensities[i] = i;
                    }

                    // preenchendo no. of pixels
                    for (int aux = 0; aux < 256; aux++){
                        noPixel[aux] = 0;
                        for (int i = 0; i < processor.getWidth(); i++) {
                            for (int j = 0; j < processor.getHeight(); j++) {
                                if(processor.getPixel(i, j) == pixelIntensities[aux]){
                                    noPixel[aux]++;
                                }
                            }
                        }
                    }

                    //preenchendo probabilidade e cumulativo
                    for(int i = 0; i < 256; i++){
                        probability[i] = (double) noPixel[i] / totalNoPixel;
                        //IJ.log("probability[" + i + "] = " + probability[i] );
                        if(i == 0){
                            cumulativeProbability[i] = cumulativeProbability[i] + probability[i];
                        }else{
                            cumulativeProbability[i] = cumulativeProbability[i-1] + probability[i];
                        }

                        cumulativeProbabilityRange[i] = cumulativeProbability[i] * range; 
                        
                        newIntensity[i] = (int) Math.floor(cumulativeProbabilityRange[i]);
                        //IJ.log("Cumulativeprobabda[" + i + "] = " + newIntensity[i] );

                    }
                    
                    for (int aux = 0; aux < 256; aux++){
                        for (int i = 0; i < processor.getWidth(); i++) {
                            for (int j = 0; j < processor.getHeight(); j++) {
                                if(processor.getPixel(i, j) == pixelIntensities[aux]){
                                    processor.putPixel(i,j,newIntensity[aux]);
                                }
                            }
                        }
                    }

                    image.updateAndDraw();
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