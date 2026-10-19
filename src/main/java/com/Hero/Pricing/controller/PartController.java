package com.Hero.Pricing.controller;


import com.Hero.Pricing.dto.CyclePriceRequest;
import com.Hero.Pricing.entity.Part;
import com.Hero.Pricing.entity.PartPriceHistory;
import com.Hero.Pricing.service.PartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {
    private final PartService partService;
    public PartController(PartService partService)
    {
        this.partService = partService;
    }

    //Get all parts
    @GetMapping
    public List<Part> getAllParts()
    {
        return partService.getAllParts();
    }

    //Get part by id
    @GetMapping("/{id}")
    public Part getPartById(@PathVariable Long id)
    {
        return partService.getPartById(id);
    }

    //Get price history by part id
    @GetMapping("/{partId}/history")
    public List<PartPriceHistory> getPriceHistoryByPartId(
            @PathVariable Long partId)
    {
        return partService.getPriceHistoryByPartId(partId);
    }

    //Calculate total cycle price
    @PostMapping("/calculate-price")
    public BigDecimal calculateTotalCyclePrice(@RequestBody CyclePriceRequest request)
    {
        return partService.calculateTotalCyclePrice(request.getPartIds());
    }


    //Add a new part
    @PostMapping
    public Part addPart(@Valid @RequestBody Part part)
    {
        return partService.addPart(part);
    }

    //Update part's price
    @PutMapping("/{id}")
    public String updatePartPrice(@PathVariable Long id, @RequestBody BigDecimal newPrice)
    {
        partService.updatePartPrice(id,newPrice);
        return "Update price successfully";
    }

    //Delete part component by id
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id)
    {
        partService.deletePartComponent(id);
        return "Delete component successfully";
    }
}
