package com.example.nium.virtualcard.api.controller;

import com.example.nium.virtualcard.api.mapper.DtoMapper;
import com.example.nium.virtualcard.api.model.AmountDto;
import com.example.nium.virtualcard.api.model.VirtualCardDto;
import com.example.nium.virtualcard.api.model.VirtualCardDetailsDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class VirtualCardController {

    @Autowired
    private final CardService cardService;

    public VirtualCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<VirtualCardDto> createCard(@RequestBody @Valid VirtualCardDto request) {
        Card card = cardService.createCard(DtoMapper.toModel(request));
        return ResponseEntity.ok(DtoMapper.toDto(card));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VirtualCardDetailsDto> getCard(@PathVariable Long id) {
        Card card = cardService.getCard(id);
        return ResponseEntity.ok(DtoMapper.toCardDetailsDto(card));
    }

    @PostMapping("/{id}/spend")
    public ResponseEntity<VirtualCardDto> spend(@PathVariable Long id, @RequestBody @Valid AmountDto request) {
        Card card = cardService.spend(id, DtoMapper.toAmountModel(request));
        return ResponseEntity.ok(DtoMapper.toDto(card));
    }

    @PostMapping("/{id}/topup")
    public ResponseEntity<VirtualCardDto> topup(@PathVariable Long id, @RequestBody @Valid AmountDto request) {
        Card card = cardService.topUp(id, DtoMapper.toAmountModel(request));
        return ResponseEntity.ok(DtoMapper.toDto(card));
    }
}
