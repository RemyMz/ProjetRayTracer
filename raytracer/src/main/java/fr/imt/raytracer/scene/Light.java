/**
 * Interface pour tous les objets émettant de la lumière.
 * (Jalon 2)
 *
 * Une interface est un "contrat" qui garantit que PointLight
 * et DirectionalLight peuvent tous deux être traités comme un "Light".
 */
package fr.imt.raytracer.scene;

public interface Light {
    
    // Pour l'instant, cette interface est vide.
    // C'est un "marqueur" qui nous permet de les mettre dans la même liste :
    // scene.addLight(new PointLight(...));
    // scene.addLight(new DirectionalLight(...));
    //
    // Plus tard (Jalon 4), nous pourrons ajouter des méthodes ici,
    // comme `getColorAtPoint(...)`.
}