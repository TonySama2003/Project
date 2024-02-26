package vn.myhome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.myhome.dto.BookingFormDTO;
import vn.myhome.dto.RoomAvailableDTO;
import vn.myhome.dto.ServiceForm;
import vn.myhome.entity.*;
import vn.myhome.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private ServiceHotelService serviceHotelService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingDetailService bookingDetailService;
    @Autowired
    private BookingDetailServiceHotelService bookingDetailServiceHotelService;

    @RequestMapping(value = {"/home","/"})
    public String homePage(HttpSession session,Model model){
        List<Room> randomRoomList = roomService.findRandomRooms();
        model.addAttribute("randomRooms",randomRoomList);

        return "user/home";
    }



    @GetMapping(value = {"/restaurant"})
    public String userRestaurant(Model model){
        model.addAttribute("view","user/user-restaurant");
        return "user/user-layout";
    }
    @GetMapping(value = {"/about"})
    public String userAbout(Model model){
        model.addAttribute("view","user/user-about");
        return "user/user-layout";
    }
    @GetMapping(value = {"/contact"})
    public String userContact(Model model){
        model.addAttribute("view","user/user-contact");
        return "user/user-layout";
    }
    @GetMapping(value = {"/blog"})
    public String userBlog(Model model){
        model.addAttribute("view","user/user-blog");
        return "user/user-layout";
    }
    @GetMapping(value = {"/blog-single"})
    public String userBlogSingle(Model model){
        model.addAttribute("view","user/user-blog-single");
        return "user/user-layout";
    }
    @GetMapping(value = {"/room-single"})
    public String userRoomSingle(Model model){
        model.addAttribute("view","user/user-room-single");
        return "user/user-layout";
    }
    ////ROOM/////
    //show listRoom
    @GetMapping(value = {"/rooms"})
    public String userRoom(Model model){
        try{
            List<Room> roomList = roomService.findAll();
            if (roomList != null){
                model.addAttribute("rooms",roomList);
                model.addAttribute("view","user/user-rooms");
                return "user/user-layout";
            }
        }catch (Exception e){

        }

        model.addAttribute("view","user/user-rooms");
        return "user/user-layout";
    }

    //get room by Id
    @RequestMapping("/roomSingle")
    public String getRoomById(@RequestParam(value = "roomId")int theId , Model model,HttpSession session){
        Room theRoom = roomService.findById(theId);
        session.setAttribute("selectedRoom",theRoom);
        model.addAttribute("room",theRoom);
        model.addAttribute("view","user/user-room-single");
        return "user/user-layout";
    }
    @RequestMapping("/roomSingleAvailable")
    public String getRoomByIdAvailable(@RequestParam(value = "roomId")int theId ,@RequestParam(value = "roomsAvailable") int roomsAvailable, Model model,HttpSession session){
        Room theRoom = roomService.findById(theId);
        session.setAttribute("selectedRoom",theRoom);
        model.addAttribute("roomsAvailable",roomsAvailable);
        model.addAttribute("room",theRoom);
        model.addAttribute("view","user/user-room-single-checkAvailable");
        return "user/user-layout";
    }
    ///BOOKING ///

    //check available

    @PostMapping("/checkAvailable")
    public String checkAvailableRoom(@RequestParam("checkinDate")String checkinDate, @RequestParam("checkoutDate")String checkoutDate, Model model , HttpSession session){


        try{
            if(formatDate(checkoutDate).getTime() - formatDate(checkinDate).getTime()>0 && formatDate(checkoutDate).getTime() > new Date().getTime() && (formatDate(checkinDate).getTime() +(24 * 3600000)) >= new Date().getTime()){
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                // Chuyển đổi checkinDate và checkoutDate từ String sang kiểu Date
                Date checkinDateNew = sdf.parse(checkinDate);
                Date checkoutDateNew = sdf.parse(checkoutDate);

                //lưu session checkin checkout dưới kiểu date
                session.setAttribute("checkinDate", checkinDateNew);
                session.setAttribute("checkoutDate", checkoutDateNew);
                //lấy ra các phòng có thể đặt
                List<RoomAvailableDTO> roomAvailableDTOS = roomService.findAvailableRooms1(checkinDateNew,checkoutDateNew);
                if (roomAvailableDTOS != null){
                    String messages = roomAvailableDTOS.size()+" Room Available ";

                    model.addAttribute("messages",messages);
                    model.addAttribute("rooms",roomAvailableDTOS);
                    model.addAttribute("view","user/user-roomsAvailable");
                    return "user/user-layout";
                }else {
                    model.addAttribute("messages","Room Unavailable ");
                    model.addAttribute("view","user/user-roomsAvailable");
                    return "user/user-layout";
                }
            }model.addAttribute("error","Date Unavailable!");
            return "/user/home";



        } catch (ParseException e) {
            e.printStackTrace();

            model.addAttribute("error","Please Enter Date to Check in & Check out !");
            return "/user/home";
        }



    }
    @PostMapping("/checkAvailableByNoRoom")
    public String checkAvailableRoomByNoRoom(@RequestParam("checkinDate")String checkinDate, @RequestParam("checkoutDate")String checkoutDate,@RequestParam("roomQuantity") Integer roomQuantity, Model model , HttpSession session){


        try{
            if(formatDate(checkoutDate).getTime() - formatDate(checkinDate).getTime()>0 && formatDate(checkoutDate).getTime() > new Date().getTime() && (formatDate(checkinDate).getTime() +(24 * 3600000)) >= new Date().getTime()){
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                // Chuyển đổi checkinDate và checkoutDate từ String sang kiểu Date
                Date checkinDateNew = sdf.parse(checkinDate);
                Date checkoutDateNew = sdf.parse(checkoutDate);

                //lưu session checkin checkout dưới kiểu date
                session.setAttribute("checkinDate", checkinDateNew);
                session.setAttribute("checkoutDate", checkoutDateNew);
                //lấy ra các phòng có thể đặt
                List<RoomAvailableDTO> roomAvailableDTOS = roomService.findRoomsAvailableByNoRoom(checkinDateNew,checkoutDateNew,roomQuantity);
                if (roomAvailableDTOS != null){
                    String messages = roomAvailableDTOS.size()+" Room Available ";

                    model.addAttribute("messages",messages);
                    model.addAttribute("rooms",roomAvailableDTOS);
                    model.addAttribute("view","user/user-roomsAvailable");
                    return "user/user-layout";
                }else {
                    model.addAttribute("messages","Room Unavailable ");
                    model.addAttribute("view","user/user-roomsAvailable");
                    return "user/user-layout";
                }
            }model.addAttribute("error","Date Unavailable!");
            return "/user/home";



        } catch (ParseException e) {
            e.printStackTrace();

            model.addAttribute("error","Please Enter Date to Check in & Check out !");
            return "/user/home";
        }



    }
    @RequestMapping("/checkAvailableByRoom")
    public String checkAvailableRoomByRoom(@RequestParam("roomId") int roomId,@RequestParam("checkinDate")String checkinDate, @RequestParam("checkoutDate")String checkoutDate,@RequestParam("roomQuantity") Integer roomQuantity, Model model , HttpSession session){


        try{
            if(formatDate(checkoutDate).getTime() - formatDate(checkinDate).getTime()>0 && formatDate(checkoutDate).getTime() > new Date().getTime() && (formatDate(checkinDate).getTime() +(24 * 3600000)) >= new Date().getTime()){
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                // Chuyển đổi checkinDate và checkoutDate từ String sang kiểu Date
                Date checkinDateNew = sdf.parse(checkinDate);
                Date checkoutDateNew = sdf.parse(checkoutDate);

                //lưu session checkin checkout dưới kiểu date
                session.setAttribute("checkinDate", checkinDateNew);
                session.setAttribute("checkoutDate", checkoutDateNew);
                // lấy room
                Room roomOrder = roomService.findById(roomId);
                //tìm kiếm room hợp lệ
                RoomAvailableDTO roomAvailableDTO = roomService.searchRoomAvailable(checkinDateNew,checkoutDateNew,roomQuantity,roomOrder);

                if (roomAvailableDTO != null){
                    String messages = roomAvailableDTO.getRoomsAvailable()+" Room Available ";

                    model.addAttribute("messages",messages);
                    session.setAttribute("selectedRoom",roomOrder);
                    model.addAttribute("roomsAvailable",roomAvailableDTO.getRoomsAvailable());
                    model.addAttribute("room",roomOrder);
                    model.addAttribute("view","user/user-room-single-checkAvailable");
                    return "user/user-layout";
                }else {
                    //lấy ra các phòng có thể đặt
                    List<RoomAvailableDTO> roomAvailableDTOS = roomService.findRoomsAvailableByNoRoom(checkinDateNew,checkoutDateNew,roomQuantity);
                    model.addAttribute("messages","Phòng đã hết , Các phòng có thể đặt : ");
                    model.addAttribute("rooms",roomAvailableDTOS);
                    model.addAttribute("view","user/user-roomsAvailable");
                    return "user/user-layout";
                }
            }model.addAttribute("error","Date Unavailable!");
            return "/user/home";



        } catch (ParseException e) {
            e.printStackTrace();

            model.addAttribute("error","Please Enter Date to Check in & Check out !");
            return "/user/home";
        }



    }
    private Date formatDate(String date) throws ParseException {
        // Thực hiện kiểm tra ngày và trả về true nếu hợp lệ, false nếu không hợp lệ.
        // Ví dụ đơn giản ở đây: kiểm tra xem chuỗi ngày có đúng định dạng không.
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // Chuyển đổi checkinDate và checkoutDate từ String sang kiểu Date
        Date dateformat = sdf.parse(date);
        return dateformat;
    }
    //Booking
    @PostMapping("/bookNow")
    public String bookNow(@RequestParam("roomId") int roomId,
                          @RequestParam("checkinDate") String checkinDate,
                          @RequestParam("checkoutDate") String checkoutDate,

                          @RequestParam("numberGuest") int numberGuest,
            @RequestParam("roomQuantity") int roomQuantity,HttpSession session,Model model){

        //Lưu session các thông tin quan trọng
        BookingFormDTO bookingFormDTO = new BookingFormDTO(roomId,checkinDate,checkoutDate,numberGuest,roomQuantity);
        session.setAttribute("bookingFormDTO",bookingFormDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            session.setAttribute("bookingFormDTO",bookingFormDTO);
            // Người dùng chưa đăng nhập, đưa họ đến trang đăng nhập
            return "redirect:/login";
        }

        return "redirect:/checkoutForm";
    }
    @GetMapping("/checkoutForm")
    public String checkoutForm(Model model, Principal principal, HttpSession session){
        try{
            BookingFormDTO bookingFormDTO = (BookingFormDTO) session.getAttribute("bookingFormDTO");
            //lấy thông tin phòng
            Room room = roomService.findById(bookingFormDTO.getRoomId());
            // Tính số ngày đặt phòng
            long diffInMilliseconds = formatDate(bookingFormDTO.getCheckoutDate()).getTime() - formatDate(bookingFormDTO.getCheckinDate()).getTime();
            long diffInDays ;
            if (diffInMilliseconds == 0){
                diffInDays =1 ;
            }else {
                diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            }
            //tính tiền theo phòng
            double totalPriceRoom = diffInDays * room.getPriceOneNight()*bookingFormDTO.getRoomQuantity();

            // Sau khi user login thanh cong se co principal
            String userName = principal.getName();
            AppUser appUser = userService.findByEmail(userName);
            model.addAttribute("serviceForm",new ServiceForm());
            model.addAttribute("booking",bookingFormDTO);
            model.addAttribute("room",room);
            model.addAttribute("user", appUser);
            model.addAttribute("totalPriceRoom",totalPriceRoom);
            List<ServiceHotel> serviceHotelList = serviceHotelService.findAll();
            model.addAttribute("services", serviceHotelList);
            model.addAttribute("noDate",diffInDays);
            model.addAttribute("view","user/user-bookingForm");
            return "user/user-layout";
        }catch (Exception exception){
            exception.printStackTrace();

            String userName = principal.getName();
            AppUser appUser = userService.findByEmail(userName);
            List<UserRole> userRoleList = userRoleService.findByAppUser(appUser);

            for (int i = 0 ; i < userRoleList.size();i++){
                if(userRoleList.get(i).role.getRoleName().equals("ROLE_ADMIN")){
                    return "redirect:/admin/index";
                }

            }

            // Nếu không phải admin, chuyển đến trang home
            return "redirect:/home";

        }

    }
    @PostMapping("/checkout")
    public String checkout(ServiceForm serviceForm,@RequestParam("totalAllService")double noDate,
                           @RequestParam("totalPrice") double totalPrice, Model model, Principal principal, HttpSession session){
        try{
            // Lấy thông tin người dùng từ Principal
            String userName = principal.getName();
            AppUser appUser = userService.findByEmail(userName);
            // Tạo đối tượng Booking
            Booking booking = new Booking();
            booking.setBookingCode(generateBookingCode());
            booking.setAppUser(appUser);
            booking.setCreateDate(new Date());
            booking.setTotalPrice(totalPrice);

            // Lưu thông tin Booking vào cơ sở dữ liệu
            bookingService.save(booking);
            booking = bookingService.save(booking);
            //lấy thông tin booking từ session
            BookingFormDTO bookingFormDTO = (BookingFormDTO) session.getAttribute("bookingFormDTO");
            //lấy thông tin phòng
            Room room = roomService.findById(bookingFormDTO.getRoomId());
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setBooking(booking);
            bookingDetail.setCheckinDate(formatDate(bookingFormDTO.getCheckinDate()));
            bookingDetail.setCheckoutDate(formatDate(bookingFormDTO.getCheckoutDate()));
            bookingDetail.setTotalPrice(totalPrice);
            bookingDetail.setNumberGuest(bookingFormDTO.getNumberGuest());
            bookingDetail.setRoomQuantity(bookingFormDTO.getRoomQuantity());
            bookingDetail.setCreateDate(new Date());
            bookingDetail.setUpdateDate(new Date());
            bookingDetail.setRoom(room);
            bookingDetail.setStatus("Wait for confirmation");


            bookingDetail = bookingDetailService.save(bookingDetail);
            //lấy thông tin từ serviceForm
             List<Integer> selectedServices= serviceForm.getSelectedServices();
            List<Integer> serviceQuantities = serviceForm.getServiceQuantities();


//             List<Double> totalServicePrices=serviceForm.getTotalServicePrices();
            double totalAllService = 0;
            //duyệt thông tin và đưa vào bookingDetailServiceHotel
            for(int i = 0 ; i < selectedServices.size();i++){
                int serviceId = selectedServices.get(i);

                int quantity = serviceQuantities.get(i);
                double totalServicePrice ;
                BookingDetailServiceHotel bookingDetailServiceHotel = new BookingDetailServiceHotel();
                ServiceHotel serviceHotel = serviceHotelService.findById(serviceId);
                totalServicePrice = serviceHotel.getServicePrice() * quantity;
                totalAllService += totalServicePrice;
                bookingDetailServiceHotel.setBookingDetail(bookingDetail);
                bookingDetailServiceHotel.setServiceHotel(serviceHotel);
                bookingDetailServiceHotel.setQuantity(quantity);
                bookingDetailServiceHotel.setPrice(totalServicePrice);
                bookingDetailServiceHotelService.save(bookingDetailServiceHotel);
            }

            model.addAttribute("messages","Booking Succesfully ! ");
            model.addAttribute("totalAllService",totalAllService);
            model.addAttribute("booking",bookingFormDTO);
            model.addAttribute("noDate",noDate);
            model.addAttribute("room",room);
            model.addAttribute("totalPrice",totalPrice);

//            session.removeAttribute("bookingFormDTO");
            return "user/user-checkout";

        }catch (Exception ex){
            ex.printStackTrace();

            return "user/home";
        }
    }
    public String generateBookingCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String bookingCode = "BK" + timestamp;
        return bookingCode;
    }
    ///PROFILE//
    @GetMapping("/profile")
    public String getProfile(Model model,Principal principal){
        try {
            String userName = principal.getName();
            AppUser appUser = userService.findByEmail(userName);
            model.addAttribute("user",appUser);
            model.addAttribute("view","user/user-profile");
            return "user/user-layout";
        }catch (Exception ex){
            return "404Page";
        }
    }
    ///GetBookings
    @GetMapping("/showBookings")
    public String showBookings(Model model, Principal principal,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,@ModelAttribute("cancelMessage") String cancelMessage){
        try {
            String userName = principal.getName();
            AppUser appUser = userService.findByEmail(userName);

            Page<Booking> bookingList = bookingService.findAllByAppUserPage(appUser,pageNo);
            model.addAttribute("totalPage",bookingList.getTotalPages());
            model.addAttribute("currentPage",pageNo);
            model.addAttribute("bookings",bookingList);
            model.addAttribute("cancelMessage", cancelMessage);
            model.addAttribute("view","user/user-showbookings");
            return "user/user-layout";

        }catch (Exception ex){
            return "404Page";
        }
    }
    //bookingdetail
    @GetMapping("/bookingCancel/{bookingId}")
    public String cancelBooking(@PathVariable("bookingId") int bookingId, Model model, RedirectAttributes redirectAttributes) {
        Booking booking = bookingService.findById(bookingId);
        for (BookingDetail bookingdetail:
             booking.getBookingDetails()) {
            bookingDetailService.updateStatus(bookingdetail.getId(),"Cancel Booking Pending");
        }

        redirectAttributes.addFlashAttribute("cancelMessage", "Yêu cầu hủy đã được gửi.");
        // Sau khi cập nhật xong, bạn có thể chuyển hướng đến một trang thành công hoặc làm gì đó khác
        return "redirect:/showBookings";
    }
    // edit profile
    //showform
    @GetMapping("/showEditProfile")
    public String showFormEditProfile(@RequestParam("userId") int theId ,Model model){
        try {
            AppUser appUser = userService.findById(theId);
            model.addAttribute("user",appUser);
            model.addAttribute("view","user/user-edit-profile");
            return "user/user-layout";
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("view","user/user-profile");
        return "user/user-layout";
    }
    //edit profile
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("user") @Valid AppUser theUser, BindingResult bindingResult, @RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes, Model model){
        // Kiểm tra và xử lý dữ liệu đăng ký ở đây
        if (bindingResult.hasErrors()) {
            model.addAttribute("view","user/user-edit-profile");
            return "user/user-layout"; // Nếu có lỗi, hiển thị lại form đăng ký với thông báo lỗi
        }
        try {
            if (!imageFile.isEmpty()){
                addImg(theUser,imageFile);
                System.out.println(theUser);
                userService.updateProfile(theUser);
                redirectAttributes.addFlashAttribute("successMessage", "Room Updated!");
                return "redirect:/profile";
            }else {
                userService.updateProfile(theUser);
                redirectAttributes.addFlashAttribute("successMessage", "Room Updated!");
                return "redirect:/profile";
            }
        }catch (Exception e){
        e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("successMessage", "Room False!");
        return "redirect:/profile";
    }
    public void addImg(AppUser theUser,MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = theUser.getFirstName().replaceAll("\\s+","_")+ UUID.randomUUID()+fileExtension;
        String staticDirectory = "src/main/resources/static";
        String imageDirectory = "/images/userImg/";
        Path imageFilePath = Paths.get(staticDirectory + imageDirectory + fileName);
        Files.copy(imageFile.getInputStream(),imageFilePath);
        theUser.setUserImg(fileName);

    }
}
