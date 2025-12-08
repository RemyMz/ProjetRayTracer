package fr.imt.raytracer.renderer;

import fr.imt.raytracer.geometry.*;
import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.scene.*;
import java.util.Optional;

/**
 * Moteur de rendu principal implémentant l'algorithme de Ray Tracing récursif (Whitted-style).
 * <p>
 * Ce moteur gère :
 * <ul>
 * <li>La génération des rayons primaires depuis la caméra.</li>
 * <li>Le calcul des intersections les plus proches.</li>
 * <li>L'éclairage direct (Modèle de Blinn-Phong + Ombres portées).</li>
 * <li>L'éclairage indirect via réflexion spéculaire parfaite (Miroir).</li>
 * </ul>
 */
public class RayTracer {

    private static final Color BLACK = new Color(0, 0, 0);
    private final Scene scene;

    /**
     * Initialise le moteur de rendu avec la scène à dessiner.
     *
     * @param scene La scène contenant les objets, lumières et la caméra.
     */
    public RayTracer(Scene scene) {
        this.scene = scene;
    }

    /**
     * Calcule la couleur finale d'un pixel donné de l'image.
     * <p>
     * Point d'entrée pour le thread de rendu. Génère le rayon primaire et lance la récursion.
     *
     * @param x Coordonnée horizontale du pixel.
     * @param y Coordonnée verticale du pixel.
     * @return La couleur calculée pour ce pixel.
     */
    public Color getPixelColor(int x, int y) {
        Ray viewRay = scene.getCamera().generateRay(x, y, scene.getWidth(), scene.getHeight());
        return calculateColor(viewRay, scene.getMaxDepth());
    }

    /**
     * Méthode récursive centrale du Ray Tracing.
     * <p>
     * Calcule l'illumination au point d'impact du rayon. Si la surface est réfléchissante
     * et que la profondeur de récursion le permet, un rayon secondaire est lancé.
     *
     * @param ray   Le rayon incident (Vue ou Réflexion).
     * @param depth Le compteur de rebonds restants (condition d'arrêt).
     * @return La couleur résultante au point d'impact (ou la couleur de fond).
     */
    private Color calculateColor(Ray ray, int depth) {
        // 1. Condition d'arrêt : Profondeur maximale atteinte
        if (depth <= 0) {
            return BLACK;
        }

        // 2. Recherche de l'intersection la plus proche
        Optional<Intersection> optIntersection = findClosestIntersection(ray);

        if (!optIntersection.isPresent()) {
            return BLACK; // Rien n'est touché, on retourne le fond (noir)
        }

        Intersection intersection = optIntersection.get();
        Point p = intersection.getPoint();
        Vector normal = intersection.getNormal();
        Shape shape = intersection.getShape();

        // 3. Calcul de l'éclairage direct (Locale : Ambiante + Diffuse + Spéculaire)
        Color finalColor = calculateDirectLighting(p, normal, shape);

        // 4. Calcul de l'éclairage indirect (Globale : Réflexion)
        // Uniquement si l'objet est brillant et qu'on peut encore rebondir
        if (shape.getSpecular().isNonZero()) {
            Color reflectionColor = calculateReflection(ray, intersection, depth);
            finalColor = finalColor.add(reflectionColor);
        }

        return finalColor;
    }

    /**
     * Calcule la contribution de la réflexion spéculaire parfaite.
     */
    private Color calculateReflection(Ray incidentRay, Intersection intersection, int currentDepth) {
        Vector d = incidentRay.getDirection();
        Vector n = intersection.getNormal();
        Point p = intersection.getPoint();

        // Calcul du vecteur réfléchi : R = d - 2(d . n)n
        // Note : On inverse le signe selon l'orientation des vecteurs dans l'implémentation
        double dotProduct = n.dot(d.mult(-1));
        Vector r = d.add(n.mult(2 * dotProduct));

        // Lancement du rayon réfléchi
        // Décalage (Bias) par EPSILON pour éviter l'auto-intersection (Shadow Acne)
        Ray reflectionRay = new Ray(p.add(r.mult(AbstractVec3.EPSILON)), r);

        // Appel récursif
        Color reflectedColor = calculateColor(reflectionRay, currentDepth - 1);

        // La couleur réfléchie est teintée par la couleur spéculaire de l'objet
        return intersection.getShape().getSpecular().schurProduct(reflectedColor);
    }

    /**
     * Calcule l'éclairage direct local selon le modèle de Blinn-Phong.
     * Intègre la gestion des ombres portées.
     */
    private Color calculateDirectLighting(Point p, Vector normal, Shape shape) {
        Color lighting = scene.getAmbient(); // Base : Lumière ambiante

        for (Light light : scene.getLights()) {
            // Si le point est à l'ombre de cette lumière, on l'ignore (sauf ambiante)
            if (isInShadow(p, light)) {
                continue;
            }

            Vector lightDir = light.getDirectionFrom(p);
            Color lightColor = light.getColor();

            // A. Composante Diffuse (Lambert) : I = Kd * (N . L)
            double nDotL = Math.max(normal.dot(lightDir), 0.0);
            Color diffuseTerm = lightColor.schurProduct(shape.getDiffuse()).mult(nDotL);
            lighting = lighting.add(diffuseTerm);

            // B. Composante Spéculaire (Blinn-Phong) : I = Ks * (N . H)^shininess
            if (shape.getShininess() > 0) {
                // Vecteur Vue (V) : du point vers la caméra
                Vector viewDir = scene.getCamera().getLookFrom().sub(p).norm();
                // Vecteur Halfway (H) : bissectrice entre L et V
                Vector halfVector = lightDir.add(viewDir).norm();

                double nDotH = Math.max(normal.dot(halfVector), 0.0);
                double specularFactor = Math.pow(nDotH, shape.getShininess());

                Color specularTerm = lightColor.schurProduct(shape.getSpecular()).mult(specularFactor);
                lighting = lighting.add(specularTerm);
            }
        }
        return lighting;
    }

    /**
     * Vérifie si un point est obscurci par un autre objet vis-à-vis d'une source de lumière.
     * Lance un "Shadow Ray" vers la lumière.
     */
    private boolean isInShadow(Point point, Light light) {
        Vector lightDir = light.getDirectionFrom(point);
        
        // Décalage de l'origine pour éviter l'auto-intersection
        Point shadowRayOrigin = point.add(lightDir.mult(AbstractVec3.EPSILON));
        Ray shadowRay = new Ray(shadowRayOrigin, lightDir);

        Optional<Intersection> obstacle = findClosestIntersection(shadowRay);

        if (obstacle.isPresent()) {
            // Pour une lumière ponctuelle, l'obstacle doit être AVANT la lumière
            if (light instanceof PointLight) {
                PointLight pl = (PointLight) light;
                double distanceToLight = pl.getPosition().sub(point).length();
                return obstacle.get().getDistance() < distanceToLight;
            }
            // Pour une lumière directionnelle (infinie), tout obstacle masque la lumière
            return true;
        }
        return false;
    }

    /**
     * Parcourt tous les objets de la scène pour trouver l'intersection la plus proche.
     *
     * @param ray Le rayon à tester.
     * @return L'intersection valide la plus proche, ou vide.
     */
    private Optional<Intersection> findClosestIntersection(Ray ray) {
        Optional<Intersection> closest = Optional.empty();
        double minDistance = Double.MAX_VALUE;

        for (Shape shape : scene.getShapes()) {
            Optional<Intersection> intersection = shape.findIntersection(ray);

            if (intersection.isPresent()) {
                double dist = intersection.get().getDistance();
                // On cherche la plus petite distance strictement positive
                if (dist > AbstractVec3.EPSILON && dist < minDistance) {
                    minDistance = dist;
                    closest = intersection;
                }
            }
        }
        return closest;
    }
}