package com.callahan.callahancodigofuente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorWebController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "redirect:403.html";
    }
}
