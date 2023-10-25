package dk.lyngby.controller.impl;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.IController;
import dk.lyngby.dao.impl.RoomDao;
import dk.lyngby.dto.HotelDto;
import dk.lyngby.dto.RoomDto;
import dk.lyngby.exception.Message;
import dk.lyngby.model.Hotel;
import dk.lyngby.model.Room;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

public class RoomController implements IController<Room, Integer> {

    private RoomDao dao;

    public RoomController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = RoomDao.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Room room = dao.read(id);
        // dto
        RoomDto roomDto = new RoomDto(room);
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDto, RoomDto.class);

    }

    @Override
    public void readAll(Context ctx) {
        // entity
        List<Room> rooms = dao.readAll();
        // dto
        List<RoomDto> roomDtos = RoomDto.toRoomDTOList(rooms);
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDtos, RoomDto.class);

    }

    public void readByPrice(Context ctx){
        //request
        int minPrice = ctx.pathParamAsClass("min", Integer.class).check(i -> i > 0, "Not a valid price").get();
        int maxPrice = ctx.pathParamAsClass("max", Integer.class).check(i -> i > 0, "Not a valid price").get();

        if(minPrice > maxPrice){
            ctx.res().setStatus(400);
            ctx.json(new Message(400, "Min price cannot be higher than max price"));
            return;
        }

        List<Room> rooms = dao.readByPrice(minPrice, maxPrice);
        Room room = new Room(minPrice, new BigDecimal(1234), Room.RoomType.SUITE, maxPrice);
        rooms.add(room);

        List<RoomDto> roomDtos = RoomDto.toRoomDTOList(rooms);

        ctx.status(200);
        ctx.json(roomDtos, RoomDto.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        Room jsonRequest = validateEntity(ctx);

        int hotelId = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        Boolean hasRoom = validateHotelRoomNumber.apply(jsonRequest.getRoomNumber(), hotelId);

        if (hasRoom) {
            ctx.res().setStatus(400);
            ctx.json(new Message(400, "Room number already in use by hotel"));
            return;
        }

        // entity
        Hotel hotel = dao.addRoomToHotel(hotelId, jsonRequest);
        // dto
        HotelDto hotelDto = new HotelDto(hotel);
        // response
        ctx.res().setStatus(201);
        ctx.json(hotelDto, HotelDto.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        Room update = dao.update(id, validateEntity(ctx));
        // dto
        RoomDto roomDto = new RoomDto(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(roomDto, RoomDto.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // entity
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {return dao.validatePrimaryKey(integer);}

    // Checks if the room number is already in use by the hotel
    BiFunction<Integer, Integer, Boolean> validateHotelRoomNumber = (roomNumber, hotelId) -> dao.validateHotelRoomNumber(roomNumber, hotelId);

    @Override
    public Room validateEntity(Context ctx) {
        return ctx.bodyValidator(Room.class)
                .check(r -> r.getRoomNumber() != null && r.getRoomNumber() > 0, "Not a valid room number")
                .check(r -> r.getRoomType() != null, "Not a valid room type")
                .check(r -> r.getRoomPrice() != null , "Not a valid price")
                .get();
    }
}
