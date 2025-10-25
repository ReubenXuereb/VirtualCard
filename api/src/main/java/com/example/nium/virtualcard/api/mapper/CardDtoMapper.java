package com.example.nium.virtualcard.api.mapper;

import com.example.nium.virtualcard.api.model.VirtualCardDto;
import com.example.nium.virtualcard.api.model.VirtualCardDetailsDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.model.CreateVirtualCardRequest;

public class CardDtoMapper {

    public static CreateVirtualCardRequest toModel(VirtualCardDto request) {
        return new CreateVirtualCardRequest(
                        request.getCardHolderName(),
                        request.getInitialBalance()
                );
    }

    public static VirtualCardDto toDto(Card request) {
        return new VirtualCardDto(
                request.getCardholderName(),
                request.getBalance()
        );
    }

    public static VirtualCardDetailsDto toCardDetailsDto(Card request) {
        return new VirtualCardDetailsDto(
                request.getId(),
                request.getCardholderName(),
                request.getBalance(),
                request.getCreatedAt(),
                request.getStatus()
        );
    }

}
