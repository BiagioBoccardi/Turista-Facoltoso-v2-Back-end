package com.demo.model;

import java.util.UUID;

public class Utente {
    protected UUID id;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String password;
    protected String indirizzo;

    public Utente() {
    }

    public Utente(String cognome, String email, UUID id, String indirizzo, String nome) {
        this.cognome = cognome;
        this.email = email;
        this.id = id;
        this.indirizzo = indirizzo;
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    


}
