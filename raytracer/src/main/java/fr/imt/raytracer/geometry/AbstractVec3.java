package fr.imt.raytracer.geometry;

import java.util.Objects;

/**
 * Classe abstraite de base pour les objets à 3 dimensions (Vecteurs, Points, Couleurs).
 * Contient les composantes de base (x, y, z) et les opérations communes.
 * (Jalon 1)
 */
public abstract class AbstractVec3 {

    // Epsilon pour la comparaison des doubles (Jalon 1 - Aide 3)
    private static final double EPSILON = 1e-9;

    protected final double x;
    protected final double y;
    protected final double z;

    /**
     * Constructeur principal.
     * @param x composante x
     * @param y composante y
     * @param z composante z
     */
    public AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // --- Getters ---
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Produit scalaire (Dot product).
     * (x1*x2 + y1*y2 + z1*z2)
     * Opération commune listée pour Point, Vecteur et Couleur dans le Jalon 1.
     * @param other L'autre Vec3
     * @return Le résultat (double) du produit scalaire.
     */
    public double dot(AbstractVec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }


    // --- Utilitaires (Jalon 1 - Aide 2) ---

    @Override
    public String toString() {
        // %f est utilisé pour les doubles (flottants)
        return String.format("(%f, %f, %f)", x, y, z);
    }

    /**
     * Compare deux doubles avec une tolérance (epsilon).
     * (Jalon 1 - Aide 3)
     */
    protected static boolean nearlyEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Méthode equals gérant la précision des doubles.
     * (Jalon 1 - Aide 2)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Vérifie si 'o' est une instance de n'importe quelle classe
        // qui hérite de AbstractVec3
        if (o == null || !(o instanceof AbstractVec3)) return false;
        
        AbstractVec3 that = (AbstractVec3) o;
        
        // Comparaison avec epsilon
        return nearlyEquals(that.x, x) &&
               nearlyEquals(that.y, y) &&
               nearlyEquals(that.z, z);
    }

    @Override
    public int hashCode() {
        // Utilise les valeurs de base pour le hashcode
        return Objects.hash(x, y, z);
    }
}