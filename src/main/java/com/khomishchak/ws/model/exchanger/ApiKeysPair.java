package com.khomishchak.ws.model.exchanger;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class ApiKeysPair {

    private String publicApi;
    private String privateKey;
}
