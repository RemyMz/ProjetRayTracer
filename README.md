Mon Projet Ray Tracer (Java)

Voici mon projet de Ray Tracer, codé en Java pour le cours de Conception et Programmation Orientée Objet à l'IMT Nord Europe

Structure du Projet :

Ce dépôt contient deux projets Maven :

/raytracer : Le moteur de rendu 3D lui-même. C'est le projet principal.

/imgcompare : Un outil pour comparer les images. Il me sert à vérifier que mes rendus sont corrects en les comparant à des images de référence.

État d'avancement

Jalon 0 - Comparateur d'images : Terminé. L'outil imgcompare fonctionne.

Jalon 1 - Calcul vectoriel : Terminé. Les classes de base (Point, Vector, Color) sont en place et validées par des tests JUnit 5.

Jalon 2 - Lecture du fichier de scène : Next Step

Comment utiliser ?

- Prérequis :

Java (JDK) 21 ou plus récent

Maven

1. Outil imgcompare (Comparateur d'images)

L'outil imgcompare compare deux images depuis la ligne de commande. Il donne un rapport et crée une image qui montre les différences.

1. Compiler le projet (la première fois) :

Aller dans le dossier du comparateur
-> cd imgcompare

Compiler et créer le .jar
-> mvn clean package


2. Exécuter le comparateur :
Pour que les chemins fonctionnent, lancez-le depuis la racine du projet (ProjetRayTracer).

java -jar imgcompare/target/imgcompare-1.0-SNAPSHOT.jar <chemin/image1.png> <chemin/image2.png>


Exemple :

java -jar imgcompare/target/imgcompare-1.0-SNAPSHOT.jar images_test/test1.png images_test/test2.png


Ce que vous verrez dans le terminal :

OK (si la différence est < 1000 pixels) ou KO (sinon).

Le nombre exact de pixels différents.

L'outil crée aussi un fichier diff.png à la racine. Sur cette image, tout ce qui est noir est identique, le reste montre les différences.

2. Moteur raytracer

Le moteur est en cours de dev. Pour l'instant, la meilleure façon de voir si tout va bien est de lancer les tests unitaires.

1. Aller dans le dossier du moteur :

cd raytracer


2. Lancer les tests :

mvn test