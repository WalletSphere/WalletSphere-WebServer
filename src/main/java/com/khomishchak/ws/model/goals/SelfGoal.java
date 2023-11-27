package com.khomishchak.ws.model.goals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khomishchak.ws.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "self_goals")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class SelfGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private double goalAmount;

    @Transient
    private double currentAmount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean isAchieved = false;
    // closed means that we won't have any running logic on this entity, we will be only getting the info about closed goals
    private boolean isClosed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
