package vn.myhome.entity;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title",length = 150,nullable = false)
    @NotEmpty(message = "Khong duoc de trong !")
    private String title;

    @Column(name = "type",length = 150,nullable = false)
    @NotEmpty(message = "Khong duoc de trong !")
    private String type;

    @Column(name = "description",length = 255,nullable = false)
    @NotEmpty(message = "Khong duoc de trong !")
    private String description;

    @Column(name = "room_max",length = 15,nullable = false)
    @NotNull(message = "Khong duoc de trong !")
    private Integer roomMax;

    @Column(name = "room_size",length = 100,nullable = false)
    @NotNull(message = "Khong duoc de trong !")
    private Double roomSize;

    @Column(name = "room_bed",length = 15,nullable = false)
    @NotNull(message = "Khong duoc de trong !")
    private Integer roomBed;

    @Column(name = "available_room",length = 50,nullable = false)
    @NotNull(message = "Khong duoc de trong !")
    private long availableRoom;

    @Column(name = "room_img")
    private String roomImg;

    @Column(name = "price_one_night",length = 150,nullable = false)
    @NotNull(message = "Khong duoc de trong !")
    private Double priceOneNight;

    @OneToMany(mappedBy = "room")
    private List<BookingDetail> bookingDetails;

    public Room() {
        this.ratings = new ArrayList<>();
    }
    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    @OrderBy("createdAt DESC")
    public List<Rating> getRatings() {return ratings;}


    public Room(Integer id, @NotEmpty(message = "Khong duoc de trong !") String title, @NotEmpty(message = "Khong duoc de trong !") String type, @NotEmpty(message = "Khong duoc de trong !") String description, @NotNull(message = "Khong duoc de trong !") Integer roomMax, @NotNull(message = "Khong duoc de trong !") Double roomSize, @NotNull(message = "Khong duoc de trong !") Integer roomBed, @NotNull(message = "Khong duoc de trong !") long availableRoom, @NotNull(message = "Khong duoc de trong !") String roomImg, @NotNull(message = "Khong duoc de trong !") Double priceOneNight) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.roomMax = roomMax;
        this.roomSize = roomSize;
        this.roomBed = roomBed;
        this.availableRoom = availableRoom;
        this.roomImg = roomImg;
        this.priceOneNight = priceOneNight;
    }

    public List<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoomMax() {
        return roomMax;
    }

    public void setRoomMax(Integer roomMax) {
        this.roomMax = roomMax;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public Integer getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(Integer roomBed) {
        this.roomBed = roomBed;
    }

    public long getAvailableRoom() {
        return availableRoom;
    }

    public void setAvailableRoom(long availableRoom) {
        this.availableRoom = availableRoom;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }

    public Double getPriceOneNight() {
        return priceOneNight;
    }

    public void setPriceOneNight(Double priceOneNight) {
        this.priceOneNight = priceOneNight;
    }
    public void addRating(Rating rating) {
        ratings.add(rating);
    }
}
