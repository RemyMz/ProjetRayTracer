package fr.imt.raytracer.renderer;

import fr.imt.raytracer.geometry.Intersection;
import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Ray;
import fr.imt.raytracer.geometry.Shape;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.scene.Camera;
import fr.imt.raytracer.scene.Scene;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Optional;

/**
 * Le moteur principal du Ray Tracer.
 * (Jalon 3)
 */
public class RayTracer {

    private final Scene scene;
    private final Camera camera;
    
    // Repère orthonormé de la caméra (Jalon 3, page 2)
    private final Vector u, v, w;

    // Dimensions du "film" virtuel (Jalon 3, page 3)
    private final double pixelHeight;
    private final double pixelWidth;

    /**
     * Constructeur du RayTracer.
     * Prépare le moteur de rendu en calculant la base orthonormée de la caméra.
     * @param scene La scène à rendre.
     */
    public RayTracer(Scene scene) {
        this.scene = scene;
        this.camera = scene.getCamera();

        // --- Calcul du repère orthonormé (u,v,w) - (Jalon 3, page 2) ---

        // w = (lookFrom - lookAt).normalize()
        this.w = camera.getLookFrom().subtract(camera.getLookAt()).normalize();
        
        // u = (up x w).normalize()
        this.u = camera.getUp().cross(this.w).normalize();

        // v = (w x u).normalize()
        // (Pas besoin de normaliser car u et w sont orthogonaux et normalisés)
        this.v = this.w.cross(this.u); 

        // --- Calcul des dimensions d'un pixel - (Jalon 3, page 3) ---

        // fovr = fov * PI / 180
        double fovr = Math.toRadians(camera.getFov());

        // pixelHeight = tan(fovr / 2)
        // (Hauteur du film à une distance de 1)
        this.pixelHeight = Math.tan(fovr / 2.0);

        // pixelWidth = pixelHeight * (width / height)
        double aspectRatio = (double) scene.getWidth() / scene.getHeight();
        this.pixelWidth = this.pixelHeight * aspectRatio;
    }

    /**
     * Lance le rendu de l'image.
     * @return Une BufferedImage contenant l'image rendue.
     */
    public BufferedImage render() {
        int width = scene.getWidth();
        int height = scene.getHeight();
        
        // Créer l'image sur laquelle on va dessiner
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // "pour chaque pixel (i,j) de l'image..." (Jalon 3)
        for (int j = 0; j < height; j++) { // Lignes (y)
            for (int i = 0; i < width; i++) { // Colonnes (x)
                
                // 1. Calculer le rayon pour ce pixel (i, j)
                Ray ray = generateRayForPixel(i, j);

                // 2. Trouver l'intersection la plus proche
                Optional<Intersection> closestIntersection = findClosestIntersection(ray);

                // 3. Calculer la couleur (Jalon 3, page 1 & 5)
                Color pixelColor;
                if (closestIntersection.isPresent()) {
                    // "si p existe alors calculer sa couleur"
                    // "On utilisera tout simplement la lumière ambiante de la scène" (Jalon 3, page 5)
                    pixelColor = scene.getAmbientLight();
                } else {
                    // "sinon utiliser du noir"
                    pixelColor = new Color(0, 0, 0); // Noir
                }

                // 4. Peindre le pixel
                // ATTENTION: Coordonnées Java (Jalon 3, page 3)
                // (i, j) dans le raytracer correspond à (i, height - 1 - j) dans l'image
                // ou (i, j) si on inverse le vecteur 'v' ?
                // Le PDF dit "inverser les pixels des colonnes (haut/bas)"
                // Non, "inverser les pixels des colonnes" (j) est ambigu.
                // Le plus simple est de ne PAS inverser 'b' dans le calcul du rayon.
                // (Voir generateRayForPixel, j'ai mis un 'moins' sur 'b')
                //
                // Si l'image est à l'envers, on changera :
                // image.setRGB(i, j, pixelColor.toRGB());
                // en:
                image.setRGB(i, j, pixelColor.toRGB());
            }
        }
        return image;
    }

    /**
     * Calcule le rayon (origine, direction) pour un pixel (i, j) donné.
     * (Formules Jalon 3, pages 3 & 4)
     * @param i Coordonnée x du pixel (colonne)
     * @param j Coordonnée y du pixel (ligne)
     * @return Un Ray partant de la caméra.
     */
    private Ray generateRayForPixel(int i, int j) {
        int width = scene.getWidth();
        int height = scene.getHeight();

        // Calcul de 'a' et 'b' (Jalon 3, page 4)
        // a = pixelWidth * ( (i - (width/2) + 0.5) / (width/2) )
        // a = pixelWidth * ( (2i - width + 1) / width )
        double a = pixelWidth * ( (2.0 * i - width + 1.0) / width );
        
        // b = pixelHeight * ( (j - (height/2) + 0.5) / (height/2) )
        // b = pixelHeight * ( (2j - height + 1) / height )
        double b = pixelHeight * ( (2.0 * j - height + 1.0) / height );

        // Le PDF (Jalon 3, page 3) dit que le (0,0) Java est en haut à gauche,
        // ce qui inverse l'axe Y (j). On doit donc inverser 'b'.
        // Si l'image est à l'envers, on enlèvera ce "moins".
        b = -b;

        // Calcul de la direction 'd' (Jalon 3, page 4)
        // d = (u*a + v*b - w).normalize()
        // (u * a)
        Vector d_u = u.multiply(a);
        // (v * b)
        Vector d_v = v.multiply(b);
        // (u*a + v*b - w)
        Vector d = d_u.add(d_v).subtract(w);
        
        // d.normalize()
        Vector direction = d.normalize();

        // L'origine du rayon est la position de la caméra
        Point origin = camera.getLookFrom();

        return new Ray(origin, direction);
    }

    /**
     * "rechercher le point d'intersection p le plus proche" (Jalon 3)
     * Parcourt tous les objets de la scène et trouve l'intersection
     * la plus proche (distance 't' la plus faible).
     *
     * @param ray Le rayon à tester.
     * @return Un Optional contenant l'Intersection la plus proche.
     */
    private Optional<Intersection> findClosestIntersection(Ray ray) {
        
        // Utilise les Streams Java pour faire la recherche (plus propre)
        return scene.getShapes().stream()
                // 1. Appelle findIntersection() pour chaque forme
                .map(shape -> shape.findIntersection(ray))
                // 2. Filtre tous les "Optional.empty()" (rayons manqués)
                .filter(Optional::isPresent)
                // 3. Extrait les objets Intersection de leur Optional
                .map(Optional::get)
                // 4. Trouve l'intersection avec la distance minimale
                // (Comparator.comparing(Intersection::getDistance))
                .min(Comparator.comparing(Intersection::getDistance));
    }
}