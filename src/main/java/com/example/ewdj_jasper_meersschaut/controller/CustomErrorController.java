package com.example.ewdj_jasper_meersschaut.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Vangt de errors op en toont de juiste foutpagina's aan de gebruiker.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "400";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403";
            }
        }
        return "error";
    }
}