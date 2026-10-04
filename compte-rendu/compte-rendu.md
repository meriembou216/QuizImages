# Compte rendu : TP Quiz en images

**Étudiante :** Meriem bouaoun
**Thème choisi :** formes géométriques (triangle, carré, cercle, étoile, rectangle)

## 1. Interface et widgets utilisés
- `TextView` : titre, score, numéro de question, question, message de retour, résultat final
- `ProgressBar` horizontale : nombre de réponses données (max 5)
- `ImageView` : image de la question (`fitCenter`, sans déformation)
- 3 `Button` pour les choix + `Button` « Question suivante / Voir le résultat » + `Button` « Rejouer »
- `ScrollView` : défilement sur petits écrans ; le bandeau avec le score reste fixe en haut

## 2. Structure du tableau des questions
Une `data class Question` contient : le texte, l'image (`Int`, ex. `R.drawable.triangle`),
un tableau de 3 choix, l'indice de la bonne réponse (0, 1 ou 2) et une description
de l'image pour l'accessibilité. Un tableau `questions` contient 5 objets `Question`.

## 3. Événements, score et progression
- Chaque bouton a un `setOnClickListener` qui appelle `verifierReponse(i)` avec son indice `i` ;
  on compare `i` à `bonneReponse` pour savoir si la réponse est juste.
- Un seul point par question : le booléen `aRepondu` bloque les clics suivants et les
  3 boutons sont désactivés (`isEnabled = false`).
- **Score** = nombre de bonnes réponses ; **progression** = nombre de réponses données
  (bonnes ou fausses). Une mauvaise réponse fait avancer la barre mais pas le score.
- Dernière question : on teste `indexCourant == questions.size - 1` ; le bouton devient
  « Voir le résultat » et affiche le score final dans la même activité.

## 4. Tests réalisés
| Test | Résultat |
|---|---|
| Nouvelle partie | OK : 0/5 et progression 0/5 |
| Bonne réponse | OK : score +1, progression +1 |
| Mauvaise réponse | OK : score inchangé, progression +1 |
| Clics multiples sur un choix | OK : aucun point en plus |
| 5 bonnes / 5 mauvaises réponses | OK : 5/5 et 0/5, progression 5/5 |
| Rejouer | OK : retour à zéro |
| Petit écran | OK : tout reste accessible |

## 5. Difficultés rencontrées
- **Les images :** j'ai d'abord dû comprendre comment créer des drawables vectoriels
  dans `res/drawable` et respecter les règles de nommage (minuscules, sans espace ni
  accent). Une fois les cinq formes créées, je les ai associées aux questions avec
  `R.drawable.nom_de_l_image`.
- **Le score et la progression :** au début, il fallait bien distinguer les deux. J'ai
  utilisé deux variables séparées, `score` pour les bonnes réponses et `nbReponses`
  pour les réponses données, afin que la barre avance aussi après une mauvaise réponse.
- **Les points multiples :** pour éviter qu'un utilisateur gagne plusieurs points en
  cliquant plusieurs fois, j'ai ajouté le booléen `aRepondu` et désactivé les trois
  boutons après la première réponse.
- **L'affichage :** j'ai utilisé un `ScrollView` pour que tous les éléments restent
  accessibles sur un petit écran, tout en gardant le score visible en haut.

Ces difficultés ont été résolues en testant l'application étape par étape sur
l'émulateur.