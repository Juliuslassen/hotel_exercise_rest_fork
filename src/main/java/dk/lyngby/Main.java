package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import io.javalin.Javalin;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int id =0;
        ApplicationConfig
            .startServer(
                Javalin.create(),
                Integer.parseInt(ApplicationConfig.getProperty("javalin.port")));
    }
}