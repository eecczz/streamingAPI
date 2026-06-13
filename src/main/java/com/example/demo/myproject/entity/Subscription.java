package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(
        name = "subscription",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "channel_owner_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_owner_id")
    private User channelOwner;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
