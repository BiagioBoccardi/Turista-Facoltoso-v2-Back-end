package com.demo.controller;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import com.demo.model.HostRequest;
import com.demo.model.Utente;
import com.demo.service.Service;

import io.javalin.Javalin;

public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public void registerRoutes(Javalin app) {
        app.get("/abitazioni/host/{codiceHost}", ctx -> {
            try {
                String codiceHost = ctx.pathParam("codiceHost");
                ctx.json(service.getAbitazioniByCodiceHost(codiceHost));
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/utenti", ctx -> {
            try {
                ctx.json(service.getAllUtenti());
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.post("/utenti", ctx -> {
            try {
                Utente utente = ctx.bodyAsClass(Utente.class);
                if (utente.getId() == null) {
                    utente.setId(UUID.randomUUID());
                }
                service.createUtente(utente);
                ctx.status(201).json(utente);
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.put("/utenti/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                Utente utente = ctx.bodyAsClass(Utente.class);
                utente.setId(utenteId);
                boolean updated = service.updateUtente(utente);
                if (!updated) {
                    ctx.status(404).result("Utente non trovato");
                } else {
                    ctx.json(utente);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.delete("/utenti/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                boolean deleted = service.deleteUtente(utenteId);
                if (!deleted) {
                    ctx.status(404).result("Utente non trovato");
                } else {
                    ctx.status(204);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.post("/host", ctx -> {
            try {
                HostRequest request = ctx.bodyAsClass(HostRequest.class);
                service.createHost(request, false);
                ctx.status(201).json(request);
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });
        app.get("/host", ctx -> {
        try {
            ctx.json(service.getAllHost());
        } catch (SQLException e) {
            handleSqlError(ctx, e);
        }
    });


        app.post("/super-host", ctx -> {
            try {
                HostRequest request = ctx.bodyAsClass(HostRequest.class);
                service.createHost(request, true);
                ctx.status(201).json(request);
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.put("/host/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                HostRequest request = ctx.bodyAsClass(HostRequest.class);
                boolean updated = service.updateHost(utenteId, request, false);
                if (!updated) {
                    ctx.status(404).result("Host non trovato");
                } else {
                    ctx.json(request);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.put("/super-host/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                HostRequest request = ctx.bodyAsClass(HostRequest.class);
                boolean updated = service.updateHost(utenteId, request, true);
                if (!updated) {
                    ctx.status(404).result("Super host non trovato");
                } else {
                    ctx.json(request);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.delete("/host/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                boolean deleted = service.deleteHost(utenteId);
                if (!deleted) {
                    ctx.status(404).result("Host non trovato");
                } else {
                    ctx.status(204);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.delete("/super-host/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                boolean deleted = service.deleteHost(utenteId);
                if (!deleted) {
                    ctx.status(404).result("Super host non trovato");
                } else {
                    ctx.status(204);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/prenotazioni/ultima/{utenteId}", ctx -> {
            try {
                UUID utenteId = UUID.fromString(ctx.pathParam("utenteId"));
                Map<String, Object> result = service.getUltimaPrenotazioneByUtenteId(utenteId);
                if (result == null) {
                    ctx.status(404).result("Prenotazione non trovata");
                } else {
                    ctx.json(result);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/abitazioni/piu-gettonata", ctx -> {
            try {
                Map<String, Object> result = service.getAbitazionePiuGettonataUltimoMese();
                if (result == null) {
                    ctx.status(404).result("Nessuna abitazione trovata");
                } else {
                    ctx.json(result);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/host/piu-prenotazioni", ctx -> {
            try {
                ctx.json(service.getHostConPiuPrenotazioniUltimoMese());
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/super-host", ctx -> {
            try {
                ctx.json(service.getAllSuperHost());
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/utenti/top5-giorni", ctx -> {
            try {
                ctx.json(service.getTop5UtentiPiuGiorniUltimoMese());
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });

        app.get("/abitazioni/avg-posti-letto", ctx -> {
            try {
                Double media = service.getNumeroMedioPostiLetto();
                if (media == null) {
                    ctx.status(404).result("Nessun dato disponibile");
                } else {
                    ctx.json(media);
                }
            } catch (SQLException e) {
                handleSqlError(ctx, e);
            }
        });
        app.get("/utenti/count", ctx -> {
    try {
        ctx.json(service.countUtenti());
    } catch (SQLException e) {
        handleSqlError(ctx, e);
    }
});

    app.get("/host/count", ctx -> {
        try {
            ctx.json(service.countHost());
        } catch (SQLException e) {
            handleSqlError(ctx, e);
        }
    });

    app.get("/super-host/count", ctx -> {
        try {
            ctx.json(service.countSuperHost());
        } catch (SQLException e) {
            handleSqlError(ctx, e);
        }
    });


    }

    private void handleSqlError(io.javalin.http.Context ctx, SQLException e) {
        e.printStackTrace();
        ctx.status(500).result("Errore database");
    }
}
