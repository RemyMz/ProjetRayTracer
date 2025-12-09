package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Nœud d'une structure BVH (Bounding Volume Hierarchy).
 * <p>
 * Cette classe organise les objets géométriques en un arbre binaire de volumes englobants.
 * Elle permet d'accélérer considérablement le lancer de rayons en éliminant rapidement
 * des groupes entiers d'objets qui ne sont pas traversés par le rayon.
 */
public class BvhNode extends Shape {

    private final Shape left;
    private final Shape right;
    private final AABB box;

    /**
     * Construit un nœud BVH (et récursivement tout l'arbre) à partir d'une liste de formes.
     * <p>
     * L'algorithme divise la liste en deux sous-groupes (gauche et droite) :
     * 1. Choisit un axe aléatoire (X, Y ou Z).
     * 2. Trie les objets selon leur position sur cet axe.
     * 3. Scinde la liste en deux au milieu (médiane).
     *
     * @param shapes La liste des formes à organiser dans ce nœud.
     */
    public BvhNode(List<Shape> shapes) {
        // Un nœud BVH n'a pas de matériau propre, on passe des valeurs neutres/vides.
        super(new Color(), new Color(), 0);

        // 1. Choix d'un axe aléatoire pour le tri (0=X, 1=Y, 2=Z)
        int axis = (int) (Math.random() * 3);

        // Comparateur pour trier les formes selon les coordonnées de leur boîte englobante
        Comparator<Shape> comparator = (a, b) -> {
            double coordA = getAxisValue(a, axis);
            double coordB = getAxisValue(b, axis);
            return Double.compare(coordA, coordB);
        };

        shapes.sort(comparator);

        // 2. Construction récursive de l'arbre
        int size = shapes.size();
        if (size == 1) {
            // Feuille unique : les deux enfants pointent vers le même objet
            this.left = shapes.get(0);
            this.right = shapes.get(0);
        } else if (size == 2) {
            // Deux objets : un pour chaque branche
            this.left = shapes.get(0);
            this.right = shapes.get(1);
        } else {
            // Plus de 2 objets : on divise la liste en deux et on crée des sous-nœuds
            this.left = new BvhNode(shapes.subList(0, size / 2));
            this.right = new BvhNode(shapes.subList(size / 2, size));
        }

        // 3. Calcul de la boîte englobante de ce nœud (Union des enfants)
        this.box = AABB.surround(left.getBoundingBox(), right.getBoundingBox());
    }

    /**
     * Recherche l'intersection la plus proche avec le contenu de ce nœud.
     * <p>
     * Optimisation majeure : si le rayon ne touche pas la boîte englobante (AABB) de ce nœud,
     * on ignore immédiatement tout son contenu (enfants inclus).
     * * @param ray Le rayon à tester.
     * @return L'intersection la plus proche trouvée dans ce sous-arbre, ou vide.
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        // Test rapide sur la boîte englobante
        if (!box.intersect(ray)) {
            return Optional.empty();
        }

        // Si la boîte est touchée, on teste récursivement les enfants
        Optional<Intersection> hitLeft = left.findIntersection(ray);
        Optional<Intersection> hitRight = right.findIntersection(ray);

        // On retourne l'intersection la plus proche des deux (si elles existent)
        if (hitLeft.isPresent() && hitRight.isPresent()) {
            return hitLeft.get().getDistance() < hitRight.get().getDistance() ? hitLeft : hitRight;
        } else if (hitLeft.isPresent()) {
            return hitLeft;
        } else {
            return hitRight;
        }
    }

    /**
     * Retourne la boîte englobante de ce nœud.
     * @return L'objet AABB qui contient tous les enfants de ce nœud.
     */
    @Override
    public AABB getBoundingBox() {
        return box;
    }

    /**
     * Opération non supportée.
     * <p>
     * Un nœud BVH est un conteneur abstrait, il n'a pas de surface physique.
     * L'intersection finale renverra toujours la forme géométrique réelle (feuille de l'arbre),
     * jamais le nœud BVH lui-même.
     *
     * @param p Le point sur la surface.
     * @return Jamais.
     * @throws UnsupportedOperationException Toujours.
     */
    @Override
    public Vector getNormalAt(Point p) {
        throw new UnsupportedOperationException("Impossible de calculer la normale sur un nœud BVH abstrait.");
    }

    // --- Utilitaire interne pour récupérer la coordonnée selon l'axe choisi ---

    private double getAxisValue(Shape s, int axis) {
        Point min = s.getBoundingBox().min;
        return switch (axis) {
            case 0 -> min.getX();
            case 1 -> min.getY();
            default -> min.getZ();
        };
    }
}