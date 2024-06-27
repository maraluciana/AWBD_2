package com.project.demo.config;

import com.project.demo.model.Discount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "demo", url = "localhost:8080")
public interface DiscountServiceProxy {
    @GetMapping("/demo")
    Discount findDiscount();
}
