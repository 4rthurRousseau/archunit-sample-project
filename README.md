# archunit-sample-project

# ArchUnit, qu'est-ce que c'est ?

![ArchUnit logo](assets/img/archunit.png)

ArchUnit est une librairie conçue pour vérifier automatiquement l'architecture logicielle de votre application au
travers de n'importe quel framework de test Java (typiquement, JUnit).

Grâce à ArchUnit, vous pouvez définir des règles architecturales qui seront alors contrôlées au travers de tests
unitaires. En cas d'échec, ces tests unitaires vous indiquent clairement les violations constatées et comment y
remédier.

Techniquement, ArchUnit repose sur l'analyse du bytecode Java, et sa matérialisation
sous [forme de classes spécifiques à ArchUnit](https://www.javadoc.io/doc/com.tngtech.archunit/archunit/latest/com/tngtech/archunit/core/domain/package-summary.html).
Par exemple, les
classes [`JavaClass`](https://www.javadoc.io/doc/com.tngtech.archunit/archunit/latest/com/tngtech/archunit/core/domain/JavaClass.html)
et [`JavaMethod`](https://www.javadoc.io/doc/com.tngtech.archunit/archunit/latest/com/tngtech/archunit/core/domain/JavaMethod.html)
représentent respectivement les classes et les méthodes de votre projet.

Vous souhaitez en savoir plus ? N'hésitez pas à consulter [l'article dev.to](https://todo.tld) pour lequel ce projet a
été créé ! 😉

# Informations relatives à ce projet

## Les règles à respecter

Afin d'illustrer au mieux les contraintes d'une application réelle, voici les règles que notre application se doit de
respecter :

* Architecture trois tiers, représentée par les trois packages suivants : `controller`, `domain`, `repository`,
* Le package `controller` a accès aux classes du package `domain`, mais pas celles du package `repository`,
* Le package `repository` a accès aux classes du package `domain`, mais pas celles du package `controller`,
* Le package `domain` est indépendant et n'a accès ni au package `controller`, ni au package `repository`.

--> Le contrôle de ces règles a été mis en place au sein de la classe de
test [ArchitectureTest.java](src/test/java/fr/arthurrousseau/archunit/ArchitectureTest.java)

Le package `controller` contient les classes qui exposent les points d'entrée permettant d'effectuer des opérations sur
les _produits_. Les contraintes propres à ce package sont les suivantes :

* L'ensemble des controllers doivent être annotés avec l'annotation `@Controller`, avoir le suffixe `Controller` et être
  positionnés à la racine du package `..controller`,
* Les modèles de données fournies en entrée ou sortie des controllers doivent utiliser des classes dédiées (les objets
  de la couche `domain` ne sont pas directement renvoyés). Ces classes doivent être suffixées par le mot clé `Dto`.

--> Le contrôle de ces règles a été mis en place au sein de la classe de
test [ControllerTest.java](src/test/java/fr/arthurrousseau/archunit/ControllerTest.java)

Le package `service` contient les classes qui sont en charge de la logique métier de notre application. Les contraintes
propres à ce package sont les suivantes :

* L'ensemble des services doit posséder une interface définissant les méthodes pouvant être appelées et une
  implémentation. L'interface doit se trouver à la racine du package `..service` tandis que l'implémentation doit être
  dans le package `..service.impl`,
* L'ensemble des implémentations de services doivent être annotés avec l'annotation `@Service` et avoir le
  suffixe `Service`,

--> Le contrôle de ces règles a été mis en place au sein de la classe de
test [ServiceTest.java](src/test/java/fr/arthurrousseau/archunit/ServiceTest.java)

Enfin, le package `repository` contient les classes qui sont en charge de la persistance des données. Afin de ne pas
alourdir le dépôt, nous n'ajouterons pas de contrainte liée à ce package (d'ici là, vous aurez compris comment mettre en
place de nouvelles règles propres à la couche repository, j'en suis sûr 😇).

## A quoi correspondent les branches de ce dépôt ?

Le projet comporte plusieurs branches :

- `initial` : représente le point de départ de ce projet. L'application est fonctionnelle, mais ne respecte pas tout à
  fait les règles d'architecture que nous avons décrites.
- `intermediate` : représente l'étape intermédiaire de ce projet. Des règles ArchUnit on été mises en place pour
  garantir que l'architecture projet répond aux critères que nous nous sommes fixés, mais les violations n'ont pas
  encore été corrigées.
- `solution` : représente l'étape finale de ce projet. Les règles ArchUnit sont bien en place, certaines ont été
  ajoutées ou revues et l'architecture correspond désormais à nos attentes.

# Commandes utiles

Récupération de la liste des produits

```bash
curl -X GET localhost:8080/products
```

Récupération d'un produit spécifique

```bash
curl -X GET localhost:8080/products/{id}
```

Ajout d'un produit

```bash
curl -X POST localhost:8080/products -d "{ \"label\": \"Awesome dev.to article !\", \"price\": 1000 }" -H "Content-Type: application/json"
```