package fr.imt.raytracer.imgcompare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Point d'entrée de l'application de comparaison d'images.
 * <p>
 * Orchestre le chargement des fichiers, l'appel au comparateur et la gestion des résultats
 * (affichage console et génération de l'image différentielle).
 */
public class Main {

    private static final int PIXEL_TOLERANCE_THRESHOLD = 1000;
    private static final String DIFF_FILENAME = "diff_output.png";

    public static void main(String[] args) {
        // 1. Validation des arguments
        if (args.length != 2) {
            printUsage();
            return;
        }

        File fileRef = new File(args[0]);
        File fileTest = new File(args[1]);

        System.out.println(String.format("Comparaison lancée : \n - Réf : %s\n - Test : %s", fileRef.getName(), fileTest.getName()));

        try {
            // 2. Chargement des images
            BufferedImage imgRef = loadImage(fileRef);
            BufferedImage imgTest = loadImage(fileTest);

            // 3. Exécution de la comparaison
            processComparison(imgRef, imgTest);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur de validation : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur E/S critique : " + e.getMessage());
        }
    }

    /**
     * Exécute la logique métier de comparaison et gère l'affichage des résultats.
     */
    private static void processComparison(BufferedImage imgRef, BufferedImage imgTest) throws IOException {
        ImageComparator comparator = new ImageComparator();
        
        // Note : countDifferentPixels lance une IllegalArgumentException si les tailles diffèrent
        long diffCount = comparator.countDifferentPixels(imgRef, imgTest);

        // Affichage du verdict standardisé (Spécification Jalon 0)
        if (diffCount < PIXEL_TOLERANCE_THRESHOLD) {
            System.out.println("OK");
        } else {
            System.out.println("KO");
        }
        System.out.println("Nombre de pixels différents : " + diffCount);

        // Génération conditionnelle de l'image de différence
        if (diffCount > 0) {
            generateAndSaveDiff(comparator, imgRef, imgTest);
        }
    }

    /**
     * Génère et sauvegarde l'image différentielle sur le disque.
     */
    private static void generateAndSaveDiff(ImageComparator comparator, BufferedImage imgRef, BufferedImage imgTest) {
        System.out.println("Génération de l'image différentielle...");
        try {
            BufferedImage diffImage = comparator.generateDiffImage(imgRef, imgTest);
            File outputFile = new File(DIFF_FILENAME);
            ImageIO.write(diffImage, "png", outputFile);
            System.out.println(" -> Image différentielle sauvegardée : " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture de l'image différentielle : " + e.getMessage());
        }
    }

    /**
     * Charge une image depuis le disque en vérifiant sa validité.
     * @throws IOException Si le fichier est illisible ou n'est pas une image valide.
     */
    private static BufferedImage loadImage(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Fichier introuvable : " + file.getPath());
        }
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("Format d'image non reconnu ou fichier corrompu : " + file.getPath());
        }
        return img;
    }

    private static void printUsage() {
        System.err.println("Erreur : Arguments manquants.");
        System.err.println("Usage : java -jar imgcompare.jar <image_reference.png> <image_test.png>");
    }
}