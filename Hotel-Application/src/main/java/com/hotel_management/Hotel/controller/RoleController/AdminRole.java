package com.hotel_management.Hotel.controller.RoleController;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AdminRole {

    /*
    "/analytics/**",
    "/staff/**",
    "/rooms/**",
    "/booking/**",
    "/payment/**",
    "/users/**",
    "/feedback/**"
    */

    @GetMapping("/analytics/test")
    public String analytics(){
        return "Analytics : url /analytics/test";
    }

    @GetMapping("/staff/list")
    public String staff(){
        return "Staff : url : /staff/list";
    }

    @GetMapping("/rooms/test")
    public String rooms(){
        return "Rooms : url : /rooms/list";
    }

    @GetMapping("/booking/list") //
    public String bookings(){
        return "Bookings : url : /bookings/list";
    }

    @GetMapping("/payment/list")
    public String payment(){
        return "Payment : url : /payment/list";
    }

    @GetMapping("/users/list")
    public String user(){
        return "Users : url : /users/list";
    }

    @GetMapping("/feedback/list")
    public String feedback(){
        return "Feedback : url : /feedback/list";
    }


}
