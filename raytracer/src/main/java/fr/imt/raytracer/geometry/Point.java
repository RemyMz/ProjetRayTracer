package fr.imt.raytracer.geometry;

/**
 * Représente un Point 3D dans l'espace.
 * Implémente les opérations spécifiques aux points du Jalon 1.
 */
public class Point extends AbstractVec3 {

    /**
     * Constructeur pour un point.
     * @param x coordonnée x
     * @param y coordonnée y
     * @param z coordonnée z
     */
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Soustraction de deux points, qui résulte en un vecteur. (Jalon 1)
     * Opération: this - other
     * @param other Le point à soustraire.
     * @return Un nouveau Vecteur (this - other).
     */
    public Vector subtract(Point other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Addition d'un point et d'un vecteur, qui résulte en un nouveau point. (Jalon 1)
     * Opération: this + vector
     * @param vector Le vecteur à ajouter.
     * @return Un nouveau Point (this + vector).
     */
    public Point add(Vector vector) {
        // Doit utiliser getX(), getY(), getZ() car x, y, z sont 'protected'
        // dans AbstractVec3 et 'vector' est de type Vector, pas Point.
        // Bien que dans ce cas, 'vector.x' fonctionnerait car ils sont
        // dans le même package 'geometry'. Utiliser les getters est plus propre.
        return new Point(this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
    }

    /**
     * Soustraction d'un Vecteur d'un Point.
     * (Logique inverse de l'addition)
     * @param v Le vecteur à soustraire.
     * @return Un nouveau Point (this - v).
     */
    public Point subtract(Vector v) {
        return new Point(this.x - v.getX(), this.y - v.getY(), this.z - v.getZ());
    }
    
    /**
     * Multiplication par un scalaire.
     * (Jalon 1 - Table: "Point" * "Multiplication par un scalaire" = "Oui")
     * @param scalar Le scalaire.
     * @return Un nouveau Point (this * scalar).
     */
    public Point multiply(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }
}