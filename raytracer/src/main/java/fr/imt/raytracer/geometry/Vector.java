package fr.imt.raytracer.geometry;

/**
 * Représente un vecteur mathématique dans l'espace 3D.
 * <p>
 * Contrairement à un {@link Point} qui définit une position absolue, un vecteur représente
 * une direction et une magnitude (longueur). Il est utilisé pour modéliser les rayons lumineux,
 * les normales aux surfaces, et les déplacements.
 */
public class Vector extends AbstractVec3 {

    /**
     * Construit un nouveau vecteur avec les composantes spécifiées.
     *
     * @param x Composante X.
     * @param y Composante Y.
     * @param z Composante Z.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Additionne ce vecteur avec un autre.
     * <p>
     * Mathématiquement : $\vec{w} = \vec{u} + \vec{v}$.
     *
     * @param other Le vecteur à ajouter.
     * @return Le vecteur résultant de la somme.
     */
    public Vector add(Vector other) {
        return new Vector(
            this.x + other.x,
            this.y + other.y,
            this.z + other.z
        );
    }

    /**
     * Soustrait un autre vecteur de ce vecteur.
     * <p>
     * Mathématiquement : $\vec{w} = \vec{u} - \vec{v}$.
     *
     * @param other Le vecteur à soustraire.
     * @return Le vecteur résultant de la différence.
     */
    public Vector sub(Vector other) {
        return new Vector(
            this.x - other.x,
            this.y - other.y,
            this.z - other.z
        );
    }

    /**
     * Multiplie ce vecteur par un scalaire.
     * <p>
     * Mathématiquement : $\vec{w} = k \cdot \vec{u}$.
     * Permet d'étirer (k > 1), rétrécir (0 < k < 1) ou inverser (k < 0) le vecteur.
     *
     * @param scalar Le facteur multiplicatif.
     * @return Le vecteur mis à l'échelle.
     */
    public Vector mult(double scalar) {
        return new Vector(
            this.x * scalar,
            this.y * scalar,
            this.z * scalar
        );
    }

    /**
     * Calcule le produit scalaire (Dot Product) avec un autre vecteur.
     * <p>
     * Mathématiquement : $\vec{u} \cdot \vec{v} = x_u x_v + y_u y_v + z_u z_v$.
     * <p>
     * Le produit scalaire est lié au cosinus de l'angle entre les deux vecteurs.
     * Il est essentiel pour les calculs d'éclairage (Loi de Lambert).
     *
     * @param other L'autre vecteur.
     * @return La valeur scalaire résultante.
     */
    public double dot(Vector other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
    }

    /**
     * Calcule le produit vectoriel (Cross Product) avec un autre vecteur.
     * <p>
     * Mathématiquement : $\vec{w} = \vec{u} \times \vec{v}$.
     * <p>
     * Le vecteur résultant est perpendiculaire au plan formé par les deux vecteurs d'entrée.
     * Utilisé pour calculer les normales des triangles et construire des repères orthonormés.
     *
     * @param other L'autre vecteur.
     * @return Un nouveau vecteur orthogonal à 'this' et 'other'.
     */
    public Vector cross(Vector other) {
        return new Vector(
            (this.y * other.z) - (this.z * other.y),
            (this.z * other.x) - (this.x * other.z),
            (this.x * other.y) - (this.y * other.x)
        );
    }

    /**
     * Calcule la longueur (norme euclidienne) du vecteur.
     * <p>
     * Mathématiquement : $||\vec{u}|| = \sqrt{\vec{u} \cdot \vec{u}}$.
     *
     * @return La magnitude du vecteur.
     */
    public double length() {
        return Math.sqrt(this.dot(this));
    }

    /**
     * Retourne une version normalisée de ce vecteur (longueur = 1).
     * <p>
     * Si le vecteur est nul (longueur proche de 0), retourne un vecteur nul pour éviter
     * la division par zéro.
     *
     * @return Le vecteur unitaire pointant dans la même direction.
     */
    public Vector norm() {
        double len = length();
        
        // Sécurité contre la division par zéro
        if (len < EPSILON) {
            return new Vector(0, 0, 0);
        }
        
        return mult(1.0 / len);
    }

    @Override
    public String toString() {
        return String.format("Vector(%.2f, %.2f, %.2f)", x, y, z);
    }
}