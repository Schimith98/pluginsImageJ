import ij.plugin.PlugIn;
import ij.IJ;
import ij.WindowManager;
import ij.process.ImageProcessor;
import ij.ImagePlus;

public class OperacaoMaximo implements PlugIn {
    public void run(String arg) {
        ImagePlus imagem1 = IJ.getImage();
        ImageProcessor processor1 = imagem1.getProcessor();
        WindowManager.putBehind();
        ImagePlus imagem2 = IJ.getImage();
        ImageProcessor processor2 = imagem2.getProcessor();

        // verifica se as imagens são do mesmo tamanho
        if (processor1.getWidth() == processor2.getWidth() && processor1.getHeight() == processor2.getHeight()) {
            ImagePlus imagem3 = IJ.createImage("Result", "8-bit", processor1.getWidth(), processor1.getHeight(), 1);
            ImageProcessor processor3 = imagem3.getProcessor();

            int pixel;
            for (int i = 0; i < processor3.getWidth(); i++) {
                for (int j = 0; j < processor3.getHeight(); j++) {
                    pixel = processor1.getPixel(i, j);
                    if (processor1.getPixel(i, j) < processor2.getPixel(i, j))
                        pixel = processor2.getPixel(i, j);

                    processor3.putPixel(i, j, pixel);
                }
            }
            imagem3.show();
            IJ.showMessage("PlugIn encerrado com sucesso");
        } else {
            IJ.showMessage("As imagens devem ter o mesmo tamanho!");
        }
    }

}