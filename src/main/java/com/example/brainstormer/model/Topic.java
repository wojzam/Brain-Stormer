package com.example.brainstormer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "topic_id")
    private UUID id;

    @Column(length = 50)
    private String title;

    @Column(length = 1000)
    private String description;

    private boolean publicVisibility;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private Set<Idea> ideas;

    //TODO there is a problem with deleting user or topic
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> collaborators;

    public void addCollaborator(User user) {
        collaborators.add(user);
    }

    public void removeCollaborator(User user) {
        collaborators.remove(user);
    }

    public boolean userAccessDenied(User user) {
        return !collaborators.contains(user) && !creator.equals(user);
    }
}
