/**
 * Représente la caméra (ou l'œil) dans la scène.
 * Stocke les informations lues depuis la ligne "camera" du fichier de scène.
 * (Jalon 2)
 */
package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;

public class Camera {

    private final Point lookFrom; // Position de l'œil (x,y,z)
    private final Point lookAt;   // Point visé (u,v,w)
    private final Vector up;      // Vecteur "haut" (m,n,o)
    private final double fov;     // Angle de vue (f)

    /**
     * Constructeur pour la caméra.
     * C'est ce constructeur que le SceneFileParser appelle.
     *
     * @param lookFrom Position de l'œil
     * @param lookAt   Point visé
     * @param up       Vecteur "haut"
     * @param fov      Angle de vue (Field of View) en degrés
     */
    public Camera(Point lookFrom, Point lookAt, Vector up, double fov) {
        this.lookFrom = lookFrom;
        this.lookAt = lookAt;
        this.up = up;
        this.fov = fov;
    }

    // --- Getters ---
    // Ces méthodes seront essentielles pour le Jalon 3 (Calcul des rayons)

    public Point getLookFrom() {
        return lookFrom;
    }

    public Point getLookAt() {
        return lookAt;
    }

    public Vector getUp() {
        return up;
    }

    public double getFov() {
        return fov;
    }
}