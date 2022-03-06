package ProjetImageJ;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.ArrayList;

public class Profil {


    /**
     *Méthode permettant de retrouver l'image la plus proche de celle comparée, en utilisant le profil d'une image
     * @param imagePlus, l'image à comparer
     * @return l'image qui se rapproche le plus de l'image comparée
     */
    public static ImagePlus closetImageProfile(ImagePlus imagePlus) {
        ArrayList<ImagePlus> images = Main.images;
        ImagePlus found = null;
        if(images != null && !images.isEmpty()){
            double gap = Double.MAX_VALUE;
            int[] ImageVector = profile(imagePlus.getProcessor());
            for (ImagePlus imageTemp : images) {
                if(!imageTemp.getTitle().equals(imagePlus.getTitle())) {
                    ImageProcessor ipTemp = imageTemp.getProcessor();
                    int[] ImageTempVector = profile(ipTemp);
                    double dif = Utils.distanceEuclidienne(ImageVector,ImageTempVector);
                    if(dif < gap){
                        found = imageTemp;
                        gap = dif;
                    }
                }
            }
            return found;
        }
        return null;
    }


    /**
     * Méthode permettant d'effectuer le profile horizontale et vertical de l'image
     * @param ip, le processeur de l'image
     * @return un tableau de int[], sous la forme  : [[valeurs pour le profil horizontal], [valeurs pour le profil vertical]]
     */
    public static int[] profile(ImageProcessor ip) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        int[] line = new int[width * 2];

        // Profil horizontal de l'image
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(ip.getPixel(j,i) == 0){
                    line[i] += 1;
                }
            }
        }

        // Profil verticale de l'image
        for (int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(ip.getPixel(i,j) == 0){
                    line[width + i] += 1;
                }
            }
        }
        return line;
    }
}
