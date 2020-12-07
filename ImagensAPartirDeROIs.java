import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.plugin.frame.RoiManager;
import ij.gui.Roi;

public class ImagensAPartirDeROIs implements PlugIn{

    public void run(String arg) {
        
        //Obter a imagem original
        ImagePlus imageOriginal = IJ.getImage();

        //Duplicando a imagem original para aplicar os processos
        ImagePlus image = imageOriginal.duplicate();
        ImageProcessor processor = image.getProcessor();

        //Binarizar a imagem
        IJ.run(image, "Convert to Mask", "");
        //Preencher os vazios existentes internamente nas ROIs identificadas
        IJ.run(image, "Fill Holes", "");
        //Executar o comando Analyze Particles para identificar automaticamente os ROIs presentes na imagem e adicioná-los no RoiManager
        IJ.run(image, "Analyze Particles...", "add");
        
        //Criar um vetor contendo todos os ROIs presentes no RoiManager
        RoiManager rm = RoiManager.getRoiManager();
        Roi[] imageRois = rm.getRoisAsArray();

        //Obter o caminho para onde os arquivos serão gravados
        String path = IJ.getFilePath("");

        if(path != null){
            for(int i = 0; i < rm.getCount(); i++){
                //Selecionando o ROI na imagem orinal
                rm.select(imageOriginal, i);
                //Fazendo o crop
                ImagePlus CroppedImage = imageOriginal.crop();
                //CroppedImage.show();
                //Salvando
                IJ.save(CroppedImage, path + i +"png");
            }
        } 
    }
}