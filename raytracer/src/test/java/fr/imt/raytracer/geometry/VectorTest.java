package fr.imt.raytracer.geometry;

// On importe les classes de JUnit 5 pour les tests
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// On importe la classe 'Assertions' pour pouvoir écrire assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires pour la classe Vector.
 * (Jalon 1 - Aide 4)
 */
class VectorTest {

    // On définit une petite marge d'erreur (epsilon) pour comparer les 'double'
    // C'est nécessaire à cause de l'imprécision des calculs en virgule flottante.
    private static final double EPSILON = 1e-9;

    @Test
    @DisplayName("Test de l'addition de deux vecteurs")
    void testAddition() {
        // Arrange (Préparer)
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector expected = new Vector(5, 7, 9); // Le résultat attendu

        // Act (Agir)
        Vector result = v1.add(v2);

        // Assert (Vérifier)
        // On utilise assertEquals(expected, result).
        // Cela fonctionne car nous avons défini la méthode equals()
        // dans AbstractVec3, qui compare les 'double' avec précision !
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test de la soustraction de deux vecteurs")
    void testSubtraction() {
        Vector v1 = new Vector(10, 5, 2);
        Vector v2 = new Vector(2, 1, 1);
        Vector expected = new Vector(8, 4, 1);

        Vector result = v1.subtract(v2);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test de la multiplication par un scalaire")
    void testMultiplyScalar() {
        Vector v = new Vector(1, -2, 3);
        double scalar = 3.5;
        Vector expected = new Vector(3.5, -7, 10.5);

        Vector result = v.multiply(scalar);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test du produit scalaire (Dot Product)")
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        double expected = 32.0;

        double result = v1.dot(v2);

        // Ici, on compare des 'double' directement, on doit fournir l'epsilon
        assertEquals(expected, result, EPSILON);
    }

    @Test
    @DisplayName("Test du produit vectoriel (Cross Product)")
    void testCrossProduct() {
        // Test standard : X(1,0,0) cross Y(0,1,0) doit donner Z(0,0,1)
        Vector vX = new Vector(1, 0, 0);
        Vector vY = new Vector(0, 1, 0);
        Vector expectedZ = new Vector(0, 0, 1);

        Vector result = vX.cross(vY);

        assertEquals(expectedZ, result);
    }

    @Test
    @DisplayName("Test de la longueur d'un vecteur (Pythagore)")
    void testLength() {
        // Un triplet pythagoricien simple : 3, 4, 0
        Vector v = new Vector(3, 4, 0);
        // sqrt(3*3 + 4*4 + 0*0) = sqrt(9 + 16) = sqrt(25) = 5
        double expected = 5.0;

        double result = v.length();

        assertEquals(expected, result, EPSILON);
    }

    @Test
    @DisplayName("Test de la normalisation d'un vecteur")
    void testNormalize() {
        Vector v = new Vector(5, 0, 0); // Un vecteur de longueur 5
        Vector expected = new Vector(1, 0, 0); // Le même vecteur de longueur 1

        Vector result = v.normalize();

        assertEquals(expected, result);

        // On peut aussi vérifier que sa longueur est bien 1
        assertEquals(1.0, result.length(), EPSILON);
    }
}