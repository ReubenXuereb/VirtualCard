package com.example.nium.virtualcard;

import com.example.nium.virtualcard.api.controller.VirtualCardController;
import com.example.nium.virtualcard.api.model.VirtualCardDto;
import com.example.nium.virtualcard.core.entity.Card;
import com.example.nium.virtualcard.core.entity.Transaction;
import com.example.nium.virtualcard.core.model.CardStatus;
import com.example.nium.virtualcard.core.model.CreateVirtualCardRequest;
import com.example.nium.virtualcard.core.model.TransactionType;
import com.example.nium.virtualcard.core.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VirtualCardController.class)
public class VirtualCardControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card("Reuben", BigDecimal.valueOf(100), CardStatus.ACTIVE);
        card.setId(1L);
    }

    @Test
    void should_get_card_details_successfully() throws Exception {
        VirtualCardDto requestDto = new VirtualCardDto("Reuben", BigDecimal.valueOf(100));

        when(cardService.createCard(any(CreateVirtualCardRequest.class))).thenReturn(card);

        mockMvc.perform(post("/api/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardholderName").value("Reuben"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.initialBalance").value(100));
    }

    @Test
    void should_get_card_by_id_successfully() throws Exception {
        when(cardService.getCard(1L)).thenReturn(card);

        mockMvc.perform(get("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardholderName").value("Reuben"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100));
    }

    @Test
    void should_spend_amount_successfully() throws Exception {
        BigDecimal spendAmount = BigDecimal.valueOf(10);
        card.setBalance(card.getBalance().subtract(spendAmount));

        when(cardService.spend(any(), any())).thenReturn(card);

        mockMvc.perform(post("/api/cards/1/spend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 10}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.initialBalance").value(90));
    }

    @Test
    void should_topup_amount_successfully() throws Exception {
        BigDecimal topupAmount = BigDecimal.valueOf(50);
        card.setBalance(card.getBalance().add(topupAmount));

        when(cardService.topUp(any(), any())).thenReturn(card);

        mockMvc.perform(post("/api/cards/1/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 50}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.initialBalance").value(150));
    }

    @Test
    void should_get_transactions_for_card() throws Exception {
        List<Transaction> transactions = List.of(
                new Transaction(card, TransactionType.CREATE, BigDecimal.valueOf(100)),
                new Transaction(card, TransactionType.TOPUP, BigDecimal.valueOf(50))
        );

        when(cardService.getTransactionHistory(1L)).thenReturn(transactions);

        mockMvc.perform(get("/api/cards/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("CREATE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value("TOPUP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(50));
    }

}
