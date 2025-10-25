package com.example.nium.virtualcard.api.mapper;

import com.example.nium.virtualcard.api.model.CreateVirtualCardRequestDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.model.CreateVirtualCardRequest;

public class CardDto {

    public static CreateVirtualCardRequest toModel(CreateVirtualCardRequestDto request) {
        return new CreateVirtualCardRequest(
                        request.getCardHolderName(),
                        request.getInitialBalance()
                );
    }

    public static CreateVirtualCardRequestDto toDto(Card request) {
        return new CreateVirtualCardRequestDto(
                request.getCardholderName(),
                request.getBalance()
        );
    }
}
