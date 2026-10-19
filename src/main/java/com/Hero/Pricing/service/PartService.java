package com.Hero.Pricing.service;


import com.Hero.Pricing.entity.Part;
import com.Hero.Pricing.entity.PartPriceHistory;
import com.Hero.Pricing.repository.PartPriceHistoryRepository;
import com.Hero.Pricing.repository.PartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PartService {
    private final PartRepository partRepository;

    private final PartPriceHistoryRepository partPriceHistoryRepository;

    public PartService(PartRepository partRepository,
                        PartPriceHistoryRepository partPriceHistoryRepository)
    {
        this.partRepository = partRepository;
        this.partPriceHistoryRepository = partPriceHistoryRepository;
    }

    //Get all parts
    public List<Part> getAllParts()
    {
        return partRepository.findAll();
    }

    //Calculate total cycle price
    public BigDecimal calculateTotalCyclePrice(List<Long> partIds)
    {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(Long partId : partIds)
        {
            Part part = partRepository.findById(partId)
                    .orElseThrow(() -> new RuntimeException("Part not found with id ! "+partId));

            totalPrice = totalPrice.add(part.getPrice());
        }
        return totalPrice;
    }

    //Get part by id
    public Part getPartById(Long id)
    {
        return partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id :"+id));
    }

    //Get price history by id
    public List<PartPriceHistory> getPriceHistoryByPartId(Long partId)
    {
        if (!partRepository.existsById(partId))
        {
            throw new RuntimeException("Part not found with id: " + partId);
        }
        return partPriceHistoryRepository
                .findByPartIdOrderByUpdatedAtDesc(partId);

    }

    //Add a new part
    public Part addPart(Part part)
    {
        return partRepository.save(part);
    }

    //Update part price
    @Transactional
    public Part updatePartPrice(Long id, BigDecimal newPrice)
    {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part Not found with id: "+ id));


        //Step 1 store old price before updating
        BigDecimal oldPrice = part.getPrice();

        //step 2 Update the current price
        part.setPrice(newPrice);

        //step 3 Save the updated part
        Part updatedPart = partRepository.save(part) ;

        //step 4 Create the price history object
        PartPriceHistory history = new PartPriceHistory();
        history.setPart(updatedPart);
        history.setOldPrice(oldPrice);
        history.setNewPrice(newPrice);
        history.setUpdatedAt(java.time.LocalDateTime.now());

        //Save the history record
        partPriceHistoryRepository.save(history);

        return updatedPart;
    }

    //Delete part component
    public void deletePartComponent(Long id)
    {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part Not found with id: "+ id));
        partRepository.delete(part);
        return;
    }
}
