package fr.imt.raytracer.imaging;

import fr.imt.raytracer.geometry.AbstractVec3;

/**
 * Représente une couleur RGB.
 * Les composantes (r, g, b) sont stockées en tant que (x, y, z) de la classe parente.
 * (Jalon 1)
 */
public class Color extends AbstractVec3 {

    /**
     * Constructeur par défaut (Noir).
     * (Jalon 1 - Aide 2)
     */
    public Color() {
        super(0, 0, 0);
    }

    /**
     * Constructeur principal pour une couleur.
     * S'assure que les valeurs sont "clampées" (limitées) entre 0 et 1.
     * (Jalon 1 - Spécifications)
     *
     * @param r Composante rouge (sera limitée entre 0 et 1)
     * @param g Composante verte (sera limitée entre 0 et 1)
     * @param b Composante bleue (sera limitée entre 0 et 1)
     */
    public Color(double r, double g, double b) {
        super(clamp(r), clamp(g), clamp(b));
    }

    /**
     * Méthode utilitaire privée pour borner une valeur entre 0.0 et 1.0.
     */
    private static double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }

    /**
     * Addition de deux couleurs. (Jalon 1)
     * Le résultat est clampé par le constructeur.
     * @param other L'autre couleur.
     * @return Une nouvelle couleur (this + other).
     */
    public Color add(Color other) {
        return new Color(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Multiplication par un scalaire (augmente ou réduit la luminosité). (Jalon 1)
     * Le résultat est clampé par le constructeur.
     * @param scalar Le scalaire.
     * @return Une nouvelle couleur (this * scalar).
     */
    public Color multiply(double scalar) {
        return new Color(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Produit de Schur (multiplication composante par composante). (Jalon 1)
     * Utilisé pour combiner des couleurs (ex: couleur de la lumière * couleur de l'objet).
     * Le résultat est clampé par le constructeur.
     * @param other L'autre couleur.
     * @return Une nouvelle couleur (this * other).
     */
    public Color schur(Color other) {
        return new Color(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    /**
     * Convertit la couleur (flottants 0-1) en un entier RGB 24 bits.
     * (Jalon 1 - Aide 5)
     * @return un entier ARGB (Alpha est 255).
     */
    public int toRGB() {
        // x, y, z sont hérités de AbstractVec3 et représentent r, g, b
        // Ils sont déjà garantis entre 0 et 1 par le constructeur.
        int red = (int) Math.round(this.x * 255);
        int green = (int) Math.round(this.y * 255);
        int blue = (int) Math.round(this.z * 255);

        // Formule du Jalon 1 - Aide 5
        // (Assemble les 3 composantes de 8 bits en un seul entier de 32 bits)
        // (red & 0xff) << 16   -> Décalage des bits rouges
        // (green & 0xff) << 8  -> Décalage des bits verts
        // (blue & 0xff)        -> Bits bleus
        return ((red & 0xff) << 16) |
               ((green & 0xff) << 8) |
               (blue & 0xff);
    }
}