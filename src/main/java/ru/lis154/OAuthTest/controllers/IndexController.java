package ru.lis154.OAuthTest.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allUser() {
        return "index.html";
    }

}
