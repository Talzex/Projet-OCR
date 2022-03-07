/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjetImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    /**
     * Valeur de redimension d'une image (en pixels)
     */
    private static final int valeurRedimension = 20;
    /**
     * Matrice de confusion
     */
    public static int[][] matricedeconfusion;

    /**
     * Chemin vers nos images de base
     */
    private static final String pathToImages = "..\\baseProjetOCR";

    /**
     * Images
     */
    public static ArrayList<ImagePlus> images;

    /**
     *  Récupère les fichiers dans le répertoire, ici nos images
     * @param directoryPath, le chemin vers le répertoire
     * @return un tableau de fichiers à étudier
     */
    public static File[] listFiles (String directoryPath ) {
        File [] files = null ;
        File directoryToScan = new File(directoryPath);
        files = directoryToScan.listFiles ();
        return files ;
    }

    /**
     * Méthode permettant de traiter au mieux les image, pour améliorer leur reconnaissance
     * @param files, un tableau de l'ensemble des fichiers images
     */
    private static void TraitementImage(File[] files) {
        images = new ArrayList<>();
        for (File file : files) {
            if (!file.isHidden()) {
                String imageFilePath = file.getAbsolutePath();
                ImagePlus imp = IJ.openImage(imageFilePath);
                new ImageConverter(imp).convertToGray8();
                Utils.binarisation(imp.getProcessor());
                imp.getProcessor().dilate();
                imp.getProcessor().erode();
                images.add(imp);
            }
        }

    }

    /**
     * Méthode permettant d'extraire le chiffre par rapport à notre image reconnu
     * @param imagePlus, l'image
     * @return, le chiffre de l'image
     */
    private static int ImagetoNumber(ImagePlus imagePlus){
        String closestImageName;
        if(imagePlus != null){
            closestImageName = imagePlus.getTitle();
        } else {
            closestImageName = "";
        }
        System.out.println("Image trouve : " + closestImageName);
        return Integer.parseInt(closestImageName.split("_")[0]);
    }

    /**
     * Méthode permettant d'appeler en fonction du nom de l'algorithme celui-ci.
     * @param imagePlus, l'image à traiter avec les algorithme
     * @param algo, le nom de l'algorithme que l'on souhaite utiliser
     * @return un entier, qui est le nombre que l'algorithme à reconnu
     */
    private static int Algorithm(ImagePlus imagePlus,String algo) {
        switch (algo){
            case "Zoning":
                return ImagetoNumber(Zoning.closetImageZoning(imagePlus));
            case "Profil":
                return ImagetoNumber(Profil.closetImageProfile(imagePlus));
            default:
                System.out.println("Algorithme Inconnu");
                return -1;
        }
    }

    /** Calcule, le taux de reconnaissance
     * @param correct, le nombre d'image correctement reconnue
     * @return le taux de reconnaissance
     */
    private static float TauxReconnaissance(int correct) {
        return (correct*100)/(matricedeconfusion.length*matricedeconfusion.length);
    }

    /**
     * Affiche la matrice
     * @param correct le nombre d'image correctement identifiée
     */
    private static void showMatrice(int correct) {
        String message ="";
        for (int k = 0; k < matricedeconfusion.length; k++){
            if(k == 9) {
                System.out.print("     " + k + "\n");
            } else {
                System.out.print("     "+k);
            }
        }
        message += "-------------------------------------------------------------\n";
        for (int i = 0; i < matricedeconfusion.length; i++) {
            message += i + " | ";
            for (int j = 0; j < matricedeconfusion.length; j++) {
                if(j==0){
                    if(matricedeconfusion[i][j] > 9){
                        message += matricedeconfusion[i][j];
                    } else {
                        message += " " + matricedeconfusion[i][j];
                    }
                } else {
                    if(matricedeconfusion[i][j] > 9){
                        message += "    " + matricedeconfusion[i][j];
                    } else {
                        message += "     " + matricedeconfusion[i][j];
                    }
                }
            }
            message += "\n";
        }
        message += "Reconnaissance " + TauxReconnaissance(correct) + "%";
        System.out.println(message);
    }

    public static void main(String[] args) throws IOException {
        int correct = 0;
        matricedeconfusion = new int[10][10];
        File[] files = listFiles(pathToImages);
        if(files != null){
            Utils.resizeImage(files, valeurRedimension);
            files = listFiles(Utils.pathToImagesRedimension);
            TraitementImage(files);
            // Zoning => Algorithme Zoning
            // Profile => Algorithme Profile
            String algoChoisie = "Profil";
            System.out.println(algoChoisie);
            if(images != null && !images.isEmpty()){
                for (ImagePlus image : images) {
                    System.out.print("Image " + image.getTitle() + " : ");
                    int resultat = Algorithm(image,algoChoisie);
                    int valeur = Integer.parseInt(image.getTitle().split("_")[0]);
                    if(resultat == valeur) {
                        correct++;
                    }
                    matricedeconfusion[resultat][valeur] += 1;
                }
            } else {
                System.out.println("Recuperation incomplete : repertoire des images redimensionner non trouve ou aucune image trouve");
            }
        } else {
            System.out.println("Recuperation incomplete : repertoire des images non trouve");
        }
        showMatrice(correct);
    }
}
