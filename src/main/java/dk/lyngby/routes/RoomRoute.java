package dk.lyngby.routes;

import dk.lyngby.controller.impl.RoomController;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRoute {

    private final RoomController roomController = new RoomController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/rooms", () -> {
                post("/hotel/{id}", roomController::create, RouteRoles.ADMIN, RouteRoles.MANAGER);
                get("/", roomController::readAll, RouteRoles.ANYONE);
                get("/{id}", roomController::read, RouteRoles.ADMIN, RouteRoles.MANAGER);
                get("/pricefrom={min}&priceto={max}", roomController::readByPrice, RouteRoles.ANYONE);
                put("/{id}", roomController::update, RouteRoles.ADMIN, RouteRoles.MANAGER);
                delete("/{id}", roomController::delete, RouteRoles.ADMIN, RouteRoles.MANAGER);
            });
        };
    }
}