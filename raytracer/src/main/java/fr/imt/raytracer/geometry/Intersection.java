package fr.imt.raytracer.geometry;

/**
 * Une classe de données pour stocker les informations sur une intersection
 * entre un rayon et un objet.
 * (Jalon 3 - Aide 3)
 */
public class Intersection {

    private final double distance; // La distance 't' le long du rayon
    private final Point point;     // Le point 3D de l'intersection
    private final Shape shape;     // L'objet qui a été touché

    /**
     * Construit un nouvel objet Intersection.
     * @param distance La distance 't' du rayon à l'objet.
     * @param point Le point 3D exact de l'impact.
     * @param shape L'objet (ex: Sphère) qui a été touché.
     */
    public Intersection(double distance, Point point, Shape shape) {
        this.distance = distance;
        this.point = point;
        this.shape = shape;
    }

    // --- Getters ---

    public double getDistance() {
        return distance;
    }

    public Point getPoint() {
        return point;
    }

    public Shape getShape() {
        return shape;
    }
}