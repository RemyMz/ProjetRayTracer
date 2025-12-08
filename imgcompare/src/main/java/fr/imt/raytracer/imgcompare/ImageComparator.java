package fr.imt.raytracer.imgcompare;

import java.awt.image.BufferedImage;

/**
 * Utilitaire de comparaison d'images.
 * Permet de quantifier les différences et de générer une carte de chaleur des écarts colorimétriques.
 */
public class ImageComparator {

    private static final int BLACK_PIXEL = 0xFF000000; // Alpha=255, R=0, G=0, B=0

    /**
     * Compte le nombre de pixels dont la valeur RGB diffère entre deux images.
     *
     * @param img1 La première image de référence.
     * @param img2 La seconde image à comparer.
     * @return Le nombre total de pixels différents.
     * @throws IllegalArgumentException Si les dimensions des images ne correspondent pas.
     */
    public long countDifferentPixels(BufferedImage img1, BufferedImage img2) {
        checkDimensions(img1, img2);

        long diffCount = 0;
        int width = img1.getWidth();
        int height = img1.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    diffCount++;
                }
            }
        }

        return diffCount;
    }

    /**
     * Génère une image différentielle mettant en évidence les écarts de couleur.
     * <p>
     * Les pixels identiques apparaissent en noir.
     * Les pixels différents affichent la valeur absolue de la différence par canal (R, G, B).
     *
     * @param img1 La première image.
     * @param img2 La seconde image.
     * @return Une nouvelle BufferedImage représentant les différences.
     * @throws IllegalArgumentException Si les dimensions des images ne correspondent pas.
     */
    public BufferedImage generateDiffImage(BufferedImage img1, BufferedImage img2) {
        checkDimensions(img1, img2);

        int width = img1.getWidth();
        int height = img1.getHeight();
        BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                if (rgb1 == rgb2) {
                    diffImage.setRGB(x, y, BLACK_PIXEL);
                } else {
                    diffImage.setRGB(x, y, calculatePixelDiff(rgb1, rgb2));
                }
            }
        }

        return diffImage;
    }

    /**
     * Vérifie que les deux images ont strictement les mêmes dimensions.
     */
    private void checkDimensions(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            throw new IllegalArgumentException(String.format(
                "Dimensions incompatibles : %dx%d vs %dx%d",
                img1.getWidth(), img1.getHeight(), img2.getWidth(), img2.getHeight()
            ));
        }
    }

    /**
     * Calcule la différence absolue composante par composante entre deux pixels.
     */
    private int calculatePixelDiff(int rgb1, int rgb2) {
        // Extraction des composantes (Rouge, Vert, Bleu) via masques binaires
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = rgb1 & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = rgb2 & 0xFF;

        // Calcul des deltas absolus
        int rDiff = Math.abs(r1 - r2);
        int gDiff = Math.abs(g1 - g2);
        int bDiff = Math.abs(b1 - b2);

        // Recomposition du pixel (Alpha opaque par défaut + deltas)
        return (0xFF << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
    }
}