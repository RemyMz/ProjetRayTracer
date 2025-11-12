# Mon Projet Ray Tracer (Java)

Ceci est mon projet de Ray Tracer, codé en Java pour le cours de Conception et Programmation Orientée Objet à l'IMT Nord Europe.

## Structure du Projet

Ce dépôt contient deux projets Maven distincts :

1.  `/raytracer` : Le moteur de rendu 3D lui-même. C'est le projet principal.
2.  `/imgcompare` : Un outil utilitaire pour comparer des images. Il me sert à vérifier que mes rendus sont corrects en les comparant à des images de référence.

## État d'avancement

* **Jalon 0 - Comparateur d'images :** ✅ **Terminé.** L'outil `imgcompare` est fonctionnel et peut être exécuté.
* **Jalon 1 - Calcul vectoriel :** ✅ **Terminé.** Les classes de base (`Point`, `Vector`, `Color`) sont en place et entièrement validées par 19 tests unitaires JUnit 5.
* **Jalon 2 - Lecture du fichier de scène :** ✅ **Terminé.** Le `SceneFileParser` est capable de lire et de comprendre les fichiers `.scene`, transformant le texte en une structure d'objets Java.
* **Jalon 3 - Premières images :** 🚧 **Prochaine étape.**

## Comment utiliser ?

Prérequis :
* Java (JDK) 21 ou plus récent
* Maven

### 1. Outil `imgcompare` (Comparateur d'images)

L'outil `imgcompare` compare deux images depuis la ligne de commande. Il donne un rapport et crée une image qui montre les différences.

#### Compiler le projet (la première fois) :

```bash
# 1. Aller dans le dossier du comparateur
cd imgcompare
# 2. Compiler et créer le .jar
mvn clean package