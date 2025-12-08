package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class BvhNode extends Shape {
    private final Shape left;
    private final Shape right;
    private final AABB box;

    public BvhNode(List<Shape> shapes) {
        super(new Color(), new Color(), 0); // Matériau bidon, c'est un conteneur

        // 1. Choix d'un axe aléatoire pour trier (x=0, y=1, z=2)
        int axis = (int) (Math.random() * 3);
        Comparator<Shape> comparator = (a, b) -> {
            double coordA = axis == 0 ? a.getBoundingBox().min.getX() : (axis == 1 ? a.getBoundingBox().min.getY() : a.getBoundingBox().min.getZ());
            double coordB = axis == 0 ? b.getBoundingBox().min.getX() : (axis == 1 ? b.getBoundingBox().min.getY() : b.getBoundingBox().min.getZ());
            return Double.compare(coordA, coordB);
        };

        shapes.sort(comparator);

        if (shapes.size() == 1) {
            this.left = shapes.get(0);
            this.right = shapes.get(0);
        } else if (shapes.size() == 2) {
            this.left = shapes.get(0);
            this.right = shapes.get(1);
        } else {
            this.left = new BvhNode(shapes.subList(0, shapes.size() / 2));
            this.right = new BvhNode(shapes.subList(shapes.size() / 2, shapes.size()));
        }

        this.box = AABB.surround(left.getBoundingBox(), right.getBoundingBox());
    }

    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        if (!box.intersect(ray)) return Optional.empty(); // Si on rate la boîte, on ignore tout le contenu !

        Optional<Intersection> hitLeft = left.findIntersection(ray);
        Optional<Intersection> hitRight = right.findIntersection(ray);

        if (hitLeft.isPresent() && hitRight.isPresent()) {
            return hitLeft.get().getDistance() < hitRight.get().getDistance() ? hitLeft : hitRight;
        }
        return hitLeft.isPresent() ? hitLeft : hitRight;
    }

    @Override
    public AABB getBoundingBox() { return box; }
    
    @Override 
    public Vector getNormalAt(Point p) { throw new UnsupportedOperationException(); }
}