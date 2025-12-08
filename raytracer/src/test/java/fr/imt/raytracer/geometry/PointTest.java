package fr.imt.raytracer.geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires pour la classe Point.
 * Mis à jour pour correspondre à l'implémentation actuelle.
 */
class PointTest {

    @Test
    @DisplayName("Test Point - Point = Vector")
    void testSubPoints() {
        // Arrange
        Point p1 = new Point(5, 8, 10);
        Point p2 = new Point(2, 3, 4);
        Vector expected = new Vector(3, 5, 6);

        // Act
        // Utilisation de la méthode 'sub' définie dans Point.java
        Vector result = p1.sub(p2);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test Point + Vector = Point")
    void testAddVector() {
        // Arrange
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(10, 20, 30);
        Point expected = new Point(11, 22, 33);

        // Act
        // Utilisation de la méthode 'add' définie dans Point.java
        Point result = p.add(v);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test Point - Vector = Point")
    void testSubVector() {
        // Arrange
        Point p = new Point(10, 20, 30);
        Vector v = new Vector(1, 2, 3);
        Point expected = new Point(9, 18, 27);

        // Act
        // Utilisation de la méthode 'sub' définie dans Point.java
        Point result = p.sub(v);

        // Assert
        assertEquals(expected, result);
    }
}