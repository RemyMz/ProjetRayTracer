package fr.imt.raytracer.imgcompare;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Programme principal pour comparer deux images PNG.
 * Ce programme prend deux chemins d'image en entrée, les compare pixel par pixel,
 * et produit un rapport sur leurs différences.
 */
public class Main {

    /**
     * Point d'entrée du programme.
     *
     * @param args Les arguments de la ligne de commande, attend deux chemins vers des images.
     */
    public static void main(String[] args) {
        // 1. Vérifier qu'on a bien deux arguments (les deux images)
        if (args.length != 2) {
            System.out.println("Erreur: Vous devez fournir deux images en paramètre.");
            System.out.println("Usage: java -jar imgcompare.jar <image1.png> <image2.png>");
            return; // On arrête le programme
        }

        // 2. Stocker les noms de fichiers
        String imagePath1 = args[0];
        String imagePath2 = args[1];

        System.out.println("Comparaison de " + imagePath1 + " et " + imagePath2);

        try {
            // 3. Lire les fichiers et les stocker en BufferedImage
            // On utilise la classe File pour représenter le fichier
            // et ImageIO.read() pour lire l'image
            File file1 = new File(imagePath1);
            File file2 = new File(imagePath2);
            
            BufferedImage image1 = ImageIO.read(file1);
            BufferedImage image2 = ImageIO.read(file2);

            // 4. Vérifier que les images ont bien été lues
            // ImageIO.read() retourne null si le format n'est pas reconnu
            if (image1 == null || image2 == null) {
                System.out.println("Erreur: Impossible de lire une des images. Vérifiez les chemins ou les formats.");
                return;
            }

            System.out.println("Les deux images ont été chargées avec succès.");

            // 5. Appeler le comparateur
            ImageComparator comparator = new ImageComparator();
            int differentPixels = comparator.countDifferentPixels(image1, image2);

            // 6. Gérer le cas où les tailles sont différentes (code d'erreur -1)
            if (differentPixels == -1) {
                // Le message d'erreur est déjà imprimé par le comparateur
                return;
            }

            // 7. Afficher le résultat final (OK/KO et comptage) -- C'EST LE NOUVEL ORDRE
            if (differentPixels < 1000) { 
                System.out.println("OK");
            } else {
                System.out.println("KO");
            }
            System.out.println("Les deux images diffèrent de " + differentPixels + " pixels.");

            // 8. On génère l'image différentielle SEULEMENT si des pixels diffèrent
            if (differentPixels > 0) {
                System.out.println("Génération de l'image différentielle...");
                BufferedImage diffImage = comparator.generateDiffImage(image1, image2);
                
                // 9. Enregistrer la nouvelle image
                try {
                    File outputDiffFile = new File("diff.png");
                    ImageIO.write(diffImage, "png", outputDiffFile);
                    System.out.println("Image différentielle sauvegardée : diff.png");
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'écriture de l'image diff : " + e.getMessage());
                }
            }

        } catch (IOException e) {
            // 10. Gérer les erreurs de lecture de fichier (ex: fichier non trouvé)
            System.err.println("Erreur lors de la lecture des fichiers : " + e.getMessage());
        }
    }
}