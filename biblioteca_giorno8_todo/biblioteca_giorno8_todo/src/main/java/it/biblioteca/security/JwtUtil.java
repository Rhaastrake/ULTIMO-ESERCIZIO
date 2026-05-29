package it.biblioteca.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * GIORNO 7 — JwtUtil
 *
 * Utility per generare e validare JSON Web Token (JWT).
 *
 * STRUTTURA DI UN JWT:
 * Un JWT e composto da tre parti separate da un punto (.):
 *
 *   HEADER.PAYLOAD.SIGNATURE
 *
 * Esempio reale (decodificabile su jwt.io):
 *   eyJhbGciOiJIUzI1NiJ9          <- Header (Base64): algoritmo usato
 *   .eyJzdWIiOiJtYXJpbyIsInJvb... <- Payload (Base64): i dati (claims)
 *   .SflKxwRJSMeKKF2QT4fwpMeJf... <- Signature: garantisce l'integrita
 *
 * CLAIMS principali nel Payload:
 *   sub  (subject)    = chi e il token (es. username)
 *   iat  (issued at)  = quando e stato creato
 *   exp  (expiration) = quando scade
 *   + claims custom (es. ruolo, email)
 *
 * COME FUNZIONA L'AUTENTICAZIONE CON JWT:
 * 1. Client invia username + password al server
 * 2. Server verifica le credenziali
 * 3. Server genera un JWT e lo restituisce al client
 * 4. Client salva il JWT (es. in localStorage o cookie)
 * 5. Per ogni richiesta successiva, il client invia il JWT
 *    nell'header HTTP: Authorization: Bearer <token>
 * 6. Server valida la firma del JWT — nessuna sessione server-side!
 */
public class JwtUtil {

    // Chiave segreta per firmare i token — in produzione va in un file di config
    // NON mettere mai la chiave nel codice sorgente in un progetto reale!
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("biblioteca-secret-key-lunga-almeno-256-bit!".getBytes());

    // Durata del token: 1 ora (in millisecondi)
    private static final long EXPIRATION_MS = 60 * 60 * 1000;

    /**
     * Genera un JWT per l'utente specificato.
     */
    public static String generaToken(String username, String ruolo) {
        return Jwts.builder()
                .subject(username)                          // chi e il token
                .claim("ruolo", ruolo)                     // claim custom
                .issuedAt(new Date())                      // quando creato
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)                      // firma con HMAC-SHA256
                .compact();                                 // costruisce la stringa
    }

    /**
     * Valida un token e restituisce i claims se valido.
     * Lancia eccezione se il token e scaduto, malformato o con firma errata.
     */
    public static Claims validaToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Estrae l'username dal token (senza validare la scadenza).
     * Usato per logging e diagnostica.
     */
    public static String estraiUsername(String token) {
        return validaToken(token).getSubject();
    }

    /**
     * Verifica se il token e ancora valido (firma corretta + non scaduto).
     */
    public static boolean isTokenValido(String token) {
        try {
            validaToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
