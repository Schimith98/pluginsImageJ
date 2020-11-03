import ij.plugin.PlugIn;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import java.awt.AWTEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class Threshold implements PlugIn, DialogListener {

	ImagePlus image = IJ.getImage();
	ImageProcessor processor = image.getProcessor();

	public void run(String arg) {
		apresentarInterfaceGrafica();
	}

	public void apresentarInterfaceGrafica() {
		GenericDialog interfaceGrafica = new GenericDialog("Image Adjust");
		interfaceGrafica.addDialogListener(this);

		processor.snapshot();

		interfaceGrafica.addMessage("Image Adjust");

		interfaceGrafica.addSlider("Threshold", 0, 255, 0, 1);

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

		int pixel;
		int slider1 = (int) interfaceGrafica.getNextNumber();

		for (int i = 0; i < processor.getWidth(); i++) {
			for (int j = 0; j < processor.getHeight(); j++) {
				pixel = processor.getPixel(i, j);

                if(pixel < slider1){
                    pixel = 255;
                } else {
                    pixel = 0;
                }

				processor.putPixel(i, j, pixel);
			}
		}

		image.updateAndDraw();
		return true;
	}
}