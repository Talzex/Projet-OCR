package ProjetImageJ;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.ArrayList;


public class Zoning {

    /**
     * Méthode permettant de retrouver l'image la plus proche de celle comparée, en utilisant le zonning
     * @param imagePlus, l'image à comparer
     * @return l'image qui se rapproche le plus de l'image comparée
     */
    public static ImagePlus closetImageZoning(ImagePlus imagePlus){
        ArrayList<ImagePlus> images = Main.images;
        ImagePlus found = null;
        if(images != null && !images.isEmpty()){
            double gap = Double.MAX_VALUE;
            int[] ImageVector = zoning(imagePlus.getProcessor());
            for (ImagePlus imageTemp : images) {
                if(!imageTemp.getTitle().equals(imagePlus.getTitle())) {
                    ImageProcessor ipTemp = imageTemp.getProcessor();
                    int[] ImageTempVector = zoning(ipTemp);
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
     * Méthode permettant de calculer le vecteur d'une image par bloc
     * @param ip, le processeur de l'image
     * @return, le vecteur de l'image
     */
    public static int[] zoning(ImageProcessor ip){
        int height = ip.getHeight();
        int width = ip.getWidth();
        int[] V = new int[9];
        int b=0;
        for (int i = 0; i < 3 ; i++){
            for (int j = 0; j < 3; j++){
                ip.setRoi((width/3)*j,(height/3)*i,width/3,height/3);
                ImageProcessor bloc = ip.crop();
                V[b] = blackInBloc(bloc);
                b++;
            }
        }
        return V;
    }

    /**
     * Méthode permettant de calculer le nombre de pixels noir dans une zone
     * @param ip, le processeur d'une zone de l'image
     * @return, un int, le nombre de pixels noir dans cette zone
     */
    public static int blackInBloc(ImageProcessor ip){
        byte[] pixels = (byte[]) ip.getPixels();
        int height = ip.getHeight();
        int width = ip.getWidth();
        int nbBlack = 0;
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                int pix = pixels[j * width + k] & 0xff; //  conversion en int
                if (pix == 0) {
                    nbBlack += 1;
                }
            }
        }
        return nbBlack;
    }

}
