import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class RGBparaCinza implements PlugIn {

	public int classico(int[] pixel) {
		int aux = (int) ( (pixel[0] * 0.30) + (pixel[1] * 0.59) + (pixel[2] * 0.11) );
		return aux;
	}

	public int mediaDeCanal(int[] pixel) {
		int aux = (pixel[0] + pixel[1] + pixel[2])/3; 
		return aux;
	}

	public int maximoCanal(int[] pixel) {
		int aux = -1;
		for(int i = 0; i < 3; i++){
			if(pixel[i] > aux){
				aux = pixel[i];
			}
		}
		return aux;
	}


	public void run(String arg) {
		ImagePlus imagem = IJ.getImage();
		ImageProcessor processador = imagem.getProcessor();
		int pixel[] = new int[3];

		ImagePlus cinza1 = IJ.createImage("Metodo Classico", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza1 = cinza1.getProcessor();
		ImagePlus cinza2 = IJ.createImage("Metodo Media de Canal", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza2 = cinza2.getProcessor();
		ImagePlus cinza3 = IJ.createImage("Metodo Maximo Canal", "8-bit", processador.getWidth(), processador.getHeight(), 1);
		ImageProcessor procCinza3 = cinza3.getProcessor();

		for (int i = 0; i < processador.getWidth(); i++) {
			for (int j = 0; j < processador.getHeight(); j++) {
				pixel = processador.getPixel(i, j, pixel);

				procCinza1.putPixel(i, j, classico(pixel));
				procCinza2.putPixel(i, j, mediaDeCanal(pixel));
				procCinza3.putPixel(i, j, maximoCanal(pixel));

			}
		}
		cinza1.show();
		cinza2.show();
		cinza3.show();
	}

}
