package fr.imt.raytracer.geometry;

/**
 * Représente un Vecteur 3D (direction, normale).
 * Implémente les opérations vectorielles du Jalon 1.
 */
public class Vector extends AbstractVec3 {

    /**
     * Constructeur pour un vecteur.
     * @param x composante x
     * @param y composante y
     * @param z composante z
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Addition de deux vecteurs. (Jalon 1)
     * @param other L'autre vecteur.
     * @return Un nouveau vecteur (this + other).
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Soustraction de deux vecteurs. (Jalon 1)
     * @param other L'autre vecteur.
     * @return Un nouveau vecteur (this - other).
     */
    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Multiplication par un scalaire. (Jalon 1)
     * @param scalar Le scalaire.
     * @return Un nouveau vecteur (this * scalar).
     */
    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Produit vectoriel (Cross product). (Jalon 1)
     * Formule: (y1*z2 - z1*y2, z1*x2 - x1*z2, x1*y2 - y1*x2)
     * @param other L'autre vecteur.
     * @return Un nouveau vecteur (this x other).
     */
    public Vector cross(Vector other) {
        double newX = this.y * other.z - this.z * other.y;
        double newY = this.z * other.x - this.x * other.z;
        double newZ = this.x * other.y - this.y * other.x;
        return new Vector(newX, newY, newZ);
    }

    /**
     * Calcule la longueur (magnitude) du vecteur. (Jalon 1)
     * Formule: sqrt(x*x + y*y + z*z)
     * C'est aussi la racine carrée du produit scalaire du vecteur avec lui-même.
     * @return La longueur (double).
     */
    public double length() {
        return Math.sqrt(this.dot(this));
    }

    /**
     * Normalise le vecteur (le rend de longueur 1). (Jalon 1)
     * Formule: (1 / ||v||) * v
     * @return Un nouveau vecteur normalisé.
     */
    public Vector normalize() {
        double len = length();
        // Utilise la comparaison d'epsilon de la classe parente pour éviter la division par zéro
        if (nearlyEquals(len, 0)) {
            return new Vector(0, 0, 0); // Retourne un vecteur nul si la longueur est (presque) zéro
        }
        // Réutilise la méthode de multiplication par un scalaire
        return this.multiply(1.0 / len);
    }
}