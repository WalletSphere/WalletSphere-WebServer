package com.khomishchak.ws.model.exchanger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khomishchak.ws.model.User;
import com.khomishchak.ws.model.enums.ExchangerCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_key_settings")
public class ApiKeySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Enumerated(value = EnumType.STRING)
    private ExchangerCode code;

    @Embedded
    private ApiKeysPair apiKeys;
}