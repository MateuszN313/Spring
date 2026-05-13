package org.example.carrent.web;

import org.example.carrent.models.VehicleCategoryConfig;
import org.example.carrent.services.impl.VehicleCategoryConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    VehicleCategoryConfigService vehicleCategoryConfigService;

    public CategoryController(VehicleCategoryConfigService vehicleCategoryConfigService) {
        this.vehicleCategoryConfigService = vehicleCategoryConfigService;
    }

    @GetMapping
    public List<VehicleCategoryConfig> list(){
        return this.vehicleCategoryConfigService.findAllCategories();
    }

    @GetMapping("/{category}")
    public VehicleCategoryConfig get(@PathVariable String category){
        return this.vehicleCategoryConfigService.getByCategory(category);
    }
}
