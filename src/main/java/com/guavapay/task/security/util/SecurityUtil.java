package com.guavapay.task.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static String getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = null;
        if (authentication != null){
            if (authentication.getPrincipal() instanceof UserDetails){
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
            }else if(authentication.getPrincipal() instanceof String){
                username = (String) authentication.getPrincipal();
            }
        }
        return username;
    }
}
