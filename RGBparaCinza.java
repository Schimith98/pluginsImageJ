import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class RGBparaCinza implements PlugIn {

	public int R(int[] pixel) {
		int aux = pixel[0];
		return aux;
	}

	public int G(int[] pixel) {
		int aux = pixel[1];
		return aux;
	}

	public int B(int[] pixel) {
		int aux = pixel[2];
		return aux;
	}


	public void run(String arg) {
		ImagePlus imagem = IJ.getImage();
		ImageProcessor processador = imagem.getProcessor();
		int pixel[] = new int[3];

		ImagePlus cinza1 = IJ.createImage("Red", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza1 = cinza1.getProcessor();
		ImagePlus cinza2 = IJ.createImage("Green", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza2 = cinza2.getProcessor();
		ImagePlus cinza3 = IJ.createImage("Blue", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza3 = cinza3.getProcessor();

		for (int i = 0; i < processador.getWidth(); i++) {
			for (int j = 0; j < processador.getHeight(); j++) {
				pixel = processador.getPixel(i, j, pixel);

				procCinza1.putPixel(i, j, R(pixel));
				procCinza2.putPixel(i, j, G(pixel));
				procCinza3.putPixel(i, j, B(pixel));

			}
		}
		cinza1.show();
		cinza2.show();
		cinza3.show();
	}

}
