# Turista Facoltoso - Backend API

Backend REST in Java per la gestione di utenti, host, abitazioni e prenotazioni.

## Panoramica
Il progetto espone API HTTP tramite Javalin e persiste i dati su PostgreSQL con JDBC.
Lo schema viene inizializzato all'avvio e puo essere popolato automaticamente con dati di test.

## Architettura
- `controller`: gestione delle rotte HTTP e mapping request/response.
- `service`: logica applicativa.
- `Repository/dml`: operazioni CRUD (INSERT/UPDATE/DELETE).
- `Repository/query`: query di lettura e reportistica.
- `Repository/ddl`: creazione tabelle, funzioni e trigger database.
- `Utils`: connessione DB, seeding e bootstrap.

## Stack Tecnologico
- Java 21
- Maven
- Javalin 6.7.0
- PostgreSQL JDBC 42.7.8
- Jackson Databind 2.17.2
- SLF4J Simple 2.0.16
- JUnit 4.11

## Modello Dati
Entita principali:
- `UTENTE`
- `HOST` (1:1 con `UTENTE`)
- `ABITAZIONE` (N:1 verso `HOST`)
- `PRENOTAZIONE` (N:1 verso `UTENTE` e `ABITAZIONE`)

### Modello Prenotazione (API CRUD dedicata)
Il modello applicativo `Prenotazione` espone i campi:
- `id`
- `name`
- `address`
- `locali`

Nota: la tabella `PRENOTAZIONE` contiene anche altri campi obbligatori (`data_inizio`, `data_fine`, `utente_id`, `abitazione_id`). In creazione vengono valorizzati automaticamente dal repository.

## Regole di Business
### Super-host automatico
Un host e super-host se ha ricevuto **almeno 100 prenotazioni cumulative** dalla registrazione.

Implementazione:
- trigger DB su `PRENOTAZIONE` (`INSERT/UPDATE/DELETE`)
- aggiornamento automatico di `HOST.is_superhost`
- sincronizzazione di tutti gli host durante l'inizializzazione schema

Conseguenza API:
- `POST /super-host` -> `400`
- `PUT /super-host/{utenteId}` -> `400`

### Vincolo anti-overlap prenotazioni
Trigger PostgreSQL blocca prenotazioni sovrapposte sulla stessa abitazione.

### Feedback
- Lasciabile solo da utenti che hanno **gia soggiornato** in un'abitazione.
- Riferito al proprietario dell'abitazione prenotata (derivato da prenotazione -> abitazione -> host).
- Un solo feedback per prenotazione.
- Punteggio obbligatorio da 1 a 5.

## Configurazione
Le credenziali DB sono in:
- `src/main/java/com/demo/Utils/DbConnection.java`

Valori attuali:
- URL: `jdbc:postgresql://localhost:5432/turista_facoltoso`
- User: `postgres`
- Password: configurata nel file

## Avvio Locale
1. Crea il database PostgreSQL `turista_facoltoso`.
2. Verifica credenziali in `DbConnection.java`.
3. Avvia l'applicazione (`com.demo.App`).

All'avvio vengono eseguiti:
- creazione schema (`SchemaInitializer.init()`)
- seeding (`DatabaseSeeder.seedIfEmpty()`)

### Seeder
File: `src/main/java/com/demo/Utils/DatabaseSeeder.java`
- `FORCE_RESEED = true`: pulisce e ripopola sempre il DB
- `FORCE_RESEED = false`: popola solo se vuoto

Il seeder genera anche prenotazioni extra per portare almeno un host oltre la soglia super-host.

## Endpoints REST

### Utenti
- `GET /utenti`
- `POST /utenti`
- `PUT /utenti/{utenteId}`
- `DELETE /utenti/{utenteId}`
- `GET /utenti/count`

### Prenotazioni (CRUD)
- `GET /prenotazioni`
- `POST /prenotazioni`
- `PUT /prenotazioni/{prenotazioneId}`
- `DELETE /prenotazioni/{prenotazioneId}`
- `GET /prenotazioni/ultima/{utenteId}`
- `GET /prenotazioni/{prenotazioneId}/feedback`

### Feedback
- `GET /feedback`
- `POST /feedback`
- `PUT /feedback/{feedbackId}`
- `DELETE /feedback/{feedbackId}`

### Host
- `GET /host`
- `POST /host`
- `PUT /host/{utenteId}`
- `DELETE /host/{utenteId}`
- `GET /host/count`
- `GET /host/piu-prenotazioni`

### Super-host
- `GET /super-host`
- `GET /super-host/count`
- `POST /super-host` (bloccato: 400)
- `PUT /super-host/{utenteId}` (bloccato: 400)
- `DELETE /super-host/{utenteId}`

### Abitazioni / Report
- `GET /abitazioni/host/{codiceHost}`
- `GET /abitazioni/piu-gettonata`
- `GET /abitazioni/avg-posti-letto`
- `GET /utenti/top5-giorni`

### Debug
- `GET /debug/super-host/top3`
- `POST /debug/super-host/top3`

Questi endpoint forzano i top 3 host (per `codice_host`) a `is_superhost = true` e sono pensati solo per test rapido.

## Esempi Payload

### `POST /utenti`
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario@example.com",
  "indirizzo": "Via Roma 1",
  "password": "pw"
}
```

### `POST /host`
```json
{
  "nome": "Giulia",
  "cognome": "Bianchi",
  "email": "giulia@example.com",
  "indirizzo": "Via Milano 10",
  "password": "pw",
  "codice_host": "H-1009"
}
```

### `POST /prenotazioni`
```json
{
  "name": "Prenotazione demo",
  "address": "Via Test 12",
  "locali": 2
}
```

### `POST /feedback`
```json
{
  "prenotazione_id": "00000000-0000-0000-0000-000000000000",
  "titolo": "Ottima esperienza",
  "testo": "Esperienza ottima, servizio rapido.",
  "punteggio": 5
}
```

## Build e Test
Comandi Maven principali:
- `mvn clean package`
- `mvn test`

## Struttura Progetto (sintesi)
```text
brief/
  pom.xml
  README.md
  src/main/java/com/demo/
    App.java
    controller/
    model/
    service/
    Repository/
      ddl/
      dml/
      query/
    Utils/
```


