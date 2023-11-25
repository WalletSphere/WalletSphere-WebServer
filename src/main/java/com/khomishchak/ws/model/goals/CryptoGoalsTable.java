package com.khomishchak.ws.model.goals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.khomishchak.ws.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CryptoGoalsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "crypto_goals_table_records")
    private List<CryptoGoalsTableRecord> tableRecords;

    @OneToOne(mappedBy = "cryptoGoalsTable", cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;
}
