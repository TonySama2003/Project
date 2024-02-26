package vn.myhome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.myhome.dto.DataBookingCancelDTO;
import vn.myhome.dto.DataForPieChartDTO;
import vn.myhome.dto.RevenueDataDTO;
import vn.myhome.dto.UserDto;
import vn.myhome.entity.*;
import vn.myhome.service.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceHotelService serviceHotelService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingDetailService bookingDetailService;
    @Autowired
    private EmailService emailService;
    @GetMapping(value = {"/index",""})
    public String adminIndex(Model model){
        List<RevenueDataDTO> revenueDataDTOList = bookingDetailService.getRevenue();
        List<DataBookingCancelDTO> dataBookingCancelDTOList = bookingDetailService.getDataBkCancel();
        //add totalpriceBKCancel to revenueDataDTOList
        for(RevenueDataDTO data1 : revenueDataDTOList){
            for (DataBookingCancelDTO data2 : dataBookingCancelDTOList){
                if (data1.getYear()==data2.getYear() && data1.getMonth()==data2.getMonth()){
                    data1.setTotalPriceBkCancelMonth(data2.getTotalPriceBkCancelMonth()/10);
                }
            }
        }

        //lay ngay hien tai
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH)+1;
        //lay du lieu cho doanh thu theo thang
        RevenueDataDTO revenueDataDTONew = new RevenueDataDTO();
        double totalPriceYear = 0;
        for (RevenueDataDTO revenueDataDTO : revenueDataDTOList){
            if (revenueDataDTO.getYear() == yearNow ){
                totalPriceYear += revenueDataDTO.getTotalPriceMonth()+revenueDataDTO.getTotalPriceBkCancelMonth();
                if(revenueDataDTO.getMonth() == monthNow){
                    revenueDataDTONew = revenueDataDTO;
                }

            }
        }
        //booking cancel

        double totalPriceBkCancel = 0;

        DataBookingCancelDTO dataBookingCancelDTO = new DataBookingCancelDTO();
        for (DataBookingCancelDTO dataBookingCancelDTO1 : dataBookingCancelDTOList){
            if (dataBookingCancelDTO1.getYear() == yearNow ){
                totalPriceBkCancel += (dataBookingCancelDTO1.getTotalPriceBkCancelMonth()/10);
                if(dataBookingCancelDTO1.getMonth() == monthNow){
                    dataBookingCancelDTO = dataBookingCancelDTO1;
                    dataBookingCancelDTO.setTotalPriceBkCancelMonth(dataBookingCancelDTO1.getTotalPriceBkCancelMonth()/10);
                }
            }
        }
        if (dataBookingCancelDTO.getYear()==0){
            dataBookingCancelDTO.setYear(yearNow);
            dataBookingCancelDTO.setMonth(monthNow);
        }




        // lay du lieu cho chart
        // Tạo danh sách tháng và doanh thu từ dữ liệu
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        for(int i = 1 ; i <=12 ;i++){
            labels.add( i+"/"+yearNow);
            data.add(0.0);
            for (RevenueDataDTO dto : revenueDataDTOList){
                if (labels.get(i-1).equals(dto.getMonth()+"/"+yearNow) && dto.getYear() == yearNow){
                    data.set(i-1, dto.getTotalPriceMonth()+ dto.getTotalPriceBkCancelMonth());
                }
            }
        }
        //tỷ lệ completed / cancelled
        List<RevenueDataDTO> dataCompletedAndCancelleds = bookingDetailService.getNumCompletedAndCancelled();
        RevenueDataDTO dataCompletedAndCancelled = new RevenueDataDTO();

        for (RevenueDataDTO revenueDataDTO : dataCompletedAndCancelleds){
            if (revenueDataDTO.getYear()==yearNow && revenueDataDTO.getMonth() == monthNow){
                dataCompletedAndCancelled = revenueDataDTO;
                break;
            }else {
                dataCompletedAndCancelled.setYear(yearNow);
                dataCompletedAndCancelled.setMonth(monthNow);
                dataCompletedAndCancelled.setNumCancelled(0);
                dataCompletedAndCancelled.setNumCompleted(0);
                dataCompletedAndCancelled.setSumCompletedAndCancelled(0);
            }

        }
        long numCompleted = dataCompletedAndCancelled.getNumCompleted();
        long sumComlatedAndCancelled =dataCompletedAndCancelled.getSumCompletedAndCancelled();
        long ratioCompletedAndCancelled = 0 ;
        if (sumComlatedAndCancelled != 0){
            ratioCompletedAndCancelled = (long) ((numCompleted*100) / sumComlatedAndCancelled);
        }
        //tỷ lệ đã thanh toán , chưa thanh toán , đã hủy
        List<DataForPieChartDTO> dataForPieChartDTOList = bookingDetailService.getDataForPieChart();
        DataForPieChartDTO dataForPieChartDTO = new DataForPieChartDTO();

        for (DataForPieChartDTO dataDTO : dataForPieChartDTOList){
            if (dataDTO.getYear() == yearNow && dataDTO.getMonth() == monthNow){
                dataForPieChartDTO = dataDTO;
                break;
            }else {
                dataForPieChartDTO.setYear(yearNow);
                dataForPieChartDTO.setMonth(monthNow);
                dataForPieChartDTO.setNumPaided(0);
                dataForPieChartDTO.setNumUnPaid(0);
                dataForPieChartDTO.setNumCancelled(0);
            }
        }

        System.out.println(labels);
        //lay so booking ở trạng thái chờ xác nhận
        Integer numPendingBookingdetails = bookingDetailService.getPendingBookingdetails().size();
        model.addAttribute("dataForPieChart",dataForPieChartDTO);
        model.addAttribute("monthRate",dataCompletedAndCancelled.getMonth());
        model.addAttribute("ratioCompletedAndCancelled",ratioCompletedAndCancelled);
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);
        model.addAttribute("numPendingBooking",numPendingBookingdetails);
        model.addAttribute("totalPriceYear",totalPriceYear);
        model.addAttribute("totalPriceBkCancel",totalPriceBkCancel);

        model.addAttribute("revenueData",revenueDataDTONew);
        model.addAttribute("dataBookingCancelDTO",dataBookingCancelDTO);
        model.addAttribute("view","admin/admin-index");
        return "admin/admin-layout";
    }
    //test
    @GetMapping("/test")
    public String test(){
        bookingDetailService.getNumCompletedAndCancelled();
        return "/admin";
    }

    //////ROOM//////
    //room List
    @GetMapping(value = {"/rooms"})
    public String adminShowRooms(Model model ,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo){
        try{
            Page<Room> roomList = roomService.findAllPage(pageNo);
            if (roomList != null){
                model.addAttribute("totalPage",roomList.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("rooms",roomList);
                model.addAttribute("view","admin/admin-rooms");
                return "admin/admin-layout";
            }
        }catch (Exception e){

        }

        model.addAttribute("view","admin/admin-rooms");
        return "admin/admin-layout";
    }
    ///ADD ROOM///
    //ShowFormAdd
    @GetMapping(value = "/showFormAdd")
    public String showFormAdd(Model model,@ModelAttribute("successMessage") String successMessage){
        model.addAttribute("view","admin/admin-rooms-showFormAdd");
        Room newRoom = new Room();
        model.addAttribute("room",newRoom);
        model.addAttribute("successMessage", successMessage);
        return "admin/admin-layout";
    }
    //ADD new room
    @PostMapping(value = "/addRoom")
    public String addRoom(@ModelAttribute("room") @Valid Room room, BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes){

        // Kiểm tra và xử lý dữ liệu đăng ký ở đây
        if (bindingResult.hasErrors()) {
            model.addAttribute("view","admin/admin-rooms-showFormAdd");
            return "admin/admin-layout"; // Nếu có lỗi, hiển thị lại form đăng ký với thông báo lỗi
        }
        try{
            Room roomCheck = roomService.findByTitle(room.getTitle());
            if (roomCheck != null){
                model.addAttribute("view","admin/admin-rooms-showFormAdd");
                bindingResult.rejectValue("title","error.room","Room Opcuppied");
                return "admin/admin-layout";
            }
            if (!imageFile.isEmpty()) {
                try {
                    addImg(room,imageFile);
                    roomService.save(room);

                    redirectAttributes.addFlashAttribute("successMessage", "Room Added !");
                    return "redirect:/admin/rooms";
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

            roomService.save(room);
        }catch (Exception e){

        }

        model.addAttribute("view","admin/admin-rooms");
        return "admin/admin-layout";
    }
    //Update Room //
    @GetMapping("/showFormUpdate")
    public String showFormUpdateRoom(@RequestParam("roomId") int theId, Model model){
        try {
            Room theRoom = roomService.findById(theId);
            model.addAttribute("room",theRoom);
            model.addAttribute("view","admin/admin-rooms-showFormUpdate");
            return "admin/admin-layout";
        }catch (Exception e){

        }
        model.addAttribute("view","admin/admin-rooms-showFormUpdate");
        return "admin/admin-layout";
    }
    @PostMapping("/updateRoom")
    public String updateRoom(@ModelAttribute("room") Room theRoom, @RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes){
        try {
            if (!imageFile.isEmpty()){
                addImg(theRoom,imageFile);
                roomService.save(theRoom);
                redirectAttributes.addFlashAttribute("successMessage", "Room Updated!");
                return "redirect:/admin/rooms";
            }


        }catch (Exception e){

        }
        Room room = roomService.findById(theRoom.getId());
        theRoom.setRoomImg(room.getRoomImg());
        roomService.save(theRoom);
        redirectAttributes.addFlashAttribute("successMessage", "Room Updated!");
        return "redirect:/admin/rooms";
    }
    //delete room
    @GetMapping("/delete")
    public String deleteRoom(@RequestParam("roomId") int theId, RedirectAttributes redirectAttributes){
        roomService.deleteById(theId);
        redirectAttributes.addFlashAttribute("successMessage", "Room Deleted!");
        return "redirect:/admin/rooms";
    }



    public Room addImg(Room theRoom,MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = theRoom.getTitle().replaceAll("\\s+","_")+ UUID.randomUUID()+fileExtension;
        String staticDirectory = "src/main/resources/static";
        String imageDirectory = "/images/roomImg/";
        Path imageFilePath = Paths.get(staticDirectory + imageDirectory + fileName);
        Files.copy(imageFile.getInputStream(),imageFilePath);
        theRoom.setRoomImg(fileName);
        return theRoom;
    }
    //search room//
    @RequestMapping ("/searchRoom")
    public String searchRoomByTitleOrType(Model model,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,@RequestParam("searchText") String searchText){
        try {
            Page<Room> roomList = roomService.findRoomByTitleOrType(searchText,pageNo);
            if (!roomList.isEmpty()){
                model.addAttribute("totalPage",roomList.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("searchText",searchText);
                model.addAttribute("rooms",roomList);
            }else {
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("successMessage", "Khong tim thay phong !");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

            model.addAttribute("view","admin/admin-rooms");
            return "admin/admin-layout";
        }

    }

    ////USER/////
    //List User//

    @GetMapping("/users")
    public String listUser(Model model,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo){
        try{
            Page<AppUser> appUsers = userService.findAllPage(pageNo);
            model.addAttribute("totalPage",appUsers.getTotalPages());
            model.addAttribute("currentPage",pageNo);
            model.addAttribute("users", appUsers);
            model.addAttribute("view","admin/admin-users");
            return "admin/admin-layout";
        }catch (Exception e){

        }
        model.addAttribute("view","admin/admin-users");
        return "admin/admin-layout";
    }
    //Update User//
    @GetMapping("/showFormUpdateUser")
    public String showFormUpdateUser(@RequestParam("userId") int theId,Model model){
        try{
            AppUser appUser = userService.findById(theId);
            List<UserRole> userRoles = userRoleService.findByAppUser(appUser);

            model.addAttribute("userRoles",userRoles);
            model.addAttribute("view","admin/admin-user-showFormUpdate");
            return "admin/admin-layout";
        }catch (Exception ex){

        }
        model.addAttribute("view","admin/admin-user-showFormUpdate");
        return "admin/admin-layout";
    }
    @PostMapping("/updateRoleUser")
    public String updateRoles(@RequestParam("userRoleId") List<Integer> userRoleIds, @RequestParam("roleId") List<Long> roleIds, RedirectAttributes redirectAttributes) {
        try{
            UserRole userRoleUpdate = new UserRole();
            Role roleNew = new Role();
            for(int i = 0 ; i < userRoleIds.size();i++){
                System.out.println(userRoleIds.get(i));
                System.out.println(roleIds.get(i));
                System.out.println("------");
                userRoleUpdate = userRoleService.findById(userRoleIds.get(i));
                roleNew = roleService.findByRoleId(roleIds.get(i));
                userRoleUpdate.setRole(roleNew);
                userRoleService.save(userRoleUpdate);
                System.out.println("Cập nhật thành công "+ i);
            }
        }catch (Exception e){
                return "404Page";
        }

        redirectAttributes.addFlashAttribute("successMessage", "User Updated!");
        // Sau khi cập nhật xong, bạn có thể chuyển hướng đến một trang thành công hoặc làm gì đó khác
        return "redirect:/admin/users";
    }
    //delete User
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") int theId, RedirectAttributes redirectAttributes){
        try {
            AppUser appUser = userService.findById(theId);
            appUser.setEnabled(false);
            userService.save(appUser);
            redirectAttributes.addFlashAttribute("successMessage", "User Updated!");
            // Sau khi cập nhật xong, bạn có thể chuyển hướng đến một trang thành công hoặc làm gì đó khác
            return "redirect:/admin/users";
        }catch (Exception ex){

        }
        redirectAttributes.addFlashAttribute("successMessage", "Update Error!");
        // Sau khi cập nhật xong, bạn có thể chuyển hướng đến một trang thành công hoặc làm gì đó khác
        return "redirect:/admin/users";
    }
    //search User
    @RequestMapping("/searchUser")
    public String searchUser(Model model,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,@RequestParam("searchText") String searchText){
        try{
            Page<AppUser> appUsers = userService.searchUserByEmailOrPhone(searchText,pageNo);
            if (!appUsers.isEmpty()){
                model.addAttribute("totalPage",appUsers.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("searchText",searchText);
                model.addAttribute("users", appUsers);
            }else {
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("successMessage", "Khong tim thay User !");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            model.addAttribute("view","admin/admin-users");
            return "admin/admin-layout";
        }
    }
    ///BOOKING///
    //show list booking
    @GetMapping("/bookings")
    public String listBookings(Model model ,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo) {
        Page<Booking> bookings = bookingService.findAllPage(pageNo);

        model.addAttribute("totalPage",bookings.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("bookings", bookings);
        model.addAttribute("view","admin/admin-bookings");
        return "admin/admin-layout";

    }
    //Show List Booking Cancel
    @GetMapping("/bookingsCancel")
    public String listBookingsCancel(Model model ,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo) {
        try{
            Page<Booking> bookings = bookingService.findAllByBookingDetailStatus(pageNo);
            if (!bookings.isEmpty()){
                model.addAttribute("totalPage",bookings.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("bookings", bookings);
                model.addAttribute("view","admin/admin-bookingsCancel");
                return "admin/admin-layout";
            }else {
                model.addAttribute("view","admin/admin-bookingsCancel");
                model.addAttribute("successMessage", "Khong có booking hủy !");
                return "admin/admin-layout";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("view","admin/admin-bookingsCancel");
        return "admin/admin-layout";
    }
    //Show List Booking Pending
    @GetMapping("/bookingsPending")
    public String listBookingsPending(Model model ,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo) {
        try{
            Page<Booking> bookings = bookingService.findAllByBookingPending(pageNo);
            if (!bookings.isEmpty()){
                model.addAttribute("totalPage",bookings.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("bookings", bookings);
                model.addAttribute("view","admin/admin-bookingsPending");
                return "admin/admin-layout";
            }else {
                model.addAttribute("view","admin/admin-bookingsPending");
                model.addAttribute("successMessage", "Khong có booking chờ xác nhận !");
                return "admin/admin-layout";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("view","admin/admin-bookingsPending");
        return "admin/admin-layout";
    }
    //bookingdetail
    @GetMapping("/bookings/{bookingId}")
    public String viewBooking(@PathVariable("bookingId") int bookingId, Model model) {
        Booking booking = bookingService.findById(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("view","admin/admin-bookingDetail");
        return "admin/admin-layout";
    }
    //update BookingDetail
    @PostMapping("/bookingdetails/updateStatus")
    public String updateBookingDetailStatus(@RequestParam("bookingDetailId") int bookingDetailId,
                                            @RequestParam("status") String status, RedirectAttributes redirectAttributes) throws MessagingException {
        // Sử dụng BookingDetailService để cập nhật trạng thái BookingDetail
        bookingDetailService.updateStatus(bookingDetailId, status);
        BookingDetail bookingDetail = bookingDetailService.findById(bookingDetailId);
        int bookingId =bookingDetail.getBooking().getId();
        if (status.equals("Confirmed") || status.equals("Cancel Booking")){
            String bookingCode = bookingDetail.getBooking().getBookingCode();
            Date checkin = bookingDetail.getCheckinDate();
            Date checkout = bookingDetail.getCheckoutDate();
            String titleRoom = bookingDetail.getRoom().getTitle();
            String nameUser = bookingDetail.getBooking().getAppUser().getFirstName()+" "+bookingDetail.getBooking().getAppUser().getLastName();
            double totalPrice = bookingDetail.getTotalPrice();
            String userEmail = bookingDetail.getBooking().getAppUser().getEmail();
            emailService.sendEmail(userEmail,status,bookingCode,checkin,checkout,titleRoom,nameUser,totalPrice);
        }
        // Redirect hoặc trả về trang `bookingdetail` hoặc trang khác
        redirectAttributes.addFlashAttribute("successMessage", "Booking Status Updated!");
        // Sau khi cập nhật xong, bạn có thể chuyển hướng đến một trang thành công hoặc làm gì đó khác
        return "redirect:/admin/bookings/"+ bookingId;
    }
    //search Booking
    @RequestMapping("/searchBooking")
    public String searchBooking(Model model,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,@RequestParam("searchText") String searchText){
        try{
            Page<Booking> bookings = bookingService.findAllBookingByNamePhoneBkCode(searchText,pageNo);
            if (!bookings.isEmpty()){
                model.addAttribute("totalPage",bookings.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("searchText",searchText);
                model.addAttribute("bookings", bookings);
            }else {
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("successMessage", "Khong tim thay booking !");
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            model.addAttribute("view","admin/admin-bookings");
            return "admin/admin-layout";
        }
    }
    /////Service/////
    //list service
    @GetMapping("/services")
    public String showService(Model model,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo){
        try{
            Page<ServiceHotel> serviceHotelList = serviceHotelService.findAllPage(pageNo) ;
            if (!serviceHotelList.isEmpty()){
                model.addAttribute("totalPage",serviceHotelList.getTotalPages());
                model.addAttribute("servers",serviceHotelList);
                model.addAttribute("currentPage",pageNo);
            }else {
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("successMessage", "Khong co Service !");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

            model.addAttribute("view","admin/admin-services");
            return "admin/admin-layout";
        }
    }
    //add service

    @GetMapping("/showFormAddService")
    public String showFormAddService(Model model){
        ServiceHotel serviceHotel = new ServiceHotel();
        model.addAttribute("service",serviceHotel);
        model.addAttribute("view","admin/admin-services-formAdd");
        return "admin/admin-layout";
    }
    //ADD new Service
    @PostMapping(value = "/addService")
    public String addService(@ModelAttribute("service") @Valid ServiceHotel serviceHotel, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){

        // Kiểm tra và xử lý dữ liệu đăng ký ở đây
        if (bindingResult.hasErrors()) {
            model.addAttribute("view","admin/admin-services-formAdd");
            return "admin/admin-layout"; // Nếu có lỗi, hiển thị lại form đăng ký với thông báo lỗi
        }
        try{

            ServiceHotel serviceCheck = serviceHotelService.findByServiceName(serviceHotel.getServiceName());
            if (serviceCheck != null){
                model.addAttribute("view","admin/admin-services-formAdd");
                bindingResult.rejectValue("serviceName","error.serviceHotel","Service Opcuppied");
                return "admin/admin-layout";
            }

            serviceHotelService.save(serviceHotel);
            redirectAttributes.addFlashAttribute("successMessage", "Service Added !");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage", "Service False !");
        }
        return "redirect:/admin/services";

    }
    //Update Service //
    @GetMapping("/showFormUpdateService")
    public String showFormUpdateService(@RequestParam("serviceId") int theId, Model model){
        try {
            ServiceHotel serviceHotel = serviceHotelService.findById(theId);
            model.addAttribute("service",serviceHotel);
            model.addAttribute("view","admin/admin-services-showFormUpdate");
            return "admin/admin-layout";
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("view","admin/admin-services-showFormUpdate");
        return "admin/admin-layout";
    }
    @PostMapping("/updateService")
    public String updateService(@ModelAttribute("service") ServiceHotel theServiceHotel, RedirectAttributes redirectAttributes){
        try {
            serviceHotelService.save(theServiceHotel);
            redirectAttributes.addFlashAttribute("successMessage", "Service Updated!");
            return "redirect:/admin/services";


        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage", "Service Update False!");
        }
        return "redirect:/admin/services";
    }
    //delete room
    @GetMapping("/deleteService")
    public String deleteService(@RequestParam("serviceId") int theId, RedirectAttributes redirectAttributes){
        serviceHotelService.deleteById(theId);
        redirectAttributes.addFlashAttribute("successMessage", "Service Deleted!");
        return "redirect:/admin/services";
    }
}
