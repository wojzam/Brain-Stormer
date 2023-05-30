package com.example.brainstormer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Enumerated(EnumType.STRING)
    private Category category;

    private boolean publicVisibility;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Idea> ideas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "topic_collaborators",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> collaborators = new HashSet<>();

    public void addCollaborator(User user) {
        if (user != creator) {
            collaborators.add(user);
            user.getCollaboratingTopics().add(this);
        }
    }

    public void removeCollaborator(User user) {
        collaborators.remove(user);
        user.getCollaboratingTopics().remove(this);
    }

    public void removeAllCollaborators() {
        for (User user : collaborators) {
            removeCollaborator(user);
        }
    }

    public boolean userAccessDenied(User user) {
        return !collaborators.contains(user) && !creator.equals(user);
    }
}
