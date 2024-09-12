package com.spring.security.security.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    public Cookie criarCookie(String jwt){
        Cookie cookie = new Cookie("USERTOKEN", jwt);

        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");

        return cookie;

    }

    public Cookie removerCookie(){
        Cookie cookie = new Cookie("USERTOKEN", "");

        cookie.setMaxAge(1);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");

        return cookie;
    }

}
