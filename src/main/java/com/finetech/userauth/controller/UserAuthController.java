package com.finetech.userauth.controller;

import com.finetech.userauth.controller.domain.Credentials;
import com.finetech.userauth.service.UserService;
import com.finetech.userauth.service.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@CommonsLog
@Controller
public class UserAuthController {
    
    private UserService service = new UserServiceImpl();

    @RequestMapping("/signup")
    public String getUserToSignup(Model model) {
        log.info("Signup service hit.");
        model.addAttribute("credentials", new Credentials());
        return "signup";
    }

    @RequestMapping("/register")
    public String register(Model model, @ModelAttribute(value = "credentials") Credentials credentials) {
        log.info("register service hit with params : credentials : " + credentials);
        boolean addUserSuccessful = service.addUserService(credentials);
        if (addUserSuccessful) {
            model.addAttribute("msg", "You have successfully signed up. You can login with your credentials now");
            return "home";
        } else {
            model.addAttribute("msg", "Your registration is not successfull. Try again.");
            return "signup";
        }
    }

    @RequestMapping("/home")
    public String getUserToHome(Model model, @ModelAttribute(value = "credentials")
            Credentials credentials, HttpServletRequest request, HttpSession session) {
        // invalidating current and starting fresh
        log.info("home service is hit with params : credentials : " + credentials);
        boolean userAunthenticated = service.authenticateUser(credentials);
        log.info("user authenticated : " + userAunthenticated);
        if (credentials.getUserName() == null ? false : !userAunthenticated) {
            log.info("Session has expired or unauthenticated user. Returning to home to login again.");
            model.addAttribute("msg", "invalid login");
            model.addAttribute("credentials", new Credentials());
            return "home";
        } else  {
            session.setAttribute("name", credentials.getUserName());
            model.addAttribute("name", credentials.getUserName());
            log.info("Sending user to user-details page.");
            return "user_details";
        }
    }

    @RequestMapping("/user_details")
    public String displayUserDetails(Model model, HttpSession session) {
        log.info("user-details service is hit.");
        Object nameOb = session.getAttribute("name");
        if (nameOb == null) {
            log.info("Session does not have required attributes. Sending user to home to login again.");
            model.addAttribute("credentials", new Credentials());
            return "home";
        }
        String name = (String) nameOb;
        log.info("User authenticated. Displaying details for : " + name);
        model.addAttribute("name", name);
        return "user_details";
    }

    @RequestMapping("/signout")
    public String signUserOut(Model model, HttpSession session) {
        log.info("signout service is hit.");
        session.invalidate();
        log.info("session invaliedated. Signing user out and sending to home.");
        model.addAttribute("msg", "You have signed out");
        Credentials credentials = new Credentials();
        model.addAttribute("credentials", credentials);
        return "home";
    }
}
