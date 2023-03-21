package com.example.brainstormer.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Data
@NoArgsConstructor
@Entity
@IdClass(VoteId.class)
public class Vote {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    private short value;

    @Builder
    public Vote(User user, Idea idea, short value) {
        this.user = user;
        this.idea = idea;
        setValue(value);
    }

    public void setValue(short value) {
        if (value != 1 && value != -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vote value should be 1 or -1");
        }
        this.value = value;
    }
}
