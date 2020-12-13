import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class RGBparaHSV implements PlugIn {

    public double hue(int[] pixel){
        double R = (double) pixel[0] / 255;
        double G = (double) pixel[1] / 255;
        double B = (double) pixel[2] / 255;

        //IJ.log("R =" + R);

        double H = 0;
        
        double MAX = R;
        double MIN = R;

        //atribuindo maior e menor valor do pixel RGB
        for(int i = 0; i < 3; i++){
            if( (double) pixel[i] / 255 > MAX){
                MAX = (double) pixel[i] / 255;
                //IJ.log("MAX =" + MAX);
            }
            if((double) pixel[i] / 255 < MIN){ 
                MIN = (double) pixel[i] / 255;
            }
        }

        // aplicando fórmula de conversão
        if(MAX == R){
            if(G >= B){
                H = 60 * ((G-B) / (MAX - MIN)) + 0;
            } else if (G < B){
                H = 60 * ((G-B) / (MAX - MIN)) + 360;
            }
        }

        if(MAX == G){
            H = 60 * ((B-R) / (MAX - MIN)) + 120;
        }
        if(MAX == B){
            H = 60 * ((R-G) / (MAX - MIN)) + 240;
        }
        //IJ.log("H =" + H);
        return H;
    }

    public double saturation(int[] pixel){
        double S = 0;

        double MAX = (double) pixel[0] / 255;
        double MIN = (double) pixel[0] / 255;

        //atribuindo maior e menor valor do pixel RGB
         for(int i = 0; i < 3; i++){
            if( (double) pixel[i] / 255 > MAX){
                MAX = (double) pixel[i] / 255;
                //IJ.log("MAX =" + MAX);
            }
            if((double) pixel[i] / 255 < MIN){ 
                MIN = (double) pixel[i] / 255;
            }
        }

        // aplicando fórmula de conversão
        if( MAX > 0){
            S = (MAX - MIN) / MAX;
        }
        if( MAX == 0){
            S = 0;
        }

        return S * 255;
    }

    public double brightness(int[] pixel){
        double MAX = (double) pixel[0] / 255;

        //atribuindo maior valor do pixel RGB
        for(int i = 0; i < 3; i++){
            if((double)pixel[i] / 255 > MAX){
                MAX = (double) pixel[i] / 255;
            }
        }
        return MAX * 255;
    }


	public void run(String arg) {
		ImagePlus image = IJ.getImage();
		ImageProcessor processor = image.getProcessor();
        int pixel[] = new int[3];
        
		ImagePlus imageHue = IJ.createImage("Hue", "8-bit", processor.getWidth(), processor.getHeight(), 1);
		ImageProcessor procHue = imageHue.getProcessor();
		ImagePlus imageSaturation = IJ.createImage("Saturation", "8-bit", processor.getWidth(), processor.getHeight(), 1);
		ImageProcessor procSaturation = imageSaturation.getProcessor();
		ImagePlus imageBrightness = IJ.createImage("Brightness", "8-bit", processor.getWidth(), processor.getHeight(), 1);
		ImageProcessor procBrightness = imageBrightness.getProcessor();

		for (int i = 0; i < processor.getWidth(); i++) {
			for (int j = 0; j < processor.getHeight(); j++) {
				pixel = processor.getPixel(i, j, pixel);

                procHue.putPixel(i, j, (int)hue(pixel) );
                procSaturation.putPixel(i, j, (int) saturation(pixel));
                procBrightness.putPixel(i, j, (int) brightness(pixel));
			}
        }
        
        imageHue.show();
        imageSaturation.show();
        imageBrightness.show();
		
	}

}