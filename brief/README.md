# Documentazione Software

la struttura del progetto:

- brief/
  - [pom.xml](pom.xml) ----> file relativi alle dipendenze installate
  - [README.md](README.md) ---> Documentazione del progetto
  - [image.png](images/image.png) ---> Diagramma ER
  - src/
    - main/
      - java/
        - com/
          - demo/
            - [App.java](src/main/java/com/demo/App.java)
            - controller/
              - [Controller.java](src/main/java/com/demo/controller/Controller.java)
            - model/
              - [Abitazione.java](src/main/java/com/demo/model/Abitazione.java)
              - [Host.java](src/main/java/com/demo/model/Host.java)
              - [SuperHost.java](src/main/java/com/demo/model/SuperHost.java)
              - [Utente.java](src/main/java/com/demo/model/Utente.java)
            - Repository/
              - dml/
                - [HostRepository.java](src/main/java/com/demo/Repository/dml/HostRepository.java)
                - [SuperHostRepository.java](src/main/java/com/demo/Repository/dml/SuperHostRepository.java)
                - [UtenteRepository.java](src/main/java/com/demo/Repository/dml/UtenteRepository.java)
            - service/
              - [Service.java](src/main/java/com/demo/service/Service.java)
            - Utils/
              - [DbConnection.java](src/main/java/com/demo/Utils/DbConnection.java)
    - test/
      - java/
        - com/
          - demo/
            - [AppTest.java](src/test/java/com/demo/AppTest.java)

# Stack Tecnologico Backend

| Linguaggio / Libreria / Tool          | Descrizione                                                                                                       |
| ------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| **Java**                              | Linguaggio di programmazione principale; versione utilizzata (verifica da `pom.xml`).                             |
| **Javalin**                           | Framework web leggero per Java per lo sviluppo di API REST e applicazioni web.                                    |
| **JDBC (Java Database Connectivity)** | API Java per l'accesso ai database relazionali. Utilizzato per eseguire query SQL tramite `PreparedStatement`.    |
| **PostgreSQL**                        | Database relazionale utilizzato per la persistenza dei dati (`jdbc:postgres://localhost:5432/turista_facoltoso`). |
| **Maven**                             | Tool di build e gestione dipendenze; utilizza `pom.xml` per la configurazione del progetto.                       |
| **UUID**                              | Classe Java per generare identificatori univoci; usata per `id` in Host e Utente.                                 |
| **PreparedStatement**                 | Classe JDBC per esecuzione di query SQL parametrizzate con placeholder `?` per prevenire SQL injection.           |
| **JUnit**                             | Framework per testing (test unit, verifica in `AppTest.java`).       
                                             |

# Stack Tecnologico Front-end

| Linguaggio / Libreria / Tool | Descrizione                                                                                             |
| ---------------------------- | ------------------------------------------------------------------------------------------------------- |
| **React**                    | Framework usato per l'interfaccia UI/UX dell'applicazione.                                              |
| **ShadcnUI**                 | Libreria di componenti UI/UX dell'applicazione`.                                                        |
| **ZOD**                      | Libreria usata per la validazione dei dati inseriti dall’utente nel front-end..                         |
| **UUID**                     | Classe Java per generare identificatori univoci; usata per `id` in Host e Utente.                       |
| **JDBC**                     |Una libreria di java che fornisce un'interfaccia standardizzata per comunicare con database relazionali.                    |
| **JUnit**                    | Framework per testing (test unit, verifica in `AppTest.java`).                                          |

## Spiegazione di JDBC

- **JDBC (Java Database Connectivity)** è un'API Java che fornisce un'interfaccia standardizzata per comunicare con database relazionali. Nel progetto, JDBC è utilizzato per:
- **Connessione al Database**: Stabilisce una connessione con PostgreSQL tramite `DbConnection.getConnection()`
- **Esecuzione di Query**: Utilizza `PreparedStatement` per eseguire query SQL parametrizzate (INSERT, SELECT, UPDATE, DELETE)
- **Gestione dei Dati**: Consente di passare parametri in modo sicuro usando placeholder `?` per prevenire SQL injection
- **Gestione delle Eccezioni**: Cattura le eccezioni `SQLException` durante le operazioni database

Esempio di utilizzo nel progetto:
```java
try (Connection conn = DbConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setObject(1, host.getId());
    stmt.setString(2, host.getNome());
    stmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();
}
```

# Interfaccia Software

L’interfaccia fornisce un controllo per avviare l’operazione e visualizza il risultato richiesto
in forma testuale o tabellare.

# Cosa Gestisce

| Entità           | Operazioni                                                          |
| ---------------- | ------------------------------------------------------------------- |
| **Prenotazione** | Creare una nuova prenotazione, Modificarla, Visualizzare in tabella |
| **Host**         | Creare un nuovo host, Modificarlo, Visualizzare in tabella          |
| **Super_host**   | Creare un nuovo super_host, Modificarlo, Visualizzare in tabella    |
| **Utente**       | Creare un nuovo utente, Modificarlo, Visualizzare in tabella        |

# Vincoli Database
Non vogliamo che due utenti prenotino la **stessa abitazione**
nello **stesso periodo**. Per questo nel database c’è un trigger PostgreSQL
chiamato `trg_prenotazione_no_overlap` sulla tabella `PRENOTAZIONE`.

Quando provo a inserire o modificare una prenotazione, 
il trigger controlla se esiste già un'altra prenotazione con lo **stesso `abitazione_id`** e con **date che si sovrappongono**
(`data_inizio`/`data_fine`). 
Se la sovrapposizione c’è, il database lancia un errore
e l’operazione viene bloccata.


Esempio di codice (Java + JDBC) che crea funzione e trigger:
```java
try (Statement stmt = connection.createStatement()) {
    stmt.execute(
        "CREATE OR REPLACE FUNCTION prenotazione_no_overlap() " +
        "RETURNS TRIGGER AS $$ " +
        "BEGIN " +
        "IF EXISTS ( " +
        "SELECT 1 FROM PRENOTAZIONE p " +
        "WHERE p.abitazione_id = NEW.abitazione_id " +
        "AND p.id <> NEW.id " +
        "AND p.data_inizio <= NEW.data_fine " +
        "AND NEW.data_inizio <= p.data_fine " +
        ") THEN " +
        "RAISE EXCEPTION 'Prenotazione sovrapposta per la stessa abitazione'; " +
        "END IF; " +
        "RETURN NEW; " +
        "END; " +
        "$$ LANGUAGE plpgsql"
    );
    stmt.execute(
        "DROP TRIGGER IF EXISTS trg_prenotazione_no_overlap ON PRENOTAZIONE"
    );
    stmt.execute(
        "CREATE TRIGGER trg_prenotazione_no_overlap " +
        "BEFORE INSERT OR UPDATE ON PRENOTAZIONE " +
        "FOR EACH ROW EXECUTE FUNCTION prenotazione_no_overlap()"
    );
}
```

# Endpoint Controller (Javalin)

| Metodo | Endpoint | Descrizione |
| ------ | -------- | ----------- |
| GET | `/abitazioni/host/{codiceHost}` | Ottiene le abitazioni corrispondenti a un certo codice host |
| GET | `/prenotazioni/ultima/{utenteId}` | Ottiene l'ultima prenotazione dato un id utente |
| GET | `/abitazioni/piu-gettonata` | Ottiene l'abitazione pi� gettonata nell'ultimo mese |
| GET | `/host/piu-prenotazioni` | Ottiene gli host con pi� prenotazioni nell'ultimo mese |
| GET | `/super-host` | Ottiene tutti i super-host |
| GET | `/utenti/top5-giorni` | Ottiene i 5 utenti con pi� giorni prenotati nell'ultimo mese |
| GET | `/abitazioni/avg-posti-letto` | Ottiene il numero medio di posti letto su tutte le abitazioni |
