package com.project.demo.controller;

import com.project.demo.config.PropertiesConfig;
import com.project.demo.model.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DiscountController {
    @Autowired
    private PropertiesConfig configuration;

    @GetMapping("/demo")
    public Discount getDiscount(){
        return new
                Discount(configuration.getMonth(),configuration.getYear());
    }
}
