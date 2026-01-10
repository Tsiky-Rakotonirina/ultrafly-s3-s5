============================
 Méthodes principales de JpaRepository<T, ID>

 T = type de l’entité (ex: Department)
 ID = type de la clé primaire (ex: Long)
 S = type de l’entité ou sous-type de T (pour la sauvegarde)
 Optional<T> = conteneur pouvant contenir ou pas l’entité (Java 8+)

============================

1. Méthodes de CrudRepository<T, ID>

- S save(S entity)
  Arguments : entity de type S extends T
  Retour : l’entité sauvegardée
  Description : Sauvegarde ou met à jour une entité

- Optional<T> findById(ID id)
  Arguments : id
  Retour : Optional<T>
  Description : Cherche une entité par son identifiant

- boolean existsById(ID id)
  Arguments : id
  Retour : boolean
  Description : Vérifie si une entité existe

- Iterable<T> findAll()
  Arguments : aucun
  Retour : Iterable<T>
  Description : Retourne toutes les entités

- Iterable<T> findAllById(Iterable<ID> ids)
  Arguments : ids
  Retour : Iterable<T>
  Description : Retourne toutes les entités dont l’id est dans la liste

- long count()
  Arguments : aucun
  Retour : long
  Description : Compte le nombre total d’entités

- void deleteById(ID id)
  Arguments : id
  Retour : void
  Description : Supprime une entité par id

- void delete(T entity)
  Arguments : entity
  Retour : void
  Description : Supprime une entité

- void deleteAllById(Iterable<? extends ID> ids)
  Arguments : ids
  Retour : void
  Description : Supprime plusieurs entités par id

- void deleteAll(Iterable<? extends T> entities)
  Arguments : entities
  Retour : void
  Description : Supprime plusieurs entités

- void deleteAll()
  Arguments : aucun
  Retour : void
  Description : Supprime toutes les entités


2. Méthodes de PagingAndSortingRepository<T, ID>

- Iterable<T> findAll(Sort sort)
  Arguments : Sort
  Retour : Iterable<T>
  Description : Retourne toutes les entités triées

- Page<T> findAll(Pageable pageable)
  Arguments : Pageable
  Retour : Page<T>
  Description : Retourne une page d’entités


3. Méthodes spécifiques à JpaRepository<T, ID>

- List<T> findAll()
  Arguments : aucun
  Retour : List<T>
  Description : Retourne toutes les entités sous forme de liste

- List<T> findAll(Sort sort)
  Arguments : Sort
  Retour : List<T>
  Description : Retourne toutes les entités triées sous forme de liste

- List<T> findAllById(Iterable<ID> ids)
  Arguments : ids
  Retour : List<T>
  Description : Retourne toutes les entités avec ces ids

- void flush()
  Arguments : aucun
  Retour : void
  Description : Force le flush du contexte de persistance

- S saveAndFlush(S entity)
  Arguments : entity
  Retour : S
  Description : Sauvegarde et flush immédiat

- void deleteInBatch(Iterable<T> entities)
  Arguments : entities
  Retour : void
  Description : Supprime en lot sans charger les entités

- void deleteAllInBatch()
  Arguments : aucun
  Retour : void
  Description : Supprime toutes les entités en lot

- T getOne(ID id)
  Arguments : id
  Retour : T
  Description : Retourne une référence proxy (lazy loading)


4. Méthodes basées sur le naming convention (findBy, countBy, deleteBy, ...)

Ces méthodes ne sont pas codées dans JpaRepository directement mais sont
générées automatiquement par Spring Data JPA à partir du nom de la méthode.

Exemples de signatures et comportements courants :

- findBy[Champ](Type valeur)
  Exemple : List<T> findByName(String name)
  Description : Cherche les entités où le champ "name" est égal à la valeur donnée.

- findBy[Champ]And[Champ](Type val1, Type val2)
  Exemple : List<T> findByNameAndLocation(String name, String location)
  Description : Recherche avec plusieurs conditions AND.

- findBy[Champ]Or[Champ](Type val1, Type val2)
  Exemple : List<T> findByNameOrLocation(String name, String location)
  Description : Recherche avec condition OR.

- findBy[Champ]Between(Type start, Type end)
  Exemple : List<T> findByDateBetween(Date start, Date end)
  Description : Recherche entre deux valeurs.

- findBy[Champ]LessThan(Type valeur)
  Exemple : List<T> findByAgeLessThan(Integer age)
  Description : Recherche où le champ est inférieur à la valeur.

- findBy[Champ]GreaterThan(Type valeur)
  Exemple : List<T> findByAgeGreaterThan(Integer age)
  Description : Recherche où le champ est supérieur à la valeur.

- findBy[Champ]Containing(String fragment)
  Exemple : List<T> findByDescriptionContaining(String keyword)
  Description : Recherche avec LIKE %keyword%.

- findBy[Champ]StartingWith(String prefix)
  Exemple : List<T> findByNameStartingWith(String prefix)
  Description : Recherche avec LIKE prefix%.

- findBy[Champ]EndingWith(String suffix)
  Exemple : List<T> findByNameEndingWith(String suffix)
  Description : Recherche avec LIKE %suffix.

- findBy[Champ]In(Collection<Type> valeurs)
  Exemple : List<T> findByIdIn(List<Long> ids)
  Description : Recherche où le champ est dans une collection.

- countBy[Champ](Type valeur)
  Exemple : Long countByStatus(String status)
  Description : Compte les entités selon une condition.

- deleteBy[Champ](Type valeur)
  Exemple : void deleteByStatus(String status)
  Description : Supprime les entités selon une condition.

- findBy[Champ]IsNull()
  Exemple : List<T> findByDescriptionIsNull()
  Description : Recherche où le champ est NULL.

- findBy[Champ]IsNotNull()
  Exemple : List<T> findByDescriptionIsNotNull()
  Description : Recherche où le champ n'est pas NULL.

- findBy[Champ]True() / findBy[Champ]False()
  Exemple : List<T> findByActiveTrue()
  Description : Recherche selon un booléen.

Note : Les noms peuvent être combinés et enrichis selon la documentation officielle de Spring Data JPA.

5.  Query and Objects
@Modifying (si CUD)
@Query("SELECT d FROM table d WHERE d.name = :name")
List<T> rechercheParNom(@Param("name") String name);
