package ProjetImageJ;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static ProjetImageJ.Main.listFiles;

public class MagicVector {
    public int[] vector = new int[9];
    ArrayList<int[]> tabvectors = new ArrayList<int[]>();

    public void getVectors(String path) {

        File[] files = listFiles(path);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    String filePath = files[i].getAbsolutePath();
                    ImagePlus Img = new ImagePlus(filePath);
                    new ImageConverter(Img).convertToGray8();

                    ImageProcessor ip = Img.getProcessor();
                    Zoning zoned = new Zoning();
                    ImageProcessor imageBin = zoned.binarisation(ip);
                    ImagePlus imp = new ImagePlus("Result", imageBin);
                    int V[] = new int [9];
                    V = zoned.zoning(ip);
                    tabvectors.set(i, V);
                }
            }
        }
    }

    public void getMagicVector(){
        for(int i=0; i< vector.length; i++){
            int moy;
            for(int j=0; j<tabvectors.size();i++){

                
            }
        }

    }
}
