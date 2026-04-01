# CI/CD Story - What Happens Step by Step

This document explains the full workflow in execution order, including checks done before each action.

Source workflow: [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml)

## 1. How the pipeline starts

1. The pipeline starts on push to main or develop, on pull requests targeting develop, or manually.
2. Before doing any heavy work, concurrency is applied: if a newer run starts on the same ref, the previous in-progress run is cancelled.
3. Global runtime settings are loaded (Java memory, Node memory, action compatibility settings).

## 2. First wave: core jobs running in parallel

The pipeline launches four core jobs at the same time.

### 2.1 Backend job

1. Checkout repository with full git history.
2. Set Java 17 and enable Maven cache.
3. Install parent Maven POM.
4. Build performance artifacts first because backend depends on them.
5. Compile backend.
6. Run lint checks (Checkstyle, PMD). These are informative and do not block the run.
7. Run unit tests with CI environment variables.
8. Run integration tests (currently tolerated even if they fail).
9. Generate JaCoCo coverage.
10. Upload backend build and coverage artifacts.

### 2.2 Frontend job

1. Checkout repository.
2. Set Node 20 and npm cache.
3. Install headless Chrome for browser tests.
4. Install frontend dependencies.
5. Run ESLint and Prettier checks (non-blocking in current setup).
6. Build Angular app.
7. Run unit tests with coverage.
8. Upload frontend build and coverage artifacts.

### 2.3 Performance job

1. Checkout repository.
2. Set Java 17 with Maven cache.
3. Install parent POM.
4. Compile performance modules.
5. Run Checkstyle and PMD (non-blocking).
6. Run performance tests (non-blocking).
7. Generate coverage (non-blocking).
8. Upload performance coverage artifact.

### 2.4 Test-generation-service job (Python)

1. Checkout repository.
2. Set Python 3.11 with pip cache.
3. Install Python dependencies.
4. Run flake8, black, mypy (all non-blocking).
5. Run pytest with coverage (non-blocking).
6. Upload XML and HTML coverage artifacts.

## 3. Docker images job (gated on success)

Before this job starts, it checks these conditions:

1. Backend must be success.
2. Frontend must be success.
3. Test-generation-service must be success.

Then it does:

1. Setup Docker Buildx.
2. Compute normalized owner namespace.
3. If run is push on main, login to GHCR.
4. If Docker Hub secrets exist and run is push on main, login to Docker Hub.
5. Build backend, frontend, and test-generation images.
6. Push images only when conditions allow it.
7. Write a run summary indicating what was built and what was published.

## 4. SonarCloud job (main branch only)

Before this job starts, it checks:

1. Branch must be main.
2. It waits for backend, frontend, performance, and test-generation-service to complete.

Then it does:

1. Download artifacts.
2. Verify SONAR_TOKEN exists.
3. Verify token can access the configured Sonar project.
4. Try to enforce new-code period policy.
5. Rebuild required bytecode and regenerate backend coverage to avoid false 0%.
6. Run SonarCloud scan.
7. Poll Sonar compute-engine task and fail only if server-side processing fails.

## 5. Reporting job (always runs)

This job runs regardless of previous failures.

1. Download coverage artifacts and optional build artifacts.
2. Create a pipeline summary with per-component status.
3. If event is pull_request, post a status comment on the PR.

## 6. Diagrams job

1. Checkout repository.
2. Set Node 20.
3. Install Mermaid CLI.
4. Create output directory in docs.
5. Create Puppeteer config file with no-sandbox flags.
6. Generate all Mermaid SVG diagrams.
7. Upload generated diagrams as artifact.

## 7. Documentation job (publishing docs)

Before this job starts, it checks:

1. Branch must be main or develop.
2. Backend must be success.
3. Frontend must be success.
4. It also depends on diagrams job completion.

Then it does:

1. Download generated diagrams artifact.
2. Generate frontend docs with Compodoc.
3. Build backend artifacts and JavaDoc.
4. If JavaDoc is incomplete, build a fallback backend docs page.
5. Build endpoint catalog from controller annotations.
6. Build global docs index page.
7. Deploy docs to GitHub Pages.
8. Print final docs URL.

### 7.1 Why a separate branch appears (`gh-pages`)

This is expected behavior.

1. The deploy step uses `peaceiris/actions-gh-pages@v3` with `publish_dir: ./docs`.
2. Because no `publish_branch` is specified, the action uses the default branch `gh-pages`.
3. On the first successful docs deploy, the action creates `gh-pages` automatically if it does not exist.
4. On next runs, it updates that same branch with the new generated site content.
5. So `main` keeps source code, while `gh-pages` stores only the built static website.

In other words, seeing a `gh-pages` branch means documentation publication is working as designed.

### 7.2 Exact check before publish

Before deploying docs, the workflow checks:

1. The run is on `main` or `develop`.
2. `backend` and `frontend` jobs succeeded.
3. The documentation job has `contents: write` permission, which is required to push to `gh-pages`.

## 8. Practical reading guide for failures

When something fails, read in this order:

1. Core parallel jobs first: backend, frontend, performance, test-generation-service.
2. If docker-images did not run, check its upstream success conditions.
3. If Sonar did not run, check branch and token/project access checks.
4. If documentation did not run, check branch and backend/frontend success.
5. If docs ran but diagrams are missing, inspect diagrams artifact in the run.

## 9. End-to-end mental model in one line

The pipeline first validates and tests all stacks in parallel, then conditionally builds/publishes Docker and Sonar quality analysis, always publishes a status report, and finally generates and deploys documentation (including diagrams) when branch and quality prerequisites are met.
