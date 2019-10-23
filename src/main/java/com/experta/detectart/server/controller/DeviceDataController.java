package com.experta.detectart.server.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.experta.detectart.server.exception.ResourceNotFoundException;
import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.User;
import com.experta.detectart.server.model.deviceData.DeviceData;
import com.experta.detectart.server.model.deviceData.Status;
import com.experta.detectart.server.repository.DeviceDataRepository;
import com.experta.detectart.server.repository.DeviceRepository;
import com.experta.detectart.server.services.PushNotificationService;

@RestController
public class DeviceDataController {

    public static final String DEVICE_DATA = "/deviceData";

    @Autowired
    private PushNotificationService notificationService;

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping(DEVICE_DATA)
    public ResponseEntity<?> createDeviceData(@Valid @RequestBody final DeviceData deviceData) {

        Optional<Device> device = deviceRepository.findByMacAddress(deviceData.getMacAddress());

        if (!device.isPresent()) {
            throw new ResourceNotFoundException("Device with Mac Address " + deviceData.getMacAddress() + " not found");
        }

        deviceDataRepository.save(deviceData);

        if (deviceData.getStatus() == Status.ALARM) {
            User user = device.get().getUser();

            if (user == null) {
                throw new ResourceNotFoundException("The device with Mac Address " + deviceData.getMacAddress() + " is not registered to an existing user.");
            }

            notificationService.pushNoificationToToken(user);
        }

        return ResponseEntity.ok().build();
    }

//    @GetMapping(USERS + "/{userId}" + DEVICES)
//    public Collection<Device> getAllDevicessByUserId(@PathVariable (value = "userId") final Long userId) {
//        return deviceDataRepository.findByUserId(userId);
//    }
//
//    @GetMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
//    public Device getDeviceByUserIdAndMacAddress(
//                                    @PathVariable (value = "userId") final Long userId
//                                    , @PathVariable (value = "macAddress") final String macAddress) {
//
//        if(!userRepository.existsById(userId)) {
//            throw new ResourceNotFoundException("UserId " + userId + " not found");
//        }
//
//        return deviceDataRepository.findByMacAddressAndUserId(macAddress, userId)
//                                .orElseThrow(() -> new ResourceNotFoundException("Device MacAddress " + macAddress + "not found"));
//    }
//
//    @PutMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
//    public Device updateDevice(@PathVariable (value = "userId") final Long userId,
//                                 @PathVariable (value = "macAddress") final String macAddress,
//                                 @Valid @RequestBody final Device deviceRequest) {
//
//        if(!userRepository.existsById(userId)) {
//            throw new ResourceNotFoundException("UserId " + userId + " not found");
//        }
//
//        return deviceDataRepository.findById(macAddress).map(device -> {
//
//            deviceRequest.copyInto(device);
//            return deviceDataRepository.save(device);
//
//        }).orElseThrow(() -> new ResourceNotFoundException("ContactId " + macAddress + "not found"));
//    }
//
//    @DeleteMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
//    public ResponseEntity<?> deleteDevice(@PathVariable (value = "userId") final Long userId,
//                              @PathVariable (value = "macAddress") final String macAddress) {
//        return deviceDataRepository.findByMacAddressAndUserId(macAddress, userId).map(contact -> {
//
//            deviceDataRepository.delete(contact);
//            return ResponseEntity.ok().body(contact);
//        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with Mac Address " + macAddress + " and userId " + userId));
//    }
}