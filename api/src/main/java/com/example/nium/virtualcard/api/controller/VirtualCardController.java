package com.example.nium.virtualcard.api.controller;

import com.example.nium.virtualcard.api.mapper.CardDto;
import com.example.nium.virtualcard.api.model.CreateVirtualCardRequestDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class VirtualCardController {

    @Autowired
    private final CardService cardService;

    public VirtualCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateVirtualCardRequestDto> createCard(@RequestBody @Valid CreateVirtualCardRequestDto request) {
        Card card = cardService.createCard(CardDto.toModel(request));
        return ResponseEntity.ok(CardDto.toDto(card));
    }
}
