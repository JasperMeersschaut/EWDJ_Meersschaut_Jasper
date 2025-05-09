package com.example.ewdj_jasper_meersschaut;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ModelAttribute("username")
    public String populateColors(Authentication authentication) {
        return authentication == null?"": authentication.getName();
    }
    
}