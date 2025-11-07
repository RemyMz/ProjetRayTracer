package fr.imt.raytracer.imgcompare;

import java.awt.image.BufferedImage;
import java.awt.Color; // Pour la couleur noire, c'est plus lisible

/**
 * Fournit des méthodes utilitaires pour comparer deux images BufferedImage.
 * Peut compter les pixels différents et générer une image de "différence".
 */
public class ImageComparator {

    /**
     * Compare deux images pixel par pixel et retourne le nombre de pixels différents.
     * * @param image1 La première image
     * @param image2 La deuxième image
     * @return Le nombre de pixels différents, ou -1 si les tailles ne correspondent pas.
     */
    public int countDifferentPixels(BufferedImage image1, BufferedImage image2) {
        
        // 1. Vérifier que les images ont la même taille
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            System.err.println("Erreur : Les images n'ont pas la même taille.");
            return -1; // On retourne -1 pour signifier une erreur
        }

        int diffPixelsCount = 0;
        int width = image1.getWidth();
        int height = image1.getHeight();

        // 2. Parcourir chaque pixel de l'image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                // 3. Récupérer la couleur RGB de chaque image au même pixel
                // getRGB() retourne un entier 'int' qui contient les 4 composantes (Alpha, Rouge, Vert, Bleu)
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);

                // 4. Comparer les couleurs
                if (rgb1 != rgb2) {
                    diffPixelsCount++; // Incrémenter le compteur si elles sont différentes
                }
            }
        }

        // 5. Retourner le nombre total de pixels différents
        return diffPixelsCount;
    }

    /**
     * Génère une image montrant les différences.
     * Les pixels identiques sont en noir, les autres montrent la différence absolue de couleur.
     * * @param image1 La première image
     * @param image2 La deuxième image
     * @return Une nouvelle BufferedImage représentant la différence
     */
    public BufferedImage generateDiffImage(BufferedImage image1, BufferedImage image2) {
        // On suppose que les images ont la même taille (le 'Main' a dû vérifier avant)
        int width = image1.getWidth();
        int height = image1.getHeight();

        // 1. Créer une nouvelle image vide, de type RGB (ignore la transparence)
        BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);

                if (rgb1 == rgb2) {
                    // 2. Pixels identiques : on les met en noir
                    diffImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    // 3. Pixels différents : on calcule la "différence de couleur"
                    
                    // On extrait chaque composante (Rouge, Vert, Bleu) pour l'image 1
                    // >> 16 : décale les bits rouges vers la droite
                    // & 0xFF : masque pour ne garder que les 8 bits de la couleur (0-255)
                    int r1 = (rgb1 >> 16) & 0xFF;
                    int g1 = (rgb1 >> 8) & 0xFF;
                    int b1 = rgb1 & 0xFF;

                    // On fait de même pour l'image 2
                    int r2 = (rgb2 >> 16) & 0xFF;
                    int g2 = (rgb2 >> 8) & 0xFF;
                    int b2 = rgb2 & 0xFF;

                    // On calcule la différence absolue pour chaque composante
                    int rDiff = Math.abs(r1 - r2);
                    int gDiff = Math.abs(g1 - g2);
                    int bDiff = Math.abs(b1 - b2);

                    // On recombine en une nouvelle couleur RGB
                    // << 16 : décale les bits rouges vers la gauche pour les remettre à leur place
                    int diffColor = (rDiff << 16) | (gDiff << 8) | bDiff;
                    
                    diffImage.setRGB(x, y, diffColor);
                }
            }
        }

        return diffImage; // On retourne la nouvelle image
    }
}