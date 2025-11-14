package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
// Importer les nouvelles classes
import java.util.Optional;

/**
 * Représente une Sphère dans la scène.
 * (Mise à jour Jalon 3 & 4)
 */
public class Sphere extends Shape {

    private final Point center;
    private final double radius;
    // Epsilon pour éviter les problèmes d'auto-intersection
    private static final double EPSILON = 1e-9; 

    /**
     * Constructeur pour une Sphère.
     * Appelle le constructeur parent (Shape) pour stocker les matériaux.
     *
     * @param center Le point central de la sphère.
     * @param radius Le rayon de la sphère.
     * @param diffuse La couleur diffuse.
     * @param specular La couleur spéculaire.
     * @param shininess L'indice de brillance.
     */
    public Sphere(Point center, double radius, Color diffuse, Color specular, double shininess) {
        // Appelle le constructeur de Shape (Jalon 2)
        super(diffuse, specular, shininess);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Calcule le vecteur normal à la surface de la sphère au point donné.
     * (Formule Jalon 4)
     * Formule: n = (Point - Centre) / ||Point - Centre||
     *
     * @param point Le point sur la surface de la sphère.
     * @return Le vecteur normal (normalisé) à ce point.
     */
    @Override
    public Vector getNormalAt(Point point) {
        // La normale est simplement le vecteur allant du centre au point, normalisé.
        // (point - center).normalize()
        return point.subtract(this.center).normalize();
    }

    /**
     * Calcule si un rayon donné intersecte cette sphère.
     * (Formule Jalon 3)
     *
     * @param ray Le rayon à tester.
     * @return Un Optional contenant l'Intersection si elle existe,
     * sinon Optional.empty().
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        // Implémentation de la formule de l'équation du second degré (Jalon 3)
        // a*t^2 + b*t + c = 0
        
        // o = origine du rayon
        // d = direction du rayon
        // c = centre de la sphère

        // Vecteur (o - c)
        Vector o_minus_c = ray.getOrigin().subtract(this.center);

        // a = d . d
        // Si le rayon est normalisé, a = 1, mais on le calcule par sécurité.
        double a = ray.getDirection().dot(ray.getDirection());

        // b = 2 * (o - c) . d
        double b = 2.0 * o_minus_c.dot(ray.getDirection());

        // c = (o - c) . (o - c) - r^2
        double c = o_minus_c.dot(o_minus_c) - (this.radius * this.radius);

        // Calculer le discriminant delta = b^2 - 4*a*c
        double delta = (b * b) - (4 * a * c);

        // Si delta < 0, le rayon manque la sphère
        if (delta < 0) {
            return Optional.empty();
        }

        // --- Le rayon touche la sphère, trouver le point d'impact ---

        double sqrtDelta = Math.sqrt(delta);

        // Calculer les two solutions (distances) t1 et t2
        // (t2 est la plus petite car -b - sqrtDelta)
        double t1 = (-b + sqrtDelta) / (2 * a);
        double t2 = (-b - sqrtDelta) / (2 * a);

        double t; // La distance finale retenue

        // On cherche la plus petite intersection POSITIVE
        // (EPSILON est utilisé pour éviter t=0, Jalon 5)
        
        if (t2 > EPSILON) {
            // t2 est la plus proche et est devant nous
            t = t2;
        } else if (t1 > EPSILON) {
            // t2 est négative (derrière nous), mais t1 est positive
            // (Nous sommes à l'intérieur de la sphère)
            t = t1;
        } else {
            // Les deux intersections (t1 et t2) sont négatives
            // L'objet est entièrement derrière nous
            return Optional.empty();
        }

        // Si on a trouvé un 't' valide, on crée l'objet Intersection
        
        // 1. Trouver le point d'impact
        Point intersectionPoint = ray.pointAt(t);
        
        // 2. Créer l'objet de données
        Intersection intersection = new Intersection(t, intersectionPoint, this);

        // 3. Le retourner dans un Optional
        return Optional.of(intersection);
    }
}