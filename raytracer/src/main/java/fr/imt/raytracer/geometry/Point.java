package fr.imt.raytracer.geometry;

/**
 * Représente une position fixe dans l'espace 3D.
 * <p>
 * Contrairement à un vecteur (qui a une direction et une magnitude), un point définit un emplacement.
 * Les opérations arithmétiques sur les points suivent des règles géométriques strictes :
 * <ul>
 * <li>Point - Point = Vecteur (déplacement)</li>
 * <li>Point + Vecteur = Point (translation)</li>
 * <li>Point - Vecteur = Point (translation inverse)</li>
 * </ul>
 */
public class Point extends AbstractVec3 {

    /**
     * Crée un nouveau point à la position spécifiée.
     *
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Calcule le vecteur déplacement entre ce point et un autre.
     * <p>
     * Mathématiquement : $\vec{v} = P_{this} - P_{other}$.
     * Le vecteur résultant pointe de 'other' vers 'this'.
     *
     * @param other Le point d'origine de la soustraction.
     * @return Un vecteur représentant la direction et la distance entre les deux points.
     */
    public Vector sub(Point other) {
        return new Vector(
            this.x - other.x,
            this.y - other.y,
            this.z - other.z
        );
    }

    /**
     * Translate ce point selon un vecteur donné.
     * <p>
     * Mathématiquement : $P_{new} = P_{this} + \vec{v}$.
     *
     * @param vector Le vecteur de déplacement à appliquer.
     * @return Un nouveau point correspondant à la nouvelle position.
     */
    public Point add(Vector vector) {
        return new Point(
            this.x + vector.getX(),
            this.y + vector.getY(),
            this.z + vector.getZ()
        );
    }

    /**
     * Translate ce point dans la direction opposée d'un vecteur.
     * <p>
     * Mathématiquement : $P_{new} = P_{this} - \vec{v}$.
     *
     * @param vector Le vecteur de déplacement inverse.
     * @return Un nouveau point correspondant à la nouvelle position.
     */
    public Point sub(Vector vector) {
        return new Point(
            this.x - vector.getX(),
            this.y - vector.getY(),
            this.z - vector.getZ()
        );
    }

    @Override
    public String toString() {
        return String.format("Point(%.2f, %.2f, %.2f)", x, y, z);
    }
}