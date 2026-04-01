param(
    [ValidateSet('quick', 'full', 'docs')]
    [string]$Mode = 'quick'
)

$ErrorActionPreference = 'Stop'

function Run-Step {
    param(
        [string]$Title,
        [string]$Command,
        [string]$WorkingDir = '.'
    )

    Write-Host "`n=== $Title ===" -ForegroundColor Cyan
    Push-Location $WorkingDir
    try {
        Write-Host "$WorkingDir> $Command"
        Invoke-Expression $Command
    }
    finally {
        Pop-Location
    }
}

Run-Step -Title 'Java version' -Command 'java -version'

if ($Mode -eq 'quick' -or $Mode -eq 'full') {
    Run-Step -Title 'Install parent POM' -Command 'mvn -N -f pom.docker.xml install -q'
    Run-Step -Title 'Build performance dependencies' -Command 'mvn -f performance/pom.xml clean install -DskipTests -q'
    Run-Step -Title 'Backend compile' -Command 'mvn clean compile -DskipTests -q' -WorkingDir 'backend'
    Run-Step -Title 'Backend lint (checkstyle)' -Command 'mvn checkstyle:check -q' -WorkingDir 'backend'
    Run-Step -Title 'Backend static analysis (pmd)' -Command 'mvn pmd:check -q' -WorkingDir 'backend'
    Run-Step -Title 'Backend unit tests' -Command 'mvn test -q' -WorkingDir 'backend'
    Run-Step -Title 'Backend integration tests' -Command 'mvn failsafe:integration-test -q' -WorkingDir 'backend'
    Run-Step -Title 'Backend coverage report' -Command 'mvn jacoco:report -q' -WorkingDir 'backend'

    Run-Step -Title 'Frontend install' -Command 'npm install --legacy-peer-deps' -WorkingDir 'frontend'
    Run-Step -Title 'Frontend lint' -Command 'npm run lint' -WorkingDir 'frontend'
    Run-Step -Title 'Frontend format check' -Command 'npm run format:check' -WorkingDir 'frontend'
    Run-Step -Title 'Frontend build' -Command 'npm run build' -WorkingDir 'frontend'
    Run-Step -Title 'Frontend tests + coverage' -Command 'npm run test -- --watch=false --code-coverage' -WorkingDir 'frontend'

    Run-Step -Title 'Python install deps' -Command 'python -m pip install --upgrade pip; pip install -r requirements-dev.txt' -WorkingDir 'test-generation-service'
    Run-Step -Title 'Python format check' -Command 'black --check app' -WorkingDir 'test-generation-service'
    Run-Step -Title 'Python tests + coverage' -Command 'pytest --cov=app --cov-report=xml --cov-report=html --cov-report=term-missing' -WorkingDir 'test-generation-service'
}

if ($Mode -eq 'docs' -or $Mode -eq 'full') {
    Run-Step -Title 'Frontend docs (Compodoc)' -Command 'npm install --legacy-peer-deps; npx @compodoc/compodoc -p tsconfig.doc.json -d ../docs/frontend --name "TAF UI"' -WorkingDir 'frontend'
    Run-Step -Title 'Build backend artifacts' -Command 'mvn -N -f pom.docker.xml install -q; mvn -f performance/pom.xml clean install -DskipTests -q; mvn clean package -DskipTests -q' -WorkingDir 'backend'
}

Write-Host "`nLocal verification completed successfully." -ForegroundColor Green
