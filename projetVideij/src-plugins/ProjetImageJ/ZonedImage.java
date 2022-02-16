package ProjetImageJ;

import ij.process.ImageProcessor;

import java.util.ArrayList;

public class ZonedImage {

    public ArrayList[] zoning(ImageProcessor ip){
        int height = ip.getHeight();
        int width = ip.getWidth();

        ip = binarisation(ip);
        ip.
        int[] V = new int[9];

        for (int i = 0; i < 3 ; i++){
            for (int j = 0; j < 3; j++){
                ip.setRoi(width/3*i,height/3*j,width/3,height/3);
                ImageProcessor bloc = ip.crop();

            }
        }



        return null;
    }

    public ImageProcessor binarisation(ImageProcessor ip){
        byte[] pixels = (byte[]) ip.getPixels();
        int height = ip.getHeight();
        int width = ip.getWidth();

        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                int pix = pixels[j * width + k] & 0xff; //  conversion en int
                if (pix < 120) {
                    pixels[j * width + k] = (byte) 0; // re-conversion en byte
                } else {
                    pixels[j * width + k] = (byte) 255; // re-conversion en byte
                }
            }
        }

        return pixels;
    }

}
