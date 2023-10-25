package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false, unique = true)
    private Integer roomId;

    @Setter
    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Setter
    @Column(name = "price", nullable = false)
    private BigDecimal roomPrice;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Setter
    @Column(name = "square_meters", nullable = false)
    private double squareMeters;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    public Room(Integer roomNumber, BigDecimal roomPrice, RoomType roomType, double squareMeters) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.squareMeters = squareMeters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber) && Objects.equals(hotel, room.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, hotel);
    }

    public enum RoomType {
        SINGLE, DOUBLE, SUITE
    }
}
