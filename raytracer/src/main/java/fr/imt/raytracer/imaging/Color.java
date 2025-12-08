package fr.imt.raytracer.imaging;

import fr.imt.raytracer.geometry.AbstractVec3;

/**
 * Représente une couleur trichromatique (Rouge, Vert, Bleu).
 * <p>
 * Les composantes sont stockées sous forme de flottants (double).
 * Bien que l'affichage final soit borné entre [0, 1], les calculs intermédiaires
 * peuvent dépasser cette plage (High Dynamic Range) pour simuler des lumières intenses.
 */
public class Color extends AbstractVec3 {

    // Redéfinition locale pour la clarté sémantique (R, G, B au lieu de X, Y, Z)
    // Note : On pourrait techniquement utiliser super.x/y/z, mais r/g/b est plus lisible.
    private final double r;
    private final double g;
    private final double b;

    /**
     * Construit une couleur à partir de ses composantes.
     *
     * @param r Composante Rouge.
     * @param g Composante Verte.
     * @param b Composante Bleue.
     */
    public Color(double r, double g, double b) {
        super(r, g, b); // Stockage dans le vecteur parent pour la compatibilité
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construit une couleur noire par défaut (0, 0, 0).
     */
    public Color() {
        this(0.0, 0.0, 0.0);
    }

    // --- Accesseurs ---

    public double getR() { return r; }
    public double getG() { return g; }
    public double getB() { return b; }

    // --- Opérations Arithmétiques ---

    /**
     * Additionne deux couleurs (mélange additif de lumière).
     * <p>
     * Le résultat est automatiquement borné (clamp) pour rester affichable.
     *
     * @param other La couleur à ajouter.
     * @return La nouvelle couleur résultante, bornée à [0, 1].
     */
    public Color add(Color other) {
        return new Color(
            this.r + other.r,
            this.g + other.g,
            this.b + other.b
        ).clamp();
    }

    /**
     * Multiplie cette couleur par un scalaire (intensité).
     * <p>
     * Le résultat N'EST PAS borné pour permettre les calculs HDR.
     *
     * @param scalar Le facteur d'intensité.
     * @return La couleur amplifiée ou atténuée.
     */
    public Color mult(double scalar) {
        return new Color(
            this.r * scalar,
            this.g * scalar,
            this.b * scalar
        );
    }

    /**
     * Effectue le produit de Schur (multiplication composante par composante).
     * <p>
     * Utilisé pour appliquer la couleur d'un matériau sous une certaine lumière :
     * (R1*R2, G1*G2, B1*B2). C'est un filtre de couleur.
     *
     * @param other La couleur filtrante (ex: couleur de l'objet).
     * @return La couleur résultante de l'interaction.
     */
    public Color schurProduct(Color other) {
        return new Color(
            this.r * other.r,
            this.g * other.g,
            this.b * other.b
        );
    }

    // --- Utilitaires ---

    /**
     * Borne les composantes de la couleur dans l'intervalle [0.0, 1.0].
     * Nécessaire avant l'affichage ou l'enregistrement en format standard.
     *
     * @return Une nouvelle couleur valide pour l'affichage.
     */
    public Color clamp() {
        return new Color(
            Math.min(1.0, Math.max(0.0, this.r)),
            Math.min(1.0, Math.max(0.0, this.g)),
            Math.min(1.0, Math.max(0.0, this.b))
        );
    }

    /**
     * Vérifie si la couleur a une contribution lumineuse significative.
     * Permet d'optimiser le rendu en évitant de calculer des reflets sur des objets noirs.
     *
     * @return true si au moins une composante est supérieure à epsilon.
     */
    public boolean isNonZero() {
        return r > EPSILON || g > EPSILON || b > EPSILON;
    }

    /**
     * Convertit la couleur flottante en un entier RGB 24 bits standard.
     * <p>
     * Format de sortie : 0x00RRGGBB.
     *
     * @return L'entier représentant la couleur pour BufferedImage.
     */
    public int toRGB() {
        Color clamped = this.clamp();

        // Conversion [0.0, 1.0] -> [0, 255]
        int ri = (int) Math.round(clamped.r * 255.0);
        int gi = (int) Math.round(clamped.g * 255.0);
        int bi = (int) Math.round(clamped.b * 255.0);

        // Assemblage des octets
        return ((ri & 0xFF) << 16) | ((gi & 0xFF) << 8) | (bi & 0xFF);
    }
}