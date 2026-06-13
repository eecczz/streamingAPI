package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Subscription;
import com.example.demo.myproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findBySubscriberAndChannelOwner(User subscriber, User channelOwner);
    boolean existsBySubscriberAndChannelOwner(User subscriber, User channelOwner);
    long countByChannelOwner(User channelOwner);
    List<Subscription> findBySubscriberOrderByCreatedAtDesc(User subscriber);
}
