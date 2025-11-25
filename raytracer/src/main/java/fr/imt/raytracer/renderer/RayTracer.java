package fr.imt.raytracer.renderer;

import fr.imt.raytracer.geometry.Intersection;
import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Ray;
import fr.imt.raytracer.geometry.Shape;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.scene.Camera;
import fr.imt.raytracer.scene.Light;
import fr.imt.raytracer.scene.DirectionalLight; 
import fr.imt.raytracer.scene.PointLight; 
import fr.imt.raytracer.scene.Scene;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Optional;

/**
 * Le moteur principal du Ray Tracer.
 * (Jalon 3, 4 & 5)
 */
public class RayTracer {

    private final Scene scene;
    private final Camera camera;
    
    private final Vector u, v, w;
    private final double pixelHeight;
    private final double pixelWidth;

    /**
     * Constructeur du RayTracer.
     * Prépare le moteur de rendu en calculant la base orthonormée de la caméra.
     * @param scene La scène à rendre.
     */
    
    public RayTracer(Scene scene) {
        this.scene = scene;
        this.camera = scene.getCamera();

        // --- Calcul du repère orthonormé (u,v,w) ---
        this.w = camera.getLookFrom().subtract(camera.getLookAt()).normalize();
        this.u = camera.getUp().cross(this.w).normalize();
        this.v = this.w.cross(this.u); 

        // --- Calcul des dimensions d'un pixel ---
        double fovr = Math.toRadians(camera.getFov());
        this.pixelHeight = Math.tan(fovr / 2.0);
        double aspectRatio = (double) scene.getWidth() / scene.getHeight();
        this.pixelWidth = this.pixelHeight * aspectRatio;
    }

    /**
     * Lance le rendu de l'image.
     * @return Une BufferedImage contenant l'image rendue.
     */
    public BufferedImage render() {
        int width = scene.getWidth();
        int height = scene.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Ray ray = generateRayForPixel(i, j);
                Optional<Intersection> closestIntersection = findClosestIntersection(ray);
                Color pixelColor = new Color(0, 0, 0);

                if (closestIntersection.isPresent()) {
                    pixelColor = computeColor(closestIntersection.get());
                }

                image.setRGB(i, j, pixelColor.toRGB());
            }
        }
        return image;
    }

    /**
     * Détermine si le point d'intersection est à l'ombre par rapport à la source de lumière.
     * (Jalon 5 - Les Ombres)
     * * @param p Le point d'intersection.
     * @param light La source de lumière.
     * @return true si un objet est trouvé entre p et la lumière.
     */
    private boolean isShadowed(Point p, Light light) {
        
        // 1. Direction vers la lumière (L)
        Vector l = light.getDirectionFrom(p);
        
        // 2. Origine du rayon d'ombre : on décale P légèrement (EPSILON) 
        //    pour éviter l'auto-intersection avec l'objet P lui-même.
        Point shadowRayOrigin = p.add(l.multiply(Shape.EPSILON));
        
        Ray shadowRay = new Ray(shadowRayOrigin, l);

        // 3. Parcourir tous les objets pour trouver une intersection dans l'ombre
        for (Shape shape : scene.getShapes()) {
            Optional<Intersection> shadowIntersection = shape.findIntersection(shadowRay);
            
            if (shadowIntersection.isPresent()) {
                Intersection i = shadowIntersection.get();

                // Lumière directionnelle (à l'infini) : toute intersection est une ombre
                if (light instanceof DirectionalLight) {
                    return true;
                }
                
                // Lumière ponctuelle : vérifier que l'objet n'est pas AU-DELÀ de la source
                if (light instanceof PointLight) {
                    PointLight pointLight = (PointLight) light;
                    
                    // Calculer la distance de P à la source lumineuse
                    double distToLight = pointLight.getPosition().subtract(p).length();

                    // Si l'objet trouvé est plus proche que la source de lumière (i.e. il est entre P et S)
                    if (i.getDistance() < distToLight) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    /**
     * Calcule la couleur d'un point d'intersection en utilisant Lambert + Phong (Jalon 5).
     *
     * @param intersection L'objet intersection.
     * @return La couleur calculée.
     */
    private Color computeColor(Intersection intersection) {
        Shape shape = intersection.getShape();
        Point p = intersection.getPoint();
        
        // 1. Vecteurs de base
        Vector n = shape.getNormalAt(p); // Normale (N)
        // Attention: la direction de l'œil est nécessaire pour Phong
        Vector eyeDir = this.camera.getLookFrom().subtract(p).normalize(); // Vecteur œil (V)

        // 2. Matériaux
        Color objectDiffuse = shape.getDiffuse();
        Color objectSpecular = shape.getSpecular();
        double shininess = shape.getShininess();

        // 3. Couleur finale initialisée avec la lumière ambiante
        Color finalColor = scene.getAmbientLight();

        // 4. Ajouter la contribution de chaque lumière (Lambert + Phong)
        for (Light light : scene.getLights()) {
            
            // --- JALON 5 : OMBRES (Shadow Rays) ---
            if (isShadowed(p, light)) {
                continue; // Le point est à l'ombre. On passe à la lumière suivante.
            }
            // --- FIN OMBRES ---

            // Vecteur direction vers la lumière (L)
            Vector l = light.getDirectionFrom(p);
            Color lightColor = light.getColor();
            
            // --- Calcul de Lambert (Diffuse) ---
            double dotNL = n.dot(l);
            double lambert = Math.max(dotNL, 0.0);

            if (lambert > 0) {
                // Contribution Diffuse : lambert * lightColor * objectDiffuse
                Color diffuseContribution = lightColor
                                          .multiply(lambert)
                                          .schur(objectDiffuse);
                
                finalColor = finalColor.add(diffuseContribution);
                
                // --- Calcul de Phong (Spéculaire/Brillance) (Jalon 5) ---
                
                // 1. Calculer le vecteur H (Halfway vector - Blinn-Phong)
                // H = (L + V) / ||L + V||
                Vector h = l.add(eyeDir).normalize();
                
                // 2. Calculer le coefficient de Phong : max(n . h, 0)^shininess
                double phongIntensity = Math.pow(Math.max(n.dot(h), 0.0), shininess);
                
                // 3. Contribution Spéculaire : phongIntensity * lightColor * objectSpecular
                // Si l'objet n'a pas de couleur spéculaire (noir), cette contribution est nulle.
                Color specularContribution = lightColor
                                           .multiply(phongIntensity)
                                           .schur(objectSpecular);
                
                finalColor = finalColor.add(specularContribution);
            }
        }

        return finalColor;
    }
    
    // --- Méthodes inchangées du Jalon 3 ---

    private Ray generateRayForPixel(int i, int j) {
        int width = scene.getWidth();
        int height = scene.getHeight();
        double a = pixelWidth * ( (2.0 * i - width + 1.0) / width );
        double b = pixelHeight * ( (2.0 * j - height + 1.0) / height );
        b = -b;
        Vector d_u = u.multiply(a);
        Vector d_v = v.multiply(b);
        Vector d = d_u.add(d_v).subtract(w);
        Vector direction = d.normalize();
        Point origin = camera.getLookFrom();
        return new Ray(origin, direction);
    }
    
    private Optional<Intersection> findClosestIntersection(Ray ray) {
        return scene.getShapes().stream()
                .map(shape -> shape.findIntersection(ray))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Comparator.comparing(Intersection::getDistance));
    }
}