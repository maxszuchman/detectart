package com.experta.detectart.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.experta.detectart.server.model.PushNotification;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

}
