# MGL7760 - Qualité et productivité des outils logiciels

## PROJET DE SESSION : Modernisation outillage d'un projet open-source

**UQAM – Département Informatique**  
**Groupe 30 – Hiver 2026**  
© 2026 - Serge Dogny

---

## Contexte général

Les équipes (2-3 étudiants) sélectionnent un projet open-source réel (GitHub/GitLab) de taille moyenne qui manque d'outillage moderne.

L'objectif est d'analyser, concevoir, implémenter et évaluer une chaîne complète d'outils pour améliorer qualité et productivité, en suivant une progression logique sur 3 TPs.

---

## TP2 : Implémentation de la chaîne d'outils

_(CI + Qualité + Tests + Documentation)_ — **35%**

📅 **Livraison :** Fin de la semaine 11 (29 mars 2026 avant 23:55)

---

## Objectifs

1. Implémenter concrètement les outils sélectionnés
2. Configurer l'intégration entre outils
3. Mettre en place un pipeline CI complet (build, test, qualité)
4. Automatiser la génération de documentation
5. Exclure le déploiement (reporté au TP3)

---

## Livrables

### 1. Implémentation technique

Dépôt Git (fork du projet) avec :

#### A. Pipeline CI fonctionnel (obligatoire)

- Configuration complète (GitHub Actions, GitLab CI, ou Jenkins)
- Stages minimum :
  - Build / compilation
  - Linting et formatage automatique
  - Analyse statique (SonarQube / CodeClimate ou équivalent)
  - Exécution des tests (unitaires + intégration)
  - Génération de rapports (couverture, qualité)
  - Génération et publication de la documentation
- Triggers appropriés (push, PR)
- Cache et optimisation du temps d'exécution
- Badges de statut dans le README

#### B. Outils de qualité configurés (obligatoire)

- Linters (selon langage) avec configuration personnalisée
- Analyseur statique (SonarQube/Cloud, CodeClimate, ou équivalent)
- Configuration des Quality Gates
- Hooks pre-commit pour validation locale
- Dashboards de qualité accessibles

#### C. Suite de tests automatisés (obligatoire)

- Tests unitaires avec framework approprié (**minimum 20 tests**)
- Tests d'intégration (**minimum 5 scénarios**)
- Configuration de couverture de code
- Rapports de tests (JUnit XML, HTML)
- Bonus :
  - Tests E2E
  - Mutation testing

#### D. Documentation automatisée (obligatoire)

- Génération automatique depuis le code (JSDoc, Sphinx, Javadoc, etc.)
- Documentation technique déployée automatiquement (GitHub Pages, ReadTheDocs, etc.)
- README amélioré avec :
  - Badges (build, coverage, quality)
  - Instructions setup développeur
  - Architecture overview
  - Guide de contribution
- Diagrammes générés automatiquement (PlantUML, Mermaid)

---

### 2. Guide d'intégration (documentation technique)

- Fichiers de configuration annotés :
  - Pipeline CI avec commentaires détaillés
  - Configuration des linters et analyseurs
  - Configuration des outils de test
  - Docker / docker-compose (si applicable)

- README technique détaillé :
  - Prérequis et installation
  - Guide de reproduction de l'environnement
  - Workflow développeur (branches, commits, PR)
  - Exécution locale des tests et validations
  - Guide d'utilisation de chaque outil

- Captures d'écran :
  - Pipeline en action
  - Dashboards de qualité
  - Rapports de tests et couverture
  - Documentation générée

---

### 3. Vidéo de démonstration (5-7 minutes)

- Présentation de la chaîne d'outils implémentée
- Workflow typique :
  - Création d'une branche
  - Modification de code
  - Exécution locale (pre-commit hooks)
  - Push et déclenchement du pipeline
  - Revue des rapports

- Exemples concrets :
  - Détection d'un bug par les tests
  - Identification d'un problème de qualité
  - Correction et re-validation
  - Documentation mise à jour automatiquement

---

## Présentation orale

📅 Séance 11  
⏱ 12-15 minutes + 5 min questions

- Démonstration en direct du pipeline
- Présentation des métriques de qualité
- Démonstration de la documentation générée
- Défis techniques et solutions
- Questions et feedback

---

## Critères d'évaluation

| Critère                                            | Pondération |
| -------------------------------------------------- | ----------- |
| Fonctionnalité et robustesse du pipeline CI        | 30%         |
| Configuration et intégration des outils de qualité | 20%         |
| Suite de tests automatisés                         | 20%         |
| Documentation automatisée                          | 15%         |
| Qualité de la documentation technique du TP        | 10%         |
| Complexité et innovation de la solution            | 5%          |

---

## Contraintes techniques

- Pipeline CI doit s'exécuter en **moins de 15 minutes**
- Minimum **60% de couverture de code**
- Quality Gate défini et respecté
- Tous les tests doivent passer
- Documentation accessible en ligne
