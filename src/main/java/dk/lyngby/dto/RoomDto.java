package dk.lyngby.dto;

import dk.lyngby.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RoomDto {
    private Integer roomNumber;
    private Integer roomPrice;
    private Room.RoomType roomType;
    private double squareMeters;

    public RoomDto(Room room) {
        this.roomNumber = room.getRoomNumber();
        this.roomPrice = room.getRoomPrice().intValue();
        this.roomType = room.getRoomType();
        this.squareMeters = room.getSquareMeters();
    }

    public static List<RoomDto> toRoomDTOList(List<Room> rooms) {
        return List.of(rooms.stream().map(RoomDto::new).toArray(RoomDto[]::new));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof RoomDto roomDto)) return false;

        if (getRoomNumber() != null ? !getRoomNumber().equals(roomDto.getRoomNumber()) : roomDto.getRoomNumber() != null)
            return false;
        if (getRoomPrice() != null ? !getRoomPrice().equals(roomDto.getRoomPrice()) : roomDto.getRoomPrice() != null)
            return false;
        return getRoomType() == roomDto.getRoomType();
    }

    @Override
    public int hashCode()
    {
        int result = getRoomNumber() != null ? getRoomNumber().hashCode() : 0;
        result = 31 * result + (getRoomPrice() != null ? getRoomPrice().hashCode() : 0);
        result = 31 * result + (getRoomType() != null ? getRoomType().hashCode() : 0);
        return result;
    }
}
