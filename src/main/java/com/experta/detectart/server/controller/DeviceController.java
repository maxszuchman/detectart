package com.experta.detectart.server.controller;

import static com.experta.detectart.server.controller.UserController.USERS;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.experta.detectart.server.exception.ExistentDeviceException;
import com.experta.detectart.server.exception.ResourceNotFoundException;
import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.repository.DeviceRepository;
import com.experta.detectart.server.repository.UserRepository;

@RestController
public class DeviceController {

    public static final String DEVICES = "/devices";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(USERS + "/{userId}" + DEVICES)
    public Collection<Device> getAllDevicessByUserId(@PathVariable (value = "userId") final String userId) {
        return deviceRepository.findByUserId(userId);
    }

    @GetMapping(DEVICES + "/{macAddress}")
    public Device getDeviceByMacAddress(@PathVariable (value = "macAddress") final String macAddress) {

        return deviceRepository.findByMacAddress(macAddress).orElseThrow(() -> new ResourceNotFoundException("Device MacAddress " + macAddress + " not found"));

    }

    @GetMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
    public Device getDeviceByUserIdAndMacAddress(
                                    @PathVariable (value = "userId") final String userId
                                    , @PathVariable (value = "macAddress") final String macAddress) {

        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return deviceRepository.findByMacAddressAndUserId(macAddress, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Device MacAddress " + macAddress + "not found"));
    }

    @PostMapping(USERS + "/{userId}" + DEVICES)
    public Device createDevice(@PathVariable (value = "userId") final String userId,
                                 @Valid @RequestBody final Device device) {

        if (deviceRepository.existsById(device.getMacAddress())) {
            throw new ExistentDeviceException("Device " + device.getMacAddress()
            + " is already attached to user "
            + deviceRepository.findByMacAddress(device.getMacAddress()).get().getUser().getId());
        }

        return userRepository.findById(userId).map(user -> {

            device.setUser(user);
            return deviceRepository.save(device);

        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
    public Device updateDevice(@PathVariable (value = "userId") final String userId,
                                 @PathVariable (value = "macAddress") final String macAddress,
                                 @Valid @RequestBody final Device deviceRequest) {

        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return deviceRepository.findById(macAddress).map(device -> {

            deviceRequest.copyInto(device);
            return deviceRepository.save(device);

        }).orElseThrow(() -> new ResourceNotFoundException("ContactId " + macAddress + "not found"));
    }

    @DeleteMapping(USERS + "/{userId}" + DEVICES + "/{macAddress}")
    public ResponseEntity<?> deleteDevice(@PathVariable (value = "userId") final String userId,
                              @PathVariable (value = "macAddress") final String macAddress) {
        return deviceRepository.findByMacAddressAndUserId(macAddress, userId).map(contact -> {

            deviceRepository.delete(contact);
            return ResponseEntity.ok().body(contact);
        }).orElseThrow(() -> new ResourceNotFoundException("Device not found with Mac Address " + macAddress + " and userId " + userId));
    }
}