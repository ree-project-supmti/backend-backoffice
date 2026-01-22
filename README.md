# REE - API

[![Build and Quality Analysis](https://github.com/ulysse-pilot/backend-backoffice/actions/workflows/build-and-quality.yml/badge.svg)](https://github.com/ulysse-pilot/backend-backoffice/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ree-project-supmti_backend-backoffice&metric=alert_status&token=4d2303fdc5ad9b2f5fd4af5b95b460741d592464)](https://sonarcloud.io/summary/new_code?id=ree-project-supmti_backend-backoffice)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ree-project-supmti_backend-backoffice&metric=coverage&token=4d2303fdc5ad9b2f5fd4af5b95b460741d592464)](https://sonarcloud.io/summary/new_code?id=ree-project-supmti_backend-backoffice)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ree-project-supmti_backend-backoffice&metric=code_smells&token=4d2303fdc5ad9b2f5fd4af5b95b460741d592464)](https://sonarcloud.io/summary/new_code?id=ree-project-supmti_backend-backoffice)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ree-project-supmti_backend-backoffice&metric=security_rating&token=4d2303fdc5ad9b2f5fd4af5b95b460741d592464)](https://sonarcloud.io/summary/new_code?id=ree-project-supmti_backend-backoffice)

## Description
API Backend pour le projet REE construit avec Spring Boot 3.5.9 et Java 21.

## Métriques de Qualité
- **Build Status** : CI/CD automatisé avec GitHub Actions
- **Code Coverage** : Suivi par JaCoCo (objectif: >80%)
- **Analyse Statique** : SonarQube
- **Sécurité** : OWASP Dependency Check
- **Tests** : Tests unitaires et d'intégration

## Technologies
- **Framework :** Spring Boot 3.5.9
- **Java :** Version 21
- **Base de données :** PostgreSQL (prod) / H2 (tests)
- **Analyse de code :** SonarQube
- **Couverture :** JaCoCo
- **Sécurité :** OWASP Dependency Check

## Qualité du Code
Le projet respecte les standards de qualité suivants :
- **SonarQube** : Analyse statique complète
- **Maven Enforcer** : Gestion des dépendances
- **Tests automatisés** : Couverture de code minimale

## Setup Local
```bash
# Cloner le repo
git clone https://github.com/ree-project-supmti/backend-backoffice.git

# Lancer les tests avec couverture
mvn clean verify

# Analyser les dépendances
mvn dependency-check:check
```

## CI/CD Pipeline
Le pipeline GitHub Actions exécute automatiquement :
1. **Compilation** du code source
2. **Tests unitaires** et d'intégration
3. **Analyse de couverture** avec JaCoCo
4. **Scan de sécurité** avec OWASP
5. **Analyse qualité** avec SonarQube

## Rapports de Qualité
Les rapports sont générés automatiquement et disponibles via :
- **SonarQube** : Analyse complète de la qualité
- **GitHub Actions Artifacts** : Rapports détaillés
- **Badges** : Statut en temps réel dans ce README
