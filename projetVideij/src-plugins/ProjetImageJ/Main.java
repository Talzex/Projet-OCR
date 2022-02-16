/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjetImageJ;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.File;

/**
 *
 * @author nicholasjournet
 */
public class Main {

    public static File[] listFiles (String directoryPath ) {
        File [] files = null ;
        File directoryToScan = new File ( directoryPath ) ;
        files = directoryToScan . listFiles () ;
        return files ;
    }
    public static void main(String[] args) {
        String path = "/mnt/roost/users/tduthil/S4/S4_Image/projetOCR/baseProjetOCR/";
        File[] files = listFiles(path);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    String filePath = files[i].getAbsolutePath();
                    ImagePlus Img = new ImagePlus(filePath);
                    new ImageConverter(Img).convertToGray8();
                    ImageProcessor ip = Img.getProcessor();
                    byte[] pixels = (byte[]) ip.getPixels();
                    int height = ip.getHeight();
                    int width = ip.getWidth();

                }
            }
        /*new ImageJ();
        ImagePlus image = IJ.openImage("/mnt/roost/users/tduthil/S4/S4_Image/projetOCR/baseProjetOCR/2_8.png");
        IJ.showMessage("Accès à ImageJ depuis le code source");
        image.show();
        WindowManager.addWindow(image.getWindow());
        closetImage(ip);
        System.out.println(meanImage(ip));*/

        }
    }

    public static double meanImage(ImageProcessor ip){
        byte[] pixels = (byte[]) ip.getPixels();
        int height = ip.getHeight();
        int width = ip.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pix = pixels[i * width + j] & 0xff; //  conversio en int
                if (pix < 120) {
                    pixels[i * width + j] = (byte) 0; // re-conversion en byte
                } else {
                    pixels[i * width + j] = (byte) 255; // re-conversion en byte
                }
            }
        }
        double somme = 0;
        for( int i = 0; i < pixels.length; i++) {
            somme += pixels[i] & 0xff;
        }
        return somme/pixels.length;
    }



    public  static void closetImage(ImageProcessor ip){
        String path = "/mnt/roost/users/tduthil/S4/S4_Image/projetOCR/baseProjetOCR/";
        File[] files = listFiles(path);
        if (files != null){
            double gap = Double.MAX_VALUE;
            int min = 0;
            for (int i=0; i < files.length; i++){
                if (!files[i].isHidden()){
                    //Création d'une image temporaire
                    String filePath = files[i].getAbsolutePath();
                    ImagePlus tempImg = new ImagePlus(filePath);
                    new ImageConverter(tempImg).convertToGray8();
                    ImageProcessor ipTemp = tempImg.getProcessor();
                    // Calcul du niveau de gris moyen de l'image
                    double avgTemp = meanImage(ipTemp);
                    // Différence par rapport à l'image d'origine
                    double dif = Math.abs(meanImage(ip) - avgTemp);
                    if (dif < gap){
                        gap = dif;
                        min = i;
                    }
                }
            }
            String closetImageName =files[min].getName();
            IJ.showMessage( " L ’ image la plus proche est " + closetImageName
                    + " avec une distance de " + gap + " . " ) ;
        }
    }


}
