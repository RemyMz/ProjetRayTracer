package fr.imt.raytracer.geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires pour la classe Vector.
 * Mis à jour pour correspondre aux méthodes (sub, mult, norm).
 */
class VectorTest {

    // On utilise la constante définie dans AbstractVec3 pour la cohérence
    private static final double EPSILON = AbstractVec3.EPSILON;

    @Test
    @DisplayName("Test de l'addition de deux vecteurs")
    void testAddition() {
        // Arrange
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector expected = new Vector(5, 7, 9);

        // Act
        Vector result = v1.add(v2);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test de la soustraction de deux vecteurs")
    void testSubtraction() {
        Vector v1 = new Vector(10, 5, 2);
        Vector v2 = new Vector(2, 1, 1);
        Vector expected = new Vector(8, 4, 1);

        // Correction : 'subtract' devient 'sub'
        Vector result = v1.sub(v2);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test de la multiplication par un scalaire")
    void testMultiplyScalar() {
        Vector v = new Vector(1, -2, 3);
        double scalar = 3.5;
        Vector expected = new Vector(3.5, -7, 10.5);

        // Correction : 'multiply' devient 'mult'
        Vector result = v.mult(scalar);

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

        assertEquals(expected, result, EPSILON);
    }

    @Test
    @DisplayName("Test du produit vectoriel (Cross Product)")
    void testCrossProduct() {
        // X(1,0,0) cross Y(0,1,0) -> Z(0,0,1)
        Vector vX = new Vector(1, 0, 0);
        Vector vY = new Vector(0, 1, 0);
        Vector expectedZ = new Vector(0, 0, 1);

        Vector result = vX.cross(vY);

        assertEquals(expectedZ, result);
    }

    @Test
    @DisplayName("Test de la longueur d'un vecteur")
    void testLength() {
        Vector v = new Vector(3, 4, 0);
        double expected = 5.0;

        double result = v.length();

        assertEquals(expected, result, EPSILON);
    }

    @Test
    @DisplayName("Test de la normalisation d'un vecteur")
    void testNormalize() {
        Vector v = new Vector(5, 0, 0); 
        Vector expected = new Vector(1, 0, 0); 

        // Correction : 'normalize' devient 'norm'
        Vector result = v.norm();

        assertEquals(expected, result);
        assertEquals(1.0, result.length(), EPSILON);
    }
}