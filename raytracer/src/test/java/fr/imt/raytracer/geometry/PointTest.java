package fr.imt.raytracer.geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitaires pour la classe Point.
 * (Jalon 1 - Aide 4)
 */
class PointTest {

    @Test
    @DisplayName("Test Point - Point = Vector")
    void testSubtractPoints() {
        // Arrange
        Point p1 = new Point(5, 8, 10);
        Point p2 = new Point(2, 3, 4);
        Vector expected = new Vector(3, 5, 6);

        // Act
        Vector result = p1.subtract(p2);

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
        Point result = p.add(v);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test Point - Vector = Point")
    void testSubtractVector() {
        // Arrange
        Point p = new Point(10, 20, 30);
        Vector v = new Vector(1, 2, 3);
        Point expected = new Point(9, 18, 27);

        // Act
        Point result = p.subtract(v);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test Point * scalar = Point")
    void testMultiplyScalar() {
        // Arrange
        Point p = new Point(2, 3, 4);
        double scalar = 2.5;
        Point expected = new Point(5, 7.5, 10);
        
        // Act
        Point result = p.multiply(scalar);

        // Assert
        assertEquals(expected, result);
    }

}