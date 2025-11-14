# Mon Projet Ray Tracer (Java)

Ceci est mon projet de Ray Tracer, codé en Java pour le cours de Conception et Programmation Orientée Objet à l'IMT Nord Europe.

## Structure du Projet

Ce dépôt contient deux projets Maven distincts :

1.  `/raytracer` : Le moteur de rendu 3D lui-même. C'est le projet principal.
2.  `/imgcompare` : Un outil utilitaire pour comparer des images. Il me sert à vérifier que mes rendus sont corrects en les comparant à des images de référence.

## État d'avancement

* **Jalon 0 - Comparateur d'images : Terminé.** L'outil `imgcompare` est fonctionnel et peut être exécuté.
* **Jalon 1 - Calcul vectoriel : Terminé.** Les classes de base (`Point`, `Vector`, `Color`) sont en place et entièrement validées par 19 tests unitaires JUnit 5.
* **Jalon 2 - Lecture du fichier de scène : Terminé.** Le `SceneFileParser` est capable de lire et de comprendre les fichiers `.scene`, transformant le texte en une structure d'objets Java.
* **Jalon 3 - Premières images : Prochaine étape.**

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
```

#### Exécuter le comparateur :

Pour que les chemins fonctionnent, il est préférable de le lancer depuis la racine du projet (`ProjetRayTracer`):

```bash
java -jar imgcompare/target/imgcompare-1.0-SNAPSHOT.jar <chemin/image1.png> <chemin/image2.png>
```

#### Exemple :

```bash
java -jar imgcompare/target/imgcompare-1.0-SNAPSHOT.jar images_test/test_compare_image1.png images_test/test_compare_image2.png
```

#### Ce que vous verrez dans le terminal :
* `OK` (si la différence est < à 1000 pixels), sinon `KO`
* `Les deux images diffèrent de x pixels`
* L'outil crée aussi un fichier `diff.png` à la racine. Sur cette image, tout ce qui est noir est identique, le reste montre ce qui diffère.

### 2. Moteur `raytracer`

Le moteur principal est en cours de développement. Pour l'instant, il n'y a pas encore de rendu visuel, mais on peut valider son fonctionnement.

#### 1. Lancer les tests unitaires (validation jalon 1)

C'est la meilleure façon de vérifier que le cœur mathématique (`Point`, `Vector`, `Color`) fonctionne.

```bash
# 1. Aller dans le dossier du moteur
cd raytracer
# 2. Lancer les tests
mvn test
```

Vous devriez voir un `BUILD SUCCESS` avec 19 tests passés.

#### 2. Lancer le Parser (Validation Jalon 2)

On peut exécuter le `Main` du raytracer pour lui faire lire un fichier `.scene` et afficher ce qu'il a compris.

#### Depuis VSCode :

1. Assurez-vous d'avoir un dossier `scenes/` à la racine du projet contenant vos fichiers `.scene`.

2. Ouvrez l'onglet "Exécuter et déboguer".

3. Dans le menu déroulant, sélectionnez `Launch raytracer Main (test3.scene)`.

4. Appuyez sur F5 ou sur le bouton Play.

#### Ce que vous verrez dans le terminal :

```bash
--- Validation Jalon 2 (Parsing) ---
Scène chargée avec succès !
  Taille image : 640x480
  Fichier sortie : mascene.png
  ...
  Nombre de lumières : 2
  Nombre de formes : 1
-------------------------------------
```