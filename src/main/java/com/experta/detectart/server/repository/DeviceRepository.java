package com.experta.detectart.server.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.experta.detectart.server.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    Collection<Device> findByUserId(Long userId);
    Optional<Device> findByMacAddress(String macAddress);
    Optional<Device> findByMacAddressAndUserId(String id, Long userId);
}
