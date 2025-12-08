package fr.imt.raytracer.geometry;

/**
 * Représente un rayon (demi-droite) dans l'espace 3D.
 * <p>
 * Défini par une origine $O$ et un vecteur directionnel unitaire $\vec{d}$.
 * Tout point du rayon peut être exprimé par l'équation paramétrique $P(t) = O + t \cdot \vec{d}$ pour $t \ge 0$.
 */
public class Ray {

    private final Point origin;
    private final Vector direction;

    /**
     * Construit un nouveau rayon.
     * <p>
     * Le vecteur direction fourni sera automatiquement normalisé.
     *
     * @param origin    Le point d'origine du rayon.
     * @param direction Le vecteur indiquant la direction de propagation.
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.norm(); // Normalisation critique pour la cohérence des distances t
    }

    /**
     * Retourne le point d'origine du rayon.
     * @return Le point de départ $O$.
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Retourne la direction normalisée du rayon.
     * @return Le vecteur unitaire $\vec{d}$.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Calcule la position d'un point sur le rayon à une distance donnée.
     * <p>
     * Applique la formule $P = O + t \cdot \vec{d}$.
     *
     * @param t La distance paramétrique depuis l'origine (positive vers l'avant).
     * @return Le point situé à la distance t le long du rayon.
     */
    public Point getPointAt(double t) {
        return origin.add(direction.mult(t));
    }
}