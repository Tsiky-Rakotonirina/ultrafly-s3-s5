# Gestion de projets S5-S3

Theme : Compagnie aerienne
Application : UltraFly
Description : Application Web de Gestion d'une compagnie aerienne 
Debut : Jeudi 8 janvier 2026
Fin : Vendredi 30 janvier 2026
Examens : 9-1; 15-1; 16-1; 22-1; 23-1; 29-1; 30-1

---

## Fonctionnalités

Entites a gerer : Aeroport, Equipage, Client, Vol et Billet

---

## Technologies utilisées

Persistance : Base de donnees avec SGBD PostgreSQL 16.2
Backend : Framework Web de Java : SpringBoot 4.0.1 
Frontend : Html, css et js avec thymeleaf pour le dynamisme
Maven : Outil de gestion de dependances, de compilation, de lancement de Java 
Git et gihub : Outil de gestion version et de collaboration
Pour plus d'informations, consulter le fichier HELP.md

---

## Architecture

Architecture 3-tiers avec Pattern Model-ViewController
- model : les classes mappent les tables ou les vues de la base de donnees 
- repository : gere l'interaction avec la base, fournit les methodes
- service : pont entre repository et controller pour les regles metiers : controle -  traitement - persistance
- controller : gere les requetes http, appelle service, prepare les donnees des templates
- template : affiche les donnees cote utilisateur

---

## Arborescence du projet

📁compagnie_aerienne
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 itu/
│   │   │           └── 📁 compagnie_aerienne/
│   │   │               ├── 📁 controller/
│   │   │               ├── 📁 dto/
│   │   │               ├── 📁 entite/
│   │   │               ├── 📁 repository/
│   │   │               ├── 📁 service/
│   │   │               ├── 📄 CompagnieAerienneApplication.java (353.0 B)
│   │   │               └── 📄 ServletInitializer.java (442.0 B)
│   │   └── 📁 resources/
│   │       ├── 📁 database/
│   │       │   ├── 📄 cheatsheet.sql (1.4 KB)
│   │       │   ├── 📄 data.sql (0.0 B)
│   │       │   ├── 📄 JpaRepository.md (5.9 KB)
│   │       │   ├── 📄 query.Sql (0.0 B)
│   │       │   ├── 📄 reset.sql (106.0 B)
│   │       │   ├── 📄 script.sql (0.0 B)
│   │       │   ├── 📄 table_association.sql (0.0 B)
│   │       │   ├── 📄 table_entite.sql (0.0 B)
│   │       │   ├── 📄 table_reference.sql (0.0 B)
│   │       │   └── 📄 view.sql (0.0 B)
│   │       ├── 📁 static/
│   │       │   ├── 📁 bootstrap5/
│   │       │   │   ├── 📁 css/
│   │       │   │   │   ├── 📄 bootstrap-grid.css (69.0 KB)
│   │       │   │   │   ├── ...
│   │       │   │   └── 📁 js/
│   │       │   │       ├── 📄 bootstrap.bundle.js (203.1 KB)
│   │       │   │       ├── ...
│   │       │   ├── 📁 css/
│   │       │   ├── 📁 image/
│   │       │   ├── 📁 js/
│   │       │   └── 📁 template/
│   │       ├── 📁 templates/
│   │       │   ├── 📁 aeroport/
│   │       │   ├── 📁 billet/
│   │       │   ├── 📁 client/
│   │       │   ├── 📁 dashboard/
│   │       │   ├── 📁 equipage/
│   │       │   ├── 📁 vol/
│   │       │   └── 📄 index.html (0.0 B)
│   │       └── 📄 application.properties (585.0 B)
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 com/
│               └── 📁 itu/
│                   └── 📁 compagnie_aerienne/
│                       └── 📄 CompagnieAerienneApplicationTests.java (238.0 B)
├── 📁 target/
│   ├── 📁 classes/
│   │   ├── 📁 database/
│   │   │   ├── 📄 cheatsheet.sql (1.4 KB)
│   │   │   ├── 📄 ...
│   │   ├── 📁 static/
│   │   │   └── 📁 bootstrap5/
│   │   │       ├── 📁 css/
│   │   │       │   ├── 📄 bootstrap-grid.css (69.0 KB)
│   │   │       │   ├── 📄 ...
│   │   │       └── 📁 js/
│   │   │           ├── 📄 bootstrap.bundle.js (203.1 KB)
│   │   │           ├── 📄...
│   │   ├── 📁 templates/
│   │   │   └── 📄 index.html (0.0 B)
│   │   └── 📄 application.properties (585.0 B)
│   ├── 📁 generated-sources/
│   │   └── 📁 annotations/
│   └── 📁 maven-status/
│       └── 📁 maven-compiler-plugin/
│           └── 📁 compile/
│               └── 📁 default-compile/
│                   ├── 📄 createdFiles.lst (0.0 B)
│                   └── 📄 inputFiles.lst (308.0 B)
├── 📄 HELP.md (1.7 KB)
├── 📄 mvnw (11.5 KB)
├── 📄 mvnw.cmd (8.1 KB)
├── 📄 NormeDeDev.md (1.8 KB)
├── 📄 pom.xml (4.0 KB)
└── 📄 README.md (4.9 KB)

---

## Controle de version

- un developpeur a une branche nommer : prenom/nom_de_la_feature pris depuis la derniere version de la branche dev quand un feature est a faire
- commit tous les 10-25 min (titre avec sens)
- push apres test sur un feature
- faire une pull request sur la branche dev
- le chef de projet fait la revue de code et valide la pull request pour le merge

---

## Norme de Developpement

Langue : FRANCAIS

kebab-case : tirer-par-mots
snake_case : tirer_bas_par_mots
camelCase : debutMinusculePuisMajusculeParMots
PascalCase : DebutMajusculeParMots

Database
    nom de base : 1 mot
    nom de table : snake_case
    nom de champ : snake_case
    cle primaire : id_nomdetable
    cle etrangere : nomdetable_id
    nom de view : v_action_metier

Classe
    - nom de classe : PascalCase
        - Model : PascalCase
            - si table : NomDeTable; si vue : VNomDeVue
            - annote : @Entity, @NoArgsConstructor, @AllArgsConstructor, @Data
        - le reste terminer par : Repository, Controller, Service, Dto
    - nom de variable/attribut : camelCase 
        - array / list : nom de classe avec s
        - object : nom de classe 
    - nom de fonction : camelCase
        - dans {Repository, Service} :  
            - return result 
            - throws Exception 
        - dans {Controller} :
            - si dans try : envoyer les reponses vers la page desiree
            - si catch exception : envoyer objet Type ErrorDto nomme erreur dans page precedente a gerer dans les vues
            
/src/main/java/com/gestioncafe/database
    - cheatsheet.sql : cheatsheet sur postgresql
    - methods.md : liste des methodes fournis par JpaRepository
    - data.sql : les donnees de tests
    - query.sql : les requetes non fournis par JpaRepository
    - reset : listes de commandes pour reset la database
    - script.sql :  liste des tables complete a voir dans looping
    - table_reference.sql : liste des tables dont les donnees sotn fixes
    - table_entite.sql : liste des tables dont les donnees sont a manipuler
    - table_association.sql : liste des tables de relations entre tables
    - view.sql : liste des views crees
    - si changement de base nommer un nouveau fichier avec la date du jour (ex : 2026_01_15.sql) et utiliser drop, alter etc

View 
    - nom de page : kebab-case 
    - valeur de objet : th:text="${}"
    - if : th:if="${ a==b and c!=d or !e.isEmpty() }" th:unless="${ a==c }"
    - foreach : th:each="user, ${users}"
    - lien : th:href="@{/(id=${id})}" th:src="@{/}" th:action="@{/}"
    - pour integrer les fichiers dans static : passer direct au nom du sous dossier



## Recuperer le projet

- Github : [https://github.com/Tsiky-Rakotonirina/ultrafly-s3-s5](https://github.com/Tsiky-Rakotonirina/ultrafly-s3-s5)

- code -> copier URL
- GitHub desktop : menu -> file -> clone repository -> url :
    - url : coller le lien 
    - local path : choisir le dossier pour mettre le dossier du projet
- ouvrir le code dans vscode ou IntelliJIdea
- aller dans /src/main/resources/application.properties :  changer les parametres de la base de donnees
    - spring.datasource.url=jdbc:postgresql://localhost:5432/nom_de_votre_base
    - spring.datasource.username=nom_utilisateur_postgresql
    - spring.datasource.password=mot_de_passe_postgresql

---

## Executer et lancer le projet 
executer a la racine du projet :
mvn spring-boot:run
verifier : /target/compagnie_aerienne.war

--- 

## Tester du projet

- Ouvrir le navigateur et aller sur l'URL
http://localhost:8080

---
