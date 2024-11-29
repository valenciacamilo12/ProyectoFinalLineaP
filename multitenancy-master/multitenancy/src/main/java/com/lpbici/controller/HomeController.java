package com.lpbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    public String login1(){
        return "login1";
    }

    @RequestMapping(value = "/modulo1", method = RequestMethod.GET)
    public String modulo1(){
        return "modulo1";
    }

    @RequestMapping(value = "/forbidden", method = RequestMethod.GET)
    public String forbidden(){
        return "forbidden";
    }
}
