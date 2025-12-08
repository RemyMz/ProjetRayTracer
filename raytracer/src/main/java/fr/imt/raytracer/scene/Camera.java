package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Ray;
import fr.imt.raytracer.geometry.Vector;

/**
 * Représente la caméra virtuelle (l'observateur) dans la scène.
 * <p>
 * La caméra est définie par :
 * <ul>
 * <li>Une position (l'œil ou {@code lookFrom}).</li>
 * <li>Un point visé ({@code lookAt}) définissant la direction de vue.</li>
 * <li>Un vecteur "haut" ({@code up}) définissant l'orientation verticale.</li>
 * <li>Un champ de vision (FOV) déterminant l'ouverture de l'angle de vue.</li>
 * </ul>
 * Elle construit un repère orthonormé local $(\vec{u}, \vec{v}, \vec{w})$ pour générer les rayons.
 */
public class Camera {

    /** Position de l'œil de la caméra. */
    private final Point lookFrom;

    // Repère orthonormé local de la caméra
    // w : Vecteur pointant à l'opposé de la direction de vue (vers l'arrière)
    // u : Vecteur horizontal (droite)
    // v : Vecteur vertical (haut)
    private final Vector u, v, w;

    /** Hauteur d'un pixel sur le plan de projection (situé à une distance de 1 unité). */
    private final double pixelHeight;

    /**
     * Construit et initialise la caméra.
     *
     * @param lookFrom Position de la caméra.
     * @param lookAt   Point regardé par la caméra.
     * @param up       Vecteur indiquant le "haut" approximatif du monde (ex: 0,1,0).
     * @param fov      Champ de vision vertical (Field of View) en degrés.
     */
    public Camera(Point lookFrom, Point lookAt, Vector up, double fov) {
        this.lookFrom = lookFrom;

        // 1. Calcul de w : Axe Z local (inverse de la direction de vue)
        // w = normaliser(lookFrom - lookAt)
        this.w = lookFrom.sub(lookAt).norm();

        // 2. Calcul de u : Axe X local (perpendiculaire au plan formé par 'up' et 'w')
        // u = normaliser(up ^ w)
        this.u = up.norm().cross(w).norm();

        // 3. Calcul de v : Axe Y local (perpendiculaire à w et u)
        // v = w ^ u
        this.v = w.cross(u).norm();

        // 4. Calcul de la hauteur du pixel virtuel
        // On considère que le plan image est à une distance d=1 de l'œil.
        // height = tan(fov / 2)
        double fovRadians = Math.toRadians(fov);
        this.pixelHeight = Math.tan(fovRadians / 2.0);
    }

    /**
     * Retourne la position de la caméra.
     * @return Le point {@code lookFrom}.
     */
    public Point getLookFrom() {
        return lookFrom;
    }

    /**
     * Génère un rayon primaire partant de l'œil et passant par le centre du pixel (i, j).
     *
     * @param i         L'indice de colonne du pixel (coordonnée x écran).
     * @param j         L'indice de ligne du pixel (coordonnée y écran).
     * @param imgWidth  La largeur totale de l'image en pixels.
     * @param imgHeight La hauteur totale de l'image en pixels.
     * @return Le rayon ({@link Ray}) correspondant, normalisé.
     */
    public Ray generateRay(int i, int j, int imgWidth, int imgHeight) {
        
        // Calcul du ratio d'aspect pour ajuster la largeur du pixel
        double aspectRatio = (double) imgWidth / imgHeight;
        double currentPixelWidth = pixelHeight * aspectRatio;

        // Calcul des coordonnées normalisées sur le plan image (de -1 à +1 environ)
        
        // 'a' est le décalage horizontal le long du vecteur u
        // (i - width/2 + 0.5) centre le rayon au milieu du pixel
        double a = currentPixelWidth * (i - imgWidth / 2.0 + 0.5) / (imgWidth / 2.0);
        
        // 'b' est le décalage vertical le long du vecteur v
        // NOTE IMPORTANTE : L'axe Y de l'image (j) va vers le bas, alors que l'axe v va vers le haut.
        // On multiplie par -1.0 pour inverser l'axe et remettre l'image à l'endroit.
        double b = -1.0 * pixelHeight * (j - imgHeight / 2.0 + 0.5) / (imgHeight / 2.0);

        // Construction de la direction du rayon : d = a*u + b*v - w
        // Le rayon part de l'origine vers le plan image (situé à -w)
        Vector direction = u.mult(a)
                            .add(v.mult(b))
                            .sub(w); // -w car on regarde vers -Z dans le repère local
        
        return new Ray(lookFrom, direction);
    }
}