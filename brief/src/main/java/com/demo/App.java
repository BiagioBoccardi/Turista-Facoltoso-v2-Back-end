package com.demo;

import com.demo.Repository.ddl.SchemaInitializer;
import com.demo.Utils.DatabaseSeeder;
import com.demo.controller.Controller;
import com.demo.service.Service;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class App {
    public static void main(String[] args) {
        SchemaInitializer.init();
        try {
            DatabaseSeeder.seedIfEmpty();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        Service service = new Service();
        Controller controller = new Controller(service);

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors ->
            cors.addRule(CorsPluginConfig.CorsRule::anyHost)
    );
        }).start(8080);

        controller.registerRoutes(app);
    }
}
