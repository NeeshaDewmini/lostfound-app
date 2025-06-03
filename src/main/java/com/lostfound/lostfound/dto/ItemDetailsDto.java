package com.lostfound.lostfound.dto;

import com.lostfound.lostfound.model.Item;
import com.lostfound.lostfound.model.Request;

public class ItemDetailsDto {
    private Item item;
    private boolean alreadyRequested;
    private Request.Status requestStatus; 

    // Constructors
    public ItemDetailsDto(Item item, boolean alreadyRequested, Request.Status requestStatus) {
        this.item = item;
        this.alreadyRequested = alreadyRequested;
        this.requestStatus = requestStatus;
    }

    // Getters & Setters
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public boolean isAlreadyRequested() { return alreadyRequested; }
    public void setAlreadyRequested(boolean alreadyRequested) { this.alreadyRequested = alreadyRequested; }

    public Request.Status getRequestStatus() { return requestStatus; }
    public void setRequestStatus(Request.Status requestStatus) { this.requestStatus = requestStatus; }
}
