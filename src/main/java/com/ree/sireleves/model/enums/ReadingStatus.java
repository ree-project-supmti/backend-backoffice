package com.ree.sireleves.model.enums;

public enum ReadingStatus {
    PENDING,     // reçu depuis mobile
    VALIDATED,   // validé backoffice
    SENT,        // envoyé à Odoo Facturation
    ERROR        // rejeté / erreur
}
