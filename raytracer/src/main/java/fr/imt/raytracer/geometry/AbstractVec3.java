package fr.imt.raytracer.geometry;

import java.util.Objects;

/**
 * Classe de base abstraite représentant un tuple de coordonnées en 3 dimensions.
 * <p>
 * Sert de fondation commune pour les points (position) et les vecteurs (direction),
 * en centralisant la gestion des composantes (x, y, z) et la précision des calculs flottants.
 */
public abstract class AbstractVec3 {

    /**
     * Tolérance pour les comparaisons de nombres flottants (double).
     * Permet de gérer les imprécisions inhérentes aux calculs vectoriels.
     */
    public static final double EPSILON = 1e-6;

    protected final double x;
    protected final double y;
    protected final double z;

    /**
     * Construit un nouveau tuple 3D.
     *
     * @param x Composante X.
     * @param y Composante Y.
     * @param z Composante Z.
     */
    public AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retourne la composante X.
     * @return La valeur de x.
     */
    public double getX() {
        return x;
    }

    /**
     * Retourne la composante Y.
     * @return La valeur de y.
     */
    public double getY() {
        return y;
    }

    /**
     * Retourne la composante Z.
     * @return La valeur de z.
     */
    public double getZ() {
        return z;
    }

    /**
     * Opération de soustraction générique.
     * <p>
     * Cette méthode lève une exception par défaut car la sémantique de la soustraction
     * dépend du type concret (ex: Point - Point = Vector, Vector - Vector = Vector).
     *
     * @param other L'autre tuple à soustraire.
     * @return Rien, lève une exception.
     * @throws UnsupportedOperationException Toujours, pour forcer l'usage des sous-classes.
     */
    public AbstractVec3 sub(AbstractVec3 other) {
        throw new UnsupportedOperationException(
            "Opération non supportée sur le type abstrait. Utilisez les méthodes spécifiques de Point ou Vector."
        );
    }

    /**
     * Vérifie l'égalité entre deux tuples en tenant compte de la tolérance EPSILON.
     *
     * @param o L'objet à comparer.
     * @return true si les composantes sont proches à EPSILON près, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        AbstractVec3 that = (AbstractVec3) o;
        
        return Math.abs(that.x - x) < EPSILON &&
               Math.abs(that.y - y) < EPSILON &&
               Math.abs(that.z - z) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}