package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Optional;

/**
 * Représente une sphère dans l'espace 3D.
 * <p>
 * Définie par un point central $C$ et un rayon $r$.
 * L'équation implicite d'une sphère est : $||P - C||^2 = r^2$.
 */
public class Sphere extends Shape {

    private final Point center;
    private final double radius;

    /**
     * Construit une nouvelle sphère.
     *
     * @param center    Le centre de la sphère.
     * @param radius    Le rayon de la sphère.
     * @param diffuse   La couleur diffuse (matériau).
     * @param specular  La couleur spéculaire (reflet).
     * @param shininess Le coefficient de brillance (Phong).
     */
    public Sphere(Point center, double radius, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Calcule l'intersection entre un rayon et la sphère.
     * <p>
     * Résout l'équation quadratique $at^2 + bt + c = 0$ dérivée de l'injection
     * de l'équation du rayon $P(t) = O + t\vec{d}$ dans l'équation de la sphère.
     *
     * @param ray Le rayon lancé.
     * @return Un Optional contenant l'intersection la plus proche (si elle existe).
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        // Vecteur allant du centre de la sphère à l'origine du rayon : L = O - C
        Vector originToCenter = ray.getOrigin().sub(center);
        Vector direction = ray.getDirection();

        // Calcul des coefficients de l'équation quadratique :
        // a = d . d  (vaut 1 si d est normalisé)
        // b = 2 * (L . d)
        // c = (L . L) - r^2
        double a = direction.dot(direction);
        double b = 2.0 * originToCenter.dot(direction);
        double c = originToCenter.dot(originToCenter) - (radius * radius);

        // Calcul du discriminant : delta = b^2 - 4ac
        double discriminant = (b * b) - (4.0 * a * c);

        // Cas 1 : Pas de solution réelle (le rayon manque la sphère)
        if (discriminant < 0) {
            return Optional.empty();
        }

        // Cas 2 : Une ou deux solutions (tangente ou sécante)
        double sqrtDiscriminant = Math.sqrt(discriminant);
        
        // Calcul des deux racines potentielles (distances t)
        // t2 est mathématiquement la plus petite (l'entrée dans la sphère si on est dehors)
        double t2 = (-b - sqrtDiscriminant) / (2.0 * a);
        double t1 = (-b + sqrtDiscriminant) / (2.0 * a);

        double t;

        // Sélection de la distance valide la plus proche
        if (t2 > AbstractVec3.EPSILON) {
            t = t2; // Intersection standard (devant la caméra)
        } else if (t1 > AbstractVec3.EPSILON) {
            t = t1; // La caméra est à l'intérieur de la sphère (t2 est derrière, t1 est devant)
        } else {
            return Optional.empty(); // La sphère est entièrement derrière la caméra
        }

        // Construction du résultat
        Point intersectionPoint = ray.getPointAt(t);
        Vector normal = getNormalAt(intersectionPoint);
        
        return Optional.of(new Intersection(t, intersectionPoint, normal, this));
    }

    /**
     * Calcule la normale à la surface de la sphère en un point donné.
     * <p>
     * La normale en un point $P$ d'une sphère de centre $C$ est le vecteur unitaire
     * de direction $\vec{CP}$.
     *
     * @param p Le point sur la surface.
     * @return Le vecteur normal normalisé $\vec{n} = \text{norm}(P - C)$.
     */
    @Override
    public Vector getNormalAt(Point p) {
        return p.sub(center).norm();
    }

    @Override
    public AABB getBoundingBox() {
        Vector r = new Vector(radius, radius, radius);
        return new AABB(center.sub(r), center.add(r));
    }
}