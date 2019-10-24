package com.experta.detectart.server.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.experta.detectart.server.model.deviceData.DeviceData;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, String> {
//      Collection<Device> findByUserId(String userId);
      Collection<DeviceData> findByMacAddress(String macAddress);
}
