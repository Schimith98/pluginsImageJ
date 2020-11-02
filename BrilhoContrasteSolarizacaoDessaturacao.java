import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class BrilhoContrasteSolarizacaoDessaturacao implements PlugIn, DialogListener {

	ImagePlus image = IJ.getImage();
	ImageProcessor processor = image.getProcessor();

	public void run(String arg) {
		apresentarInterfaceGrafica();
	}

	public int[] brightness(ImageProcessor processor, int[] pixel, int slider1) {
		for (int rgb = 0; rgb < 3; rgb++) {
			if ((pixel[rgb] + slider1) > 255) {
				pixel[rgb] = 255;
			} else if ((pixel[rgb] + slider1) < 0) {
				pixel[rgb] = 0;
			} else {
				pixel[rgb] = (int) (pixel[rgb] + slider1);
			}
		}
		return pixel;
	}

	public int[] contrast(ImageProcessor processor, int[] pixel, int slider2, float factor) {
		for (int rgb = 0; rgb < 3; rgb++) {
			pixel[rgb] = (int) (factor * (pixel[rgb] - 128)) + 128;
			if (pixel[rgb] < 0) {
				pixel[rgb] = 0;
			} else if (pixel[rgb] > 255) {
				pixel[rgb] = 255;
			}
		}
		return pixel;
	}

	public int[] solarize(ImageProcessor processor, int[] pixel, int slider3, int slider4) {
		int pixelLimiar = pixel[0] + pixel[1] + pixel[2];
		pixelLimiar /= 3;
		for (int rgb = 0; rgb < 3; rgb++) {
			if (pixelLimiar > slider3 || pixelLimiar < slider4) {
				pixel[rgb] = 255 - pixel[rgb];
			}
		}
		return pixel;
	}

	public int[] saturate(ImageProcessor processor, int[] pixel, float slider5) {
		int[] grayPixel = new int[3];
		float pixelLimiar = pixel[0] + pixel[1] + pixel[2];
		pixelLimiar /= 3;
		for (int rgb = 0; rgb < 3; rgb++) {
			grayPixel[rgb] = (int) pixelLimiar;
			pixel[rgb] = (int) (grayPixel[rgb] + slider5 * (pixel[rgb] - grayPixel[rgb]));
		}
		return pixel;
	}

	public void apresentarInterfaceGrafica() {
		GenericDialog interfaceGrafica = new GenericDialog("Image Adjust");
		interfaceGrafica.addDialogListener(this);

		processor.snapshot();

		interfaceGrafica.addMessage("Image Adjust");

		interfaceGrafica.addSlider("Brightness", -255, 255, 0, 1);
		interfaceGrafica.addSlider("Contrast", -255, 255, 0, 1);
		interfaceGrafica.addMessage("Solarize");
		interfaceGrafica.addSlider("Upper", 0, 255, 255, 1);
		interfaceGrafica.addSlider("Lower", 0, 255, 0, 1);
		interfaceGrafica.addSlider("Desaturate", 0, 1, 1, 0.01);

		interfaceGrafica.showDialog();

		if (interfaceGrafica.wasCanceled()) {
			processor.reset();
			image.draw();
			IJ.showMessage("PlugIn cancelado!");
		} else {
			if (interfaceGrafica.wasOKed()) {
				IJ.showMessage("Plugin encerrado com sucesso!");
			}
		}
	}

	@Override
	public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
		if (interfaceGrafica.wasCanceled())
			return false;

		processor.reset();

		int[] pixel = new int[3];
		int slider1 = (int) interfaceGrafica.getNextNumber();
		int slider2 = (int) interfaceGrafica.getNextNumber();
		int slider3 = (int) interfaceGrafica.getNextNumber();
		int slider4 = (int) interfaceGrafica.getNextNumber();
		float slider5 = (float) interfaceGrafica.getNextNumber();

		float factor1 = 259 * (slider2 + 255);
		float factor2 = 255 * (259 - slider2);
		float factor = factor1 / factor2;

		for (int i = 0; i < processor.getWidth(); i++) {
			for (int j = 0; j < processor.getHeight(); j++) {
				pixel = processor.getPixel(i, j, pixel);

				processor.putPixel(i, j, brightness(processor, pixel, slider1));
				processor.putPixel(i, j, contrast(processor, pixel, slider2, factor));
				processor.putPixel(i, j, solarize(processor, pixel, slider3, slider4));
				processor.putPixel(i, j, saturate(processor, pixel, slider5));

			}
		}

		image.updateAndDraw();
		return true;
	}
}