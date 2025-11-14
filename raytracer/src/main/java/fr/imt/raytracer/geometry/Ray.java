package fr.imt.raytracer.geometry;

/**
 * Représente un rayon, défini par une origine et une direction.
 * (Jalon 3 - Aide 3)
 */
public class Ray {

    private final Point origin;
    private final Vector direction;

    /**
     * Construit un nouveau rayon.
     * @param origin Le point d'origine du rayon.
     * @param direction Le vecteur de direction du rayon (devrait être normalisé).
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    // --- Getters ---

    public Point getOrigin() {
        return origin;
    }

    public Vector getDirection() {
        return direction;
    }

    /**
     * Calcule le point le long du rayon à une distance 't'.
     * Formule: P(t) = Origine + t * Direction
     * @param t La distance le long du rayon.
     * @return Le Point à la distance t.
     */
    public Point pointAt(double t) {
        // Utilise P.add(V.multiply(scalar))
        return origin.add(direction.multiply(t));
    }
}