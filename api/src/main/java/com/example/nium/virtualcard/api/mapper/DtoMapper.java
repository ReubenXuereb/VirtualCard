package com.example.nium.virtualcard.api.mapper;

import com.example.nium.virtualcard.api.model.AmountDto;
import com.example.nium.virtualcard.api.model.TransactionsDto;
import com.example.nium.virtualcard.api.model.VirtualCardDto;
import com.example.nium.virtualcard.api.model.VirtualCardDetailsDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
import com.example.nium.virtualcard.core.model.AmountRequest;
import com.example.nium.virtualcard.core.model.CreateVirtualCardRequest;

import java.util.List;

public class DtoMapper {

    public static CreateVirtualCardRequest toModel(VirtualCardDto request) {
        return new CreateVirtualCardRequest(
                        request.cardholderName(),
                        request.initialBalance()
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

    public static AmountRequest toAmountModel(AmountDto request) {
        return new AmountRequest(
                request.amount()
        );
    }

    public static TransactionsDto toTransactionDto(Transaction transaction) {
        return new TransactionsDto(
                transaction.getId(),
                transaction.getCard().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }

    public static List<TransactionsDto> toTransactionDtoList(List<Transaction> transactions) {
        return transactions.stream().map(DtoMapper::toTransactionDto).toList();
    }

}
