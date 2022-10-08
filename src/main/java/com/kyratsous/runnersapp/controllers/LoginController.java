package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"/login", "/login.html"})
    public String getLoginPage(@RequestParam(value = "error", defaultValue = "false") boolean loginError,
                               @RequestParam(value = "invalid-session", defaultValue = "false") boolean invalidSession,
                               @RequestParam(value = "logout", defaultValue = "false") boolean logout,
                               Model model, HttpServletResponse response, HttpServletRequest request) {

        if (loginError)
            model.addAttribute("message", "Wrong username or password");

        if (invalidSession) {
            Cookie cookie = new Cookie("VIEW-AS", "GUEST");
            response.addCookie(cookie);
            model.addAttribute("message", "Session expired, please re-login.");
        }

        if (logout) {
            Cookie cookie = new Cookie("VIEW-AS", "GUEST");
            response.addCookie(cookie);
            model.addAttribute("messageLogout", "You are successfully logged out.");
        }

        if (request != null && response != null) {
            String referer = request.getHeader("Referer");

            if (referer != null && !referer.contains("login") && !referer.contains("signup")) {
                Cookie cookie = new Cookie("PREVIOUS-PAGE", referer);
                response.addCookie(cookie);
            }
        }

        return "user/login";
    }

    @RequestMapping("/create-cookies")
    public String createCookies(@CookieValue(value = "PREVIOUS-PAGE", defaultValue = "/") String url,
                                HttpServletResponse response) {
        User user = userService.getCurrentUser();

        Cookie cookie = new Cookie("VIEW-AS", user.getAuthorities().contains("ATHLETE")? "ATHLETE": user.getAuthorities().stream().findFirst().get());
        response.addCookie(cookie);

        return "redirect:" + url;
    }

}
