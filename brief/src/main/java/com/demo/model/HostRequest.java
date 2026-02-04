package com.demo.model;

import java.util.UUID;

public class HostRequest {
    private UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String indirizzo;
    private String password;
    private String codiceHost;

    public HostRequest() {
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

    public String getCodiceHost() {
        return codiceHost;
    }

    public void setCodiceHost(String codiceHost) {
        this.codiceHost = codiceHost;
    }
}
