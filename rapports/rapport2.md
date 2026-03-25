Cours MGL7760 – Qualité et productivité des outils logiciels

**PROJET DE SESSION**  
**Modernisation de l'outillage du projet open source TAF**

Étudiants:

Hamza Afif  
Ekpe Koffi  
Seyf Eddine Necib

Équipe n°4

***Professeur: Gnagnely Serge Dogny***

*Version 2.0*

# Rapport TP2 – Implémentation de la chaîne d'outils

## 1. Contexte et objectif du TP2

Ce rapport présente l'implémentation effective de la chaîne d'outils demandée au **TP2** (CI + Qualité + Tests + Documentation) pour le projet **TAF-FALL-2025**.  
L'objectif est de démontrer, avec preuves techniques, la conformité du dépôt aux exigences du TP2, d'identifier les écarts restants et de proposer les correctifs immédiats avant remise finale.

Périmètre d'analyse principal:

- `.github/workflows/ci-cd.yml`
- `sonar-project.properties`
- `backend/pom.xml`
- `performance/pom.xml`
- `frontend/package.json`
- `frontend/karma.conf.js`
- `test-generation-service/requirements-dev.txt`
- `README.md`
- `CI_CD_FIX_GUIDE.md`
- `docs/diagrams/*`

---

## 2. Livrable 1 – Implémentation technique

### A. Pipeline CI fonctionnel (obligatoire)

Le pipeline principal est défini dans `.github/workflows/ci-cd.yml`.

**Déclencheurs configurés:**

- `push` sur `develop` et `main`
- `pull_request` sur `develop`
- `workflow_dispatch`

**Stages présents et vérifiés:**

1. Build / compilation
- Backend Java (Maven)
- Frontend Angular (npm)
- Performance (Maven)
- Service Python (pip + pytest)

2. Linting / formatage
- Backend: Checkstyle + PMD
- Frontend: ESLint + `prettier --check`
- Python: `black --check`

3. Analyse statique
- SonarCloud multi-modules (backend, performance, frontend, python)
- Fichier de config dédié: `sonar-project.properties`

4. Tests automatisés
- Backend: `mvn test` + étape dédiée `failsafe:integration-test`
- Frontend: `npm run test -- --watch=false --code-coverage`
- Python: `pytest --cov`
- Performance: `mvn test`

5. Rapports
- JaCoCo (backend/performance)
- LCOV (frontend)
- Coverage XML/HTML (python)
- Upload d'artefacts via `actions/upload-artifact`

6. Documentation
- Compodoc (frontend)
- JavaDoc (backend) avec fallback HTML robuste
- Diagrammes Mermaid (génération automatique)
- Publication GitHub Pages

7. Optimisations CI
- Cache Maven/Node/pip
- `concurrency` avec annulation des exécutions obsolètes

8. Badges README
- Build status
- Coverage SonarCloud
- Quality Gate SonarCloud

**Conclusion section A:** Pipeline CI/CD complet et industrialisé, conforme au socle TP2.

---

### B. Outils de qualité configurés (obligatoire)

**Outils effectivement en place:**

- Java: Checkstyle + PMD (`backend/pom.xml`, `performance/pom.xml`)
- Frontend: ESLint + Prettier (`frontend/package.json`, `.eslintrc.json`, `.prettierrc`)
- Python: Black, flake8, mypy déclarés (`requirements-dev.txt`), Black exécuté en CI
- SonarCloud: configuration multi-langages (`sonar-project.properties`)

**Quality gate / seuils qualité observés:**

- Contrôle couverture Sonar en CI via API, seuil fixé à **60%** (`MIN_COVERAGE="60"` dans workflow)
- `sonar.qualitygate.wait=false` pour ne pas bloquer l'analyse Sonar elle-même

**Écart identifié:**

- Aucun hook `pre-commit` (ou équivalent type Husky / `.pre-commit-config`) n'a été trouvé dans le dépôt.

**Action corrective recommandée (courte):**

- Ajouter un hook pre-commit exécutant au minimum:
  - `npm run lint && npm run format:check` (frontend)
  - `mvn -f backend/pom.xml -q checkstyle:check pmd:check test` (backend)
  - `black --check app && pytest -q` (python)

**Conclusion section B:** Exigence qualité globalement couverte, avec un point à compléter sur la validation locale pré-commit.

---

### C. Suite de tests automatisés (obligatoire)

Mesures statiques observées dans le dépôt:

- Backend: **56 méthodes `@Test`** (`backend/src/test/java`)
- Frontend: **37 scénarios Jasmine (`it(...)`)** (`frontend/src/**/*.spec.ts`)
- Python: **6 tests pytest** (`test-generation-service/tests/test_main.py`)
- Performance: **1 test JUnit** (`performance/jmeter/src/test/java/.../JMeterRunnerTest.java`)

**Seuil minimum TP2 (20 tests unitaires) :** largement dépassé.

**Scénarios d'intégration (minimum 5):**

- Classe `backend/src/test/java/ca/etsmtl/taf/IntegrationTest.java` avec **18 scénarios d'intégration** (`@DisplayName("Integration: ...")`).

**Rapports de tests/couverture:**

- Backend: JaCoCo XML/HTML
- Frontend: coverage HTML + `lcov.info`
- Python: `coverage.xml` + `htmlcov/`
- Artefacts archivés en CI

**Risque technique à corriger:**

- L'étape `mvn failsafe:integration-test` est présente, mais la convention de nommage des classes d'intégration devrait être harmonisée (`*IT`) ou explicitement configurée dans Failsafe pour garantir l'exécution dédiée des tests d'intégration.

**Conclusion section C:** Exigence TP2 respectée sur le volume et la diversité des tests, avec un ajustement recommandé pour la robustesse de l'étape Failsafe.

---

### D. Documentation automatisée (obligatoire)

**Implémentation en place:**

- Documentation frontend générée avec **Compodoc**
- Documentation backend générée avec **JavaDoc** (fallback HTML si génération partielle)
- Diagrammes architecture/pipeline/flux via **Mermaid CLI**
- Publication automatique sur **GitHub Pages**

**README amélioré (constaté):**

- Badges (build, coverage, quality gate)
- Instructions de setup développeur
- Vue architecture
- Guide de contribution de base
- Références SonarCloud et documentation

**Conclusion section D:** Exigence documentation automatisée atteinte.

---

## 3. Livrable 2 – Guide d'intégration

Le guide d'intégration est matérialisé par:

- `README.md` (prérequis, installation, commandes de validation locale)
- `CI_CD_FIX_GUIDE.md` (runbook complet de correction CI/CD)
- `.github/workflows/ci-cd.yml` (pipeline annoté et structuré)
- `sonar-project.properties` (configuration centralisée de l'analyse qualité)
- `docs/diagrams/README.md` (documentation visuelle de l'architecture et des flux)

**État:** livrable globalement conforme.

---

## 4. Livrable 3 – Vidéo de démonstration (préparation)

Le dépôt contient tous les éléments nécessaires pour la démonstration 5–7 minutes demandée:

1. Création branche + changement code
2. Vérification locale (lint/tests)
3. Push + déclenchement pipeline
4. Consultation rapports/couverture/Sonar
5. Consultation documentation générée (GitHub Pages)

**À produire pour la remise:** captation vidéo finale et narration du workflow complet.

---

## 5. Évaluation de conformité TP2 (synthèse)

| Exigence TP2 | État | Justification |
|---|---|---|
| Pipeline CI complet (build/lint/test/qualité/doc) | Conforme | Workflow principal multi-jobs et multi-langages |
| Triggers push/PR + optimisation cache | Conforme | Triggers configurés + cache Maven/Node/pip + concurrency |
| Analyse statique + Quality Gate | Conforme (avec nuance) | SonarCloud + seuil couverture CI à 60%; quality gate Sonar non bloquant |
| Tests unitaires (>=20) | Conforme | >100 tests/scénarios cumulés (backend + frontend + python + perf) |
| Tests intégration (>=5 scénarios) | Conforme | 18 scénarios intégration backend |
| Rapports de tests et couverture | Conforme | JaCoCo + LCOV + coverage.xml/html + artefacts CI |
| Documentation auto + publication | Conforme | Compodoc + JavaDoc + Mermaid + GitHub Pages |
| Hooks pre-commit locaux | Partiellement conforme | Outils présents, hook non configuré |

---

## 6. Contraintes techniques TP2

| Contrainte | État | Observation |
|---|---|---|
| Pipeline < 15 min | À valider | Mesure à confirmer sur le dernier run GitHub Actions |
| Couverture minimale 60% | Ciblée | Seuil explicite dans le job SonarCloud |
| Quality Gate défini et respecté | Partiel | Contrôle couverture explicite; quality gate Sonar configuré non bloquant |
| Tous les tests passent | À confirmer run final | Nécessite validation sur run final de remise |
| Documentation accessible en ligne | Conforme (si Pages activé) | Étape de déploiement GitHub Pages présente |

---

## 7. Plan d'actions final avant remise

1. Ajouter une configuration pre-commit versionnée.
2. Aligner explicitement les tests d'intégration avec Failsafe (`*IT` ou includes Failsafe).
3. Exécuter un run final sur `develop` puis `main` et conserver captures:
   - pipeline complet
   - dashboard Sonar
   - rapports coverage
   - site de documentation
4. Reporter dans la version finale les métriques réelles du dernier run (durée pipeline, couverture globale, statut gate).

---

## 8. Conclusion

L'implémentation TP2 du projet TAF montre une chaîne d'outils **largement conforme** aux exigences du cours: pipeline CI/CD multi-composants, outils de qualité intégrés, tests automatisés en volume suffisant, documentation générée et publiée.  
Les écarts restants sont limités et clairement identifiés (principalement pre-commit et verrouillage explicite de l'étape Failsafe), ce qui permet une finalisation rapide et solide avant l'évaluation orale et la remise finale.
