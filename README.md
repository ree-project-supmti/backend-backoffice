# SI Relevés – Plateforme Centrale de Gestion des Relevés

##  Présentation

**SI Relevés** est une application backend (Backoffice Web + API Mobile) destinée à la gestion des relevés de compteurs **Eau** et **Électricité** pour **Rabat Énergie & Eau**.

Le système joue le rôle de **HUB CENTRAL** assurant :

* la synchronisation avec **Odoo (Commercial, RH, Facturation)**
* la communication avec l’**application mobile des agents terrain**
* l’administration via un **backoffice web sécurisé**

---

📘 API Documentation – SI Relevés (Backoffice & Users)
🌐 Base URL
http://localhost:8585


Toutes les routes (sauf auth) nécessitent un JWT Token.

🔐 Authentification
➤ Login (Backoffice / Admin)
POST /api/auth/login


Body

{
  "username": "user1",
  "password": "password"
}


Response

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}


➡️ Le token doit être envoyé dans le header :

Authorization: Bearer <JWT>

👤 Utilisateurs (Backoffice)
➤ Informations utilisateur connecté
GET /api/users/me


Headers

Authorization: Bearer <JWT>


Response

{
  "uuid": "d6f4c1a9-...",
  "firstName": "Ali",
  "lastName": "Ben Salah",
  "username": "ali.user",
  "enabled": true,
  "mustChangePassword": true,
  "createdAt": "2025-01-10T12:00:00"
}

➤ Changer son mot de passe
POST /api/users/change-password


Roles : USER, SUPERADMIN

Body

{
  "newPassword": "NewPassword123!"
}


Response

204 No Content

➤ Reset mot de passe (Admin / Superadmin)
POST /api/admin/users/{uuid}/reset-password


➡️ Génère un nouveau mot de passe aléatoire
➡️ L’utilisateur devra le changer à la prochaine connexion

⚙️ Gestion des Compteurs (Backoffice)

Base path

/api/backoffice/counters


Rôle requis : USER

➤ Créer un compteur
POST /api/backoffice/counters


Body

{
  "addressId": 1,
  "type": "WATER"
}


Règles métier

SerialNumber auto-généré (9 chiffres : 000000001)

1 compteur par type (WATER / ELECTRICITY)

Max 2 compteurs par adresse (sauf immeuble)

Adresse doit avoir un client

Response

{
  "id": 5,
  "serialNumber": "000000005",
  "type": "WATER",
  "active": true,
  "odooId": null,
  "client": { "id": 1 },
  "address": { "id": 1 }
}

➤ Liste des compteurs
GET /api/backoffice/counters


Response

[
  {
    "id": 1,
    "serialNumber": "000000001",
    "type": "WATER",
    "active": true
  },
  {
    "id": 2,
    "serialNumber": "000000002",
    "type": "ELECTRICITY",
    "active": true
  }
]

➤ Détail d’un compteur
GET /api/backoffice/counters/{id}


Response

{
  "id": 1,
  "serialNumber": "000000001",
  "type": "WATER",
  "active": true
}

➤ Modifier un compteur (activation / désactivation)
PUT /api/backoffice/counters/{id}


Body

{
  "active": false
}


Response

{
  "id": 1,
  "serialNumber": "000000001",
  "active": false
}

➤ Supprimer un compteur (soft delete)
DELETE /api/backoffice/counters/{id}


➡️ Le compteur n’est pas supprimé physiquement
➡️ active = false

Response

204 No Content

📱 APIs Mobile (Agent)
➤ Login Mobile
POST /api/mobile/auth/login

➤ Home Mobile (Adresses + Relevé / Non relevé)
GET /api/mobile/home


➡️ Retourne la liste des adresses affectées à l’agent
➡️ Statut relevé / non relevé

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
