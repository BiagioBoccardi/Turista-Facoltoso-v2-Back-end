package com.demo.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.demo.Repository.dml.HostRepository;
import com.demo.Repository.dml.SuperHostRepository;
import com.demo.Repository.dml.UtenteRepository;
import com.demo.Repository.query.QueryRepository;
import com.demo.model.HostRequest;
import com.demo.model.Utente;

// qui abbiamo la logica di Business
public class Service {

    private final QueryRepository queryRepository;
    private final UtenteRepository utenteRepository;
    private final HostRepository hostRepository;
    private final SuperHostRepository superHostRepository;

    public Service() {
        this.queryRepository = new QueryRepository();
        this.utenteRepository = new UtenteRepository();
        this.hostRepository = new HostRepository();
        this.superHostRepository = new SuperHostRepository();
    }

    public List<Map<String, Object>> getAbitazioniByCodiceHost(String codiceHost) throws SQLException {
        return queryRepository.getAbitazioniByCodiceHost(codiceHost);
    }

    public List<Map<String, Object>> getAllUtenti() throws SQLException {
        return queryRepository.getAllUtenti();
    }

    public List<Map<String, Object>> getAllHost() throws SQLException {
        return queryRepository.getAllHost();
    }

    public Map<String, Object> getUltimaPrenotazioneByUtenteId(UUID utenteId) throws SQLException {
        return queryRepository.getUltimaPrenotazioneByUtenteId(utenteId);
    }

    public Map<String, Object> getAbitazionePiuGettonataUltimoMese() throws SQLException {
        return queryRepository.getAbitazionePiuGettonataUltimoMese();
    }

    public List<Map<String, Object>> getHostConPiuPrenotazioniUltimoMese() throws SQLException {
        return queryRepository.getHostConPiuPrenotazioniUltimoMese();
    }

    public List<Map<String, Object>> getAllSuperHost() throws SQLException {
        return queryRepository.getAllSuperHost();
    }

    public List<Map<String, Object>> getTop5UtentiPiuGiorniUltimoMese() throws SQLException {
        return queryRepository.getTop5UtentiPiuGiorniUltimoMese();
    }

    public Double getNumeroMedioPostiLetto() throws SQLException {
        return queryRepository.getNumeroMedioPostiLetto();
    }
    public long countUtenti() throws SQLException {
    return queryRepository.countUtenti();
    }

    public long countHost() throws SQLException {
        return queryRepository.countHost();
    }

    public long countSuperHost() throws SQLException {
        return queryRepository.countSuperHost();
    }

    public void createUtente(Utente utente) throws SQLException {
        utenteRepository.insertUtente(utente);
    }

    public boolean updateUtente(Utente utente) throws SQLException {
        return utenteRepository.updateUtente(utente);
    }

    public boolean deleteUtente(UUID utenteId) throws SQLException {
        return utenteRepository.deleteUtenteById(utenteId);
    }

    public void createHost(HostRequest request, boolean isSuperhost) throws SQLException {
        Utente utente = toUtente(request);
        if (utente.getId() == null) {
            utente.setId(UUID.randomUUID());
        }
        request.setId(utente.getId());
        try {
            utenteRepository.insertUtente(utente);
            if (isSuperhost) {
                superHostRepository.insertSuperHost(utente.getId(), request.getCodiceHost());
            } else {
                hostRepository.insertHost(utente.getId(), request.getCodiceHost(), false);
            }
        } catch (SQLException e) {
            try {
                utenteRepository.deleteUtenteById(utente.getId());
            } catch (SQLException ignored) {
            }
            throw e;
        }
    }

    public boolean updateHost(UUID utenteId, HostRequest request, boolean isSuperhost) throws SQLException {
        Utente utente = toUtente(request);
        utente.setId(utenteId);
        boolean updatedUtente = utenteRepository.updateUtente(utente);
        boolean updatedHost = hostRepository.updateHost(utenteId, request.getCodiceHost(), isSuperhost);
        return updatedUtente && updatedHost;
    }

    public boolean deleteHost(UUID utenteId) throws SQLException {
        return utenteRepository.deleteUtenteById(utenteId);
    }

    private Utente toUtente(HostRequest request) {
        Utente utente = new Utente();
        utente.setId(request.getId());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setEmail(request.getEmail());
        utente.setIndirizzo(request.getIndirizzo());
        utente.setPassword(request.getPassword());
        return utente;
    }

}
