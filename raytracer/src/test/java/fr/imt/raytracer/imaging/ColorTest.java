package fr.imt.raytracer.imaging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires pour la classe Color.
 * (Jalon 1 - Aide 4)
 */
class ColorTest {

    // Epsilon pour la comparaison des 'double'
    private static final double EPSILON = 1e-9;

    @Test
    @DisplayName("Test du constructeur par défaut (Noir)")
    void testDefaultConstructor() {
        Color black = new Color();
        // On vérifie que les composantes sont bien (0, 0, 0)
        assertEquals(0.0, black.getX(), EPSILON); // getX() est 'r'
        assertEquals(0.0, black.getY(), EPSILON); // getY() est 'g'
        assertEquals(0.0, black.getZ(), EPSILON); // getZ() est 'b'
    }

    @Test
    @DisplayName("Test du 'clamping' (valeurs > 1)")
    void testClampingHigh() {
        // Le constructeur doit borner les valeurs à 1.0
        Color overWhite = new Color(1.5, 2.0, 100.0);
        Color white = new Color(1, 1, 1);
        assertEquals(white, overWhite);
    }

    @Test
    @DisplayName("Test du 'clamping' (valeurs < 0)")
    void testClampingLow() {
        // Le constructeur doit borner les valeurs à 0.0
        Color underBlack = new Color(-0.5, -2.0, -100.0);
        Color black = new Color(0, 0, 0);
        assertEquals(black, underBlack);
    }
    
    @Test
    @DisplayName("Test du 'clamping' (valeurs mixtes)")
    void testClampingMixed() {
        Color mixed = new Color(-0.2, 0.5, 1.2);
        Color expected = new Color(0.0, 0.5, 1.0);
        assertEquals(expected, mixed);
    }

    @Test
    @DisplayName("Test de l'addition de couleurs (avec clamping)")
    void testAdd() {
        Color c1 = new Color(0.8, 0.2, 0.5);
        Color c2 = new Color(0.3, 0.1, 0.1);
        // 0.8 + 0.3 = 1.1 -> clampé à 1.0
        // 0.2 + 0.1 = 0.3
        // 0.5 + 0.1 = 0.6
        Color expected = new Color(1.0, 0.3, 0.6);
        assertEquals(expected, c1.add(c2));
    }

    @Test
    @DisplayName("Test de la multiplication par un scalaire")
    void testMultiplyScalar() {
        Color c = new Color(0.2, 0.4, 0.8);
        Color expectedClamped = new Color(0.4, 0.8, 1.0);
        
        assertEquals(expectedClamped, c.multiply(2.0));
    }

    @Test
    @DisplayName("Test du produit de Schur")
    void testSchurProduct() {
        // Utilisé pour (couleur_lumière * couleur_objet)
        Color light = new Color(1.0, 0.5, 0.5);
        Color object = new Color(0.5, 1.0, 0.5);
        // 1.0 * 0.5 = 0.5
        // 0.5 * 1.0 = 0.5
        // 0.5 * 0.5 = 0.25
        Color expected = new Color(0.5, 0.5, 0.25);
        assertEquals(expected, light.schur(object));
    }

    @Test
    @DisplayName("Test de la conversion toRGB()")
    void testToRGB() {
        // (Jalon 1 - Aide 5)
        Color black = new Color(0, 0, 0);
        Color white = new Color(1, 1, 1);
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        Color blue = new Color(0, 0, 1);
        
        // Test d'un gris moyen
        // Math.round(0.5 * 255) = 128
        Color gray = new Color(0.5, 0.5, 0.5);

        // Les valeurs sont en hexadécimal (0xRRGGBB)
        assertEquals(0x000000, black.toRGB());
        assertEquals(0xFFFFFF, white.toRGB());
        assertEquals(0xFF0000, red.toRGB());
        assertEquals(0x00FF00, green.toRGB());
        assertEquals(0x0000FF, blue.toRGB());
        assertEquals(0x808080, gray.toRGB()); // 128 = 0x80
    }
}