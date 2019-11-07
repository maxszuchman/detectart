package com.experta.detectart.server.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.experta.detectart.server.exception.ResourceNotFoundException;
import com.experta.detectart.server.model.Contact;
import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.User;
import com.experta.detectart.server.model.deviceData.DeviceData;
import com.experta.detectart.server.model.deviceData.Position;
import com.experta.detectart.server.model.deviceData.Sensor;
import com.experta.detectart.server.model.deviceData.Status;
import com.experta.detectart.server.repository.ContactRepository;
import com.experta.detectart.server.repository.DeviceDataRepository;
import com.experta.detectart.server.repository.DeviceRepository;
import com.experta.detectart.server.services.PushNotificationService;
import com.experta.detectart.twilio.EmergencyMessage;
import com.experta.detectart.twilio.WhatsappService;

@RestController
public class DeviceDataController {

    public static final String DEVICE_DATA = "/deviceData";

    @Autowired
    private PushNotificationService notificationService;

    @Autowired
    private WhatsappService whatsappService;

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(DEVICE_DATA)
    public ResponseEntity<?> createDeviceData(@Valid @RequestBody final DeviceData deviceData) {

        Optional<Device> optDevice = deviceRepository.findByMacAddress(deviceData.getMacAddress());

        if (!optDevice.isPresent()) {
            throw new ResourceNotFoundException("Device with Mac Address " + deviceData.getMacAddress() + " not found");
        }

        deviceDataRepository.save(deviceData);

        Device device = optDevice.get();

        // Actualizamos el momento en que se refrescaron los datos del dispositivo
        device.setSensorDataUpdatedAt(Date.from(Instant.now()));

        User user = optDevice.get().getUser();

        if (user == null) {
            throw new ResourceNotFoundException("The device with Mac Address " + deviceData.getMacAddress() + " is not registered to an existing user.");
        }

        if (deviceData.getStatus() == Status.ALARM && device.getGeneralStatus() != Status.ALARM) {

            updateDeviceSensorsStatus(deviceData, device);
            // Mandamos notificación PUSH
            notificationService.pushNotificationForEachSensorToToken(user, device);

            // Mandamos mensajes de Whatsapp a los contactos
            Collection<Contact> contacts = contactRepository.findByUserId(user.getId());
            whatsappService.sendWhatsappMessageToContacts(new EmergencyMessage(getSensorsAsList(deviceData)
                                                          , contacts));

        } else if (deviceData.getStatus() == Status.NORMAL && device.getGeneralStatus() == Status.ALARM) {

            device.setGeneralStatusAsNormal();
            notificationService.pushNotificationDeviceBackToNormal(user, device);
        }

        // Check si el dispositivo fue movido de lugar, en cuyo caso hacer update
        Position deviceDataPosition = deviceData.getPosition();
        if (deviceDataPosition.getLatitude() != device.getLatitude()
            || deviceDataPosition.getLongitude() != device.getLongitude()
            || deviceDataPosition.getAccuracy() != device.getAccuracy()) {

            device.setLatitude(deviceDataPosition.getLatitude());
            device.setLongitude(deviceDataPosition.getLongitude());
            device.setAccuracy(deviceDataPosition.getAccuracy());
        }

        deviceRepository.save(device);

        return ResponseEntity.ok().build();
    }

    private List<Sensor> getSensorsAsList(@Valid final DeviceData deviceData) {
        List<Sensor> sensors = new ArrayList<Sensor>();

        if (deviceData.getSensor1().getStatus() == Status.ALARM) {
            sensors.add(deviceData.getSensor1());
        }

        if (deviceData.getSensor2().getStatus() == Status.ALARM) {
            sensors.add(deviceData.getSensor2());
        }

        if (deviceData.getSensor3().getStatus() == Status.ALARM) {
            sensors.add(deviceData.getSensor3());
        }

        return sensors;
    }

    private void updateDeviceSensorsStatus(@Valid final DeviceData deviceData, final Device device) {

        if (deviceData.getSensor1().getStatus() == Status.ALARM) {
            device.setSensor1Status(Status.ALARM);
        }

        if (deviceData.getSensor2().getStatus() == Status.ALARM) {
            device.setSensor2Status(Status.ALARM);
        }

        if (deviceData.getSensor3().getStatus() == Status.ALARM) {
            device.setSensor3Status(Status.ALARM);
        }
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