package com.demo.model;

import java.time.LocalDate;
import java.util.UUID;

public class Abitazione {
    private UUID id;
    private String name;
    private String indirizzo;
    private Integer num_locali;
    private Integer num_posti_letto;
    private Integer piano;
    private Double  prezzo;
    private LocalDate data_inizio;
    private LocalDate data_fine;

    public Abitazione(LocalDate data_fine, LocalDate data_inizio, UUID id, String indirizzo, String name, Integer num_locali, Integer num_posti_letto, Integer piano, Double prezzo) {
        this.data_fine = data_fine;
        this.data_inizio = data_inizio;
        this.id = id;
        this.indirizzo = indirizzo;
        this.name = name;
        this.num_locali = num_locali;
        this.num_posti_letto = num_posti_letto;
        this.piano = piano;
        this.prezzo = prezzo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Integer getNum_locali() {
        return num_locali;
    }

    public void setNum_locali(Integer num_locali) {
        this.num_locali = num_locali;
    }

    public Integer getNum_posti_letto() {
        return num_posti_letto;
    }

    public void setNum_posti_letto(Integer num_posti_letto) {
        this.num_posti_letto = num_posti_letto;
    }

    public Integer getPiano() {
        return piano;
    }

    public void setPiano(Integer piano) {
        this.piano = piano;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public LocalDate getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(LocalDate data_inizio) {
        this.data_inizio = data_inizio;
    }

    public LocalDate getData_fine() {
        return data_fine;
    }

    public void setData_fine(LocalDate data_fine) {
        this.data_fine = data_fine;
    }



    


    



}
