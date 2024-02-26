package vn.myhome.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "servicehotel")
public class ServiceHotel {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "service_name")
    @NotEmpty(message = "Khong duoc de trong !")
    private String serviceName;


    @Column(name = "service_price")
    @NotNull(message = "Khong duoc de trong !")
    private double servicePrice;

    @OneToMany(mappedBy = "serviceHotel")
    private List<BookingDetailServiceHotel> bookingDetailServices;
    public ServiceHotel() {
    }



    public List<BookingDetailServiceHotel> getBookingDetailServices() {
        return bookingDetailServices;
    }

    public void setBookingDetailServices(List<BookingDetailServiceHotel> bookingDetailServices) {
        this.bookingDetailServices = bookingDetailServices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }
}
