package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Optional;

/**
 * Représente un triangle, la primitive de base de la modélisation 3D.
 * <p>
 * Défini par trois sommets ordonnés dans le sens anti-horaire (Counter-Clockwise).
 * L'ordre des sommets détermine la direction de la normale (règle de la main droite).
 */
public class Triangle extends Shape {

    private final Point a, b, c;
    private final Vector normal;

    // Vecteurs arêtes pré-calculés pour optimiser les tests d'intersection
    private final Vector edgeAB;
    private final Vector edgeBC;
    private final Vector edgeCA;

    /**
     * Construit un nouveau triangle.
     *
     * @param a         Sommet 1.
     * @param b         Sommet 2.
     * @param c         Sommet 3.
     * @param diffuse   Couleur diffuse.
     * @param specular  Couleur spéculaire.
     * @param shininess Facteur de brillance.
     */
    public Triangle(Point a, Point b, Point c, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.a = a;
        this.b = b;
        this.c = c;

        // Pré-calcul des vecteurs directionnels des arêtes (P2 - P1)
        this.edgeAB = b.sub(a);
        this.edgeBC = c.sub(b);
        this.edgeCA = a.sub(c);

        // Calcul de la normale de la surface : (B-A) x (C-A)
        // Normalisation immédiate pour les calculs d'éclairage
        this.normal = edgeAB.cross(c.sub(a)).norm();
    }

    /**
     * Calcule l'intersection rayon-triangle.
     * <p>
     * Procède en deux étapes :
     * 1. Intersection avec le plan de support du triangle.
     * 2. Test d'inclusion (le point est-il à l'intérieur des 3 arêtes ?).
     *
     * @param ray Le rayon lancé.
     * @return Un Optional contenant l'intersection si valide, vide sinon.
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        Vector rayDirection = ray.getDirection();
        
        // --- Étape 1 : Intersection avec le plan ---
        // On utilise 'a' comme point de référence du plan
        
        double denominator = rayDirection.dot(normal);

        // Vérification du parallélisme (Rayon orthogonal à la normale)
        // Utilisation de l'EPSILON global pour la robustesse numérique
        if (Math.abs(denominator) < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Distance t = (a - o) . n / (d . n)
        Vector originToA = a.sub(ray.getOrigin());
        double t = originToA.dot(normal) / denominator;

        // Vérification de la visibilité (intersection devant la caméra)
        if (t < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Point d'impact présumé sur le plan
        Point p = ray.getPointAt(t);

        // --- Étape 2 : Test d'intérieur (Edge-Normal Test) ---
        // Pour chaque arête, on vérifie si le point P se trouve du côté "intérieur"
        // Le produit vectoriel (Arête x Vecteur_Sommet_Vers_P) doit être aligné avec la normale.

        // Test Arête AB
        Vector pA = p.sub(a);
        if (edgeAB.cross(pA).dot(normal) < -AbstractVec3.EPSILON) return Optional.empty();

        // Test Arête BC
        Vector pB = p.sub(b);
        if (edgeBC.cross(pB).dot(normal) < -AbstractVec3.EPSILON) return Optional.empty();

        // Test Arête CA
        Vector pC = p.sub(c);
        if (edgeCA.cross(pC).dot(normal) < -AbstractVec3.EPSILON) return Optional.empty();

        // Si tous les tests passent, le point est dans le triangle
        return Optional.of(new Intersection(t, p, normal, this));
    }

    /**
     * Retourne la normale du triangle.
     * <p>
     * Pour un triangle (ombrage plat / flat shading), la normale est constante sur toute la surface.
     *
     * @param p Le point d'intersection (inutilisé).
     * @return Le vecteur normal unitaire.
     */
    @Override
    public Vector getNormalAt(Point p) {
        return normal;
    }

    @Override
    public AABB getBoundingBox() {
        double minX = Math.min(a.getX(), Math.min(b.getX(), c.getX()));
        double minY = Math.min(a.getY(), Math.min(b.getY(), c.getY()));
        double minZ = Math.min(a.getZ(), Math.min(b.getZ(), c.getZ()));

        double maxX = Math.max(a.getX(), Math.max(b.getX(), c.getX()));
        double maxY = Math.max(a.getY(), Math.max(b.getY(), c.getY()));
        double maxZ = Math.max(a.getZ(), Math.max(b.getZ(), c.getZ()));

        // On ajoute un petit epsilon pour éviter les boîtes plates
        return new AABB(
            new Point(minX - 0.001, minY - 0.001, minZ - 0.001),
            new Point(maxX + 0.001, maxY + 0.001, maxZ + 0.001)
        );
    }
}