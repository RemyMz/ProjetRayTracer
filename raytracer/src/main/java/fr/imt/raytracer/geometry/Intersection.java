package fr.imt.raytracer.geometry;

/**
 * Enregistrement immuable des détails d'une intersection rayon-objet.
 * <p>
 * Stocke les informations géométriques critiques (position, normale, distance)
 * nécessaires au calcul de l'éclairage (shading).
 */
public class Intersection {

    private final double distance;
    private final Point point;
    private final Vector normal;
    private final Shape shape;

    /**
     * Crée un nouveau record d'intersection.
     *
     * @param distance La distance paramétrique t depuis l'origine du rayon.
     * @param point    Le point de contact exact dans l'espace 3D.
     * @param normal   Le vecteur normal à la surface au point d'impact.
     * @param shape    L'objet géométrique intersecté.
     */
    public Intersection(double distance, Point point, Vector normal, Shape shape) {
        this.distance = distance;
        this.point = point;
        this.normal = normal;
        this.shape = shape;
    }

    /**
     * Retourne la distance de l'intersection depuis l'origine du rayon.
     * @return La valeur scalaire t.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Retourne les coordonnées du point d'impact.
     * @return Le point d'intersection P.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Retourne la normale à la surface au point d'impact.
     * @return Le vecteur normal unitaire.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Retourne l'objet géométrique touché.
     * @return La référence vers la forme (Shape).
     */
    public Shape getShape() {
        return shape;
    }
}