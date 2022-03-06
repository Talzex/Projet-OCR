package ProjetImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utils {


    /**
     * Le chemin vers les images apres redimension
     */
    public static final String pathToCroppedImages = "..\\ResizedImages";


    /**
     * Transforme l'image en noir et blanc => Binarisation (noir ou blanc)
     * @param ip, le processeur de l'image à binariser
     */
    public static void binarisation(ImageProcessor ip){
        int height = ip.getHeight();
        int width = ip.getWidth();
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                int pix = ip.getPixel(j,k);
                if (pix < 192) {
                    ip.putPixel(j,k,0);
                } else {
                    ip.putPixel(j,k,255);
                }
            }
        }
    }

    public static void dilatation(ImageProcessor ip, boolean flag) {
        int height = ip.getHeight();
        int width = ip.getWidth();
        int targetValue = (flag == true)?0:255;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (ip.getPixel(i, j) == targetValue) {
                    for (int ti = i - 1; ti <= i + 1; ti++) {
                        for (int tj = j - 1; tj <= j + 1; tj++) {
                            if (ti >= 0 && ti < width && tj >= 0 && tj < height){
                                if (ti != 0 && tj != 0) {
                                    ip.putPixel(ti, tj, 160);
                                }
                            }

                        }
                    }
                }

            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (ip.getPixel(i, j) == 160) {
                    ip.putPixel(i, j, targetValue);
                }
            }
        }
    }


    /**
     * Redimensionne une image
     * @param files, l'ensemble des fichiers images
     * @param targetSize, la taille de l'image souhaitée
     * @throws IOException, si on ne peut pas créer une nouvelle image
     */
    public static void resizeImage(File[] files, int targetSize) throws IOException {
        if(files != null){
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    // Ouvrir en tant qu'image
                    String imageFilePath = files[i].getAbsolutePath();
                    ImagePlus imp = IJ.openImage(imageFilePath);

                    String newName = imp.getTitle().split("\\.")[0] + ".jpg";
                    ImageProcessor ip = imp.getProcessor();
                    ip.setInterpolationMethod(ImageProcessor.BILINEAR);
                    ip = ip.resize(targetSize,targetSize);
                    BufferedImage croppedImage = ip.getBufferedImage();
                    new File(pathToCroppedImages).mkdirs();
                    ImageIO.write(croppedImage,"jpg",new File(pathToCroppedImages + "\\" + newName));

                }
            }

        }
    }

    /**
     * Division euclidienne entre deux vecteurs d'images
     *
     * @param val1 vecteur 1
     * @param val2 vecteur 2
     * @return le resultat de la division
     */
    public static double distanceEuclidienne(int[] val1, int[] val2) {
        int sum = 0;
        for (int i = 0; i < val1.length; i++) {
            sum += Math.pow(Math.abs((val1[i] - val2[i])), 2);
        }
        return Math.sqrt(sum);
    }
}
