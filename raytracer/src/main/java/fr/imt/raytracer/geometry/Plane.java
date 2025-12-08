package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Optional;

/**
 * Représente un plan infini dans l'espace 3D.
 * <p>
 * Défini mathématiquement par un point d'ancrage $q$ et une normale $\vec{n}$.
 * Tout point $p$ du plan satisfait l'équation : $(p - q) \cdot \vec{n} = 0$.
 */
public class Plane extends Shape {

    private final Point point;   // Le point q
    private final Vector normal; // Le vecteur n

    /**
     * Construit un nouveau plan.
     *
     * @param point     Un point quelconque appartenant au plan.
     * @param normal    Le vecteur normal à la surface (sera normalisé).
     * @param diffuse   La couleur diffuse (matériau).
     * @param specular  La couleur spéculaire (reflet).
     * @param shininess Le coefficient de brillance (Phong).
     */
    public Plane(Point point, Vector normal, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.point = point;
        this.normal = normal.norm(); // Normalisation critique pour les calculs d'angles
    }

    /**
     * Calcule l'intersection entre un rayon et ce plan.
     * <p>
     * Résout l'équation paramétrique $t = \frac{(q - o) \cdot \vec{n}}{\vec{d} \cdot \vec{n}}$.
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        Vector rayDirection = ray.getDirection();
        
        // Calcul du dénominateur : d . n
        double denominator = rayDirection.dot(normal);

        // 1. Vérification du parallélisme
        if (Math.abs(denominator) < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Calcul du numérateur : (q - o) . n
        Vector originToPlane = point.sub(ray.getOrigin());
        double numerator = originToPlane.dot(normal);

        // Calcul de la distance t
        double t = numerator / denominator;

        // 2. Vérification de la visibilité (devant la caméra)
        if (t < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Calcul du point d'impact exact
        Point intersectionPoint = ray.getPointAt(t);

        // --- GESTION DOUBLE FACE ---
        // Si le rayon arrive "de dos" (dénominateur positif), on inverse la normale
        // pour que la surface soit éclairée correctement des deux côtés.
        Vector effectiveNormal = denominator < 0 ? normal : normal.mult(-1.0);

        return Optional.of(new Intersection(t, intersectionPoint, effectiveNormal, this));
    }

    /**
     * Retourne la normale au plan.
     */
    @Override
    public Vector getNormalAt(Point p) {
        return normal;
    }

    /**
     * Retourne la boîte englobante (AABB) du plan.
     * <p>
     * Un plan étant infini, sa boîte englobante couvre théoriquement tout l'espace.
     * On retourne une boîte allant de -Infini à +Infini pour garantir que le plan
     * est toujours inclus dans les tests d'intersection du BVH.
     *
     * @return Une AABB infinie.
     */
    @Override
    public AABB getBoundingBox() {
        return new AABB(
            new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
            new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        );
    }
}