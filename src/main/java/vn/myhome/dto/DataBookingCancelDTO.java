package vn.myhome.dto;

public class DataBookingCancelDTO {
    private int year ;
    private int month;
    private double totalPriceBkCancelMonth;

    public DataBookingCancelDTO() {
    }

    public DataBookingCancelDTO(int year, int month, double totalPriceBkCancelMonth) {
        this.year = year;
        this.month = month;
        this.totalPriceBkCancelMonth = totalPriceBkCancelMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalPriceBkCancelMonth() {
        return totalPriceBkCancelMonth;
    }

    public void setTotalPriceBkCancelMonth(double totalPriceBkCancelMonth) {
        this.totalPriceBkCancelMonth = totalPriceBkCancelMonth;
    }
}
