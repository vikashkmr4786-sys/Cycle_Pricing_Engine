package com.Hero.Pricing.dto;
import java.util.List;
public class CyclePriceRequest {
    private List<Long> partIds;
    public List<Long> getPartIds()
    {
        return partIds;
    }
    public void setPartIds(List<Long> partIds)
    {
        this.partIds = partIds;
    }
}
