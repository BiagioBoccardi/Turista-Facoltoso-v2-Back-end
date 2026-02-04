package com.demo.Repository.dml;

import java.sql.SQLException;
import java.util.UUID;

public class SuperHostRepository {

    private final HostRepository hostRepository;

    public SuperHostRepository() {
        this.hostRepository = new HostRepository();
    }

    public void insertSuperHost(UUID utenteId, String codiceHost) throws SQLException {
        hostRepository.insertHost(utenteId, codiceHost, true);
    }
}


