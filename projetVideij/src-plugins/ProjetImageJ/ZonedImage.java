package ProjetImageJ;

import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.util.ArrayList;

public class ZonedImage {

    public int[] zoning(ImageProcessor ip){
        int height = ip.getHeight();
        int width = ip.getWidth();

        ip = binarisation(ip);
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
        ByteProcessor binarised_pixels = new ByteProcessor(width,height,pixels);

        return binarised_pixels.crop();
    }

    public int blackInBloc(ImageProcessor ip){
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
