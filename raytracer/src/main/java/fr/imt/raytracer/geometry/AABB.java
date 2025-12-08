package fr.imt.raytracer.geometry;

/**
 * Boîte englobante alignée sur les axes (AABB).
 * Permet d'optimiser les intersections en testant d'abord la boîte avant l'objet.
 */
public class AABB {
    public final Point min;
    public final Point max;

    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Test d'intersection rapide (Algorithme "Slab").
     * Retourne true si le rayon traverse la boîte.
     */
    public boolean intersect(Ray ray) {
        double tmin = (min.getX() - ray.getOrigin().getX()) / ray.getDirection().getX();
        double tmax = (max.getX() - ray.getOrigin().getX()) / ray.getDirection().getX();

        if (tmin > tmax) { double temp = tmin; tmin = tmax; tmax = temp; }

        double tymin = (min.getY() - ray.getOrigin().getY()) / ray.getDirection().getY();
        double tymax = (max.getY() - ray.getOrigin().getY()) / ray.getDirection().getY();

        if (tymin > tymax) { double temp = tymin; tymin = tymax; tymax = temp; }

        if ((tmin > tymax) || (tymin > tmax)) return false;

        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;

        double tzmin = (min.getZ() - ray.getOrigin().getZ()) / ray.getDirection().getZ();
        double tzmax = (max.getZ() - ray.getOrigin().getZ()) / ray.getDirection().getZ();

        if (tzmin > tzmax) { double temp = tzmin; tzmin = tzmax; tzmax = temp; }

        return !((tmin > tzmax) || (tzmin > tmax));
    }
    
    // Fusionner deux boîtes pour en faire une plus grande
    public static AABB surround(AABB box0, AABB box1) {
        Point small = new Point(
            Math.min(box0.min.getX(), box1.min.getX()),
            Math.min(box0.min.getY(), box1.min.getY()),
            Math.min(box0.min.getZ(), box1.min.getZ())
        );
        Point big = new Point(
            Math.max(box0.max.getX(), box1.max.getX()),
            Math.max(box0.max.getY(), box1.max.getY()),
            Math.max(box0.max.getZ(), box1.max.getZ())
        );
        return new AABB(small, big);
    }
}