# SI Relevés – Plateforme Centrale de Gestion des Relevés

##  Présentation

**SI Relevés** est une application backend (Backoffice Web + API Mobile) destinée à la gestion des relevés de compteurs **Eau** et **Électricité** pour **Rabat Énergie & Eau**.

Le système joue le rôle de **HUB CENTRAL** assurant :

* la synchronisation avec **Odoo (Commercial, RH, Facturation)**
* la communication avec l’**application mobile des agents terrain**
* l’administration via un **backoffice web sécurisé**

---

##  Architecture générale

```
si-releves
├── controller
│   ├── auth
│   ├── mobile
│   ├── backoffice
│   └── batch
├── service
│   ├── security
│   ├── mobile
│   ├── dashboard
│   └── batch
├── repository
│   ├── core
│   └── security
├── model
│   ├── core
│   └── security
├── dto
├── security
└── config
```

---

## 🔐 Sécurité & Authentification

### JWT

* Authentification via **JWT (HS256)**
* Header requis :

```
Authorization: Bearer <token>
```

### Rôles

| Rôle            | Description             |
| --------------- | ----------------------- |
| ROLE_SUPERADMIN | Administration complète |
| ROLE_USER       | Utilisateur backoffice  |
| ROLE_AGENT      | Agent terrain (mobile)  |

### Durée des tokens

* Backoffice : configurable
* Mobile : **10 minutes (inactivité)**

---

## API Mobile

### 🔐 Authentification Agent

**POST** `/api/mobile/auth/login`

**Paramètre (Query)**

| Nom        | Type      | Description       |
| ---------- | --------- | ----------------- |
| secretCode | String(6) | Code secret agent |

**Exemple**

```http
POST /api/mobile/auth/login?secretCode=123456
```

**Réponse**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

###  Home Mobile – Liste des adresses

**GET** `/api/mobile/home/addresses`

JWT requis (ROLE_AGENT)

**Réponse**

```json
[
  {
    "addressId": 10,
    "fullAddress": "Av. Mohammed V, Immeuble A",
    "district": "HAY RIAD",
    "allRead": false,
    "counters": [
      {
        "counterId": 1001,
        "type": "WATER",
        "lastIndex": 1250,
        "read": false
      }
    ]
  }
]
```

**Filtres supportés**

* `status` : READ / NOT_READ
* `type` : WATER / ELECTRICITY
* `search` : adresse ou identifiant compteur

---

Liste des compteurs

Method: GET

URL: http://localhost:8585/api/mobile/home/counters?district=HAY RIAD

Header: Authorization: Bearer <token>

 Response : liste des compteurs avec statut relevé/non relevé

###  Enregistrement d’un relevé

**POST** `/api/mobile/readings`

🔒 JWT requis (ROLE_AGENT)

**Body JSON**

```json
{
  "counterId": 1001,
  "newIndex": 1300
}
```

**Règles métier**

* Nouvel index ≥ ancien index
* Date/heure générées automatiquement
* Un seul relevé actif par compteur

---

##  API Backoffice

###  Authentification

**POST** `/api/auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

---

###  Gestion des utilisateurs

| Méthode | Endpoint                       | Rôle       |
| ------- | ------------------------------ | ---------- |
| GET     | /api/users                     | SUPERADMIN |
| POST    | /api/users                     | SUPERADMIN |
| PUT     | /api/users/{id}                | SUPERADMIN |
| DELETE  | /api/users/{id}                | SUPERADMIN |
| POST    | /api/users/{id}/reset-password | SUPERADMIN |

---

##  Batch & Synchronisation Odoo

### Import Odoo (Clients / Agents)

**POST** `/api/batch/odoo/import`

* Import depuis :

  * Odoo Commercial (Clients, Adresses)
  * Odoo RH (Agents terrain)
* Mapping via `odoo_id`
* Idempotent (update si existe)

---

### Export vers Odoo Facturation

**POST** `/api/batch/odoo/export-readings`

* Calcul consommation = nouvel index - ancien index
* Agrégation par client / compteur

---

## 📊 Dashboards (en cours)

* Taux de couverture des relevés
* Nombre de relevés par agent
* Statistiques par district

---

## 🧪 Environnement & Lancement

### Prérequis

* Java 21
* MySQL 8
* Maven 3.9+

### Lancement

```bash
mvn clean spring-boot:run
```

---

## 📎 Notes importantes

* Toutes les routes sont sécurisées par JWT sauf login
* Les agents sont strictement limités à leur district
* Validation métier côté serveur 

---


---

 **Projet SI Relevés – Rabat Énergie & Eau**
