package com.experta.detectart.server.jobs;

import java.util.Collection;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.deviceData.Status;
import com.experta.detectart.server.repository.DeviceRepository;
import com.experta.detectart.server.services.PushNotificationService;

@Configuration
@EnableScheduling
@Transactional
public class ScheduledJobs {

    public static final int A_MINUTE_TEN = 70000;
    public static final int TWENTY_SECONDS = 20000;

    private static final Logger log = LoggerFactory.getLogger(ScheduledJobs.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    /* Cada un 1:10, buscamos todos los dispostivos que no se encuentran inactivos y refrescamos su
    estado, para ver si pasó más de un minuto sin datos. En cuyo caso los ponemos en inactivo
    y enviamos una notificación push al usuario.
    */
    @Scheduled(fixedDelay = A_MINUTE_TEN, initialDelay = TWENTY_SECONDS)
    public void checkForInactiveDevices() {

        log.info("Checking all devices for inactivity");
        Collection<Device> devices = deviceRepository.findByGeneralStatusIsNotInactive();

        devices.stream().filter(device -> device.refreshGeneralStatus() == Status.INACTIVE)
                        .forEach(device -> {

                            try {
                                pushNotificationService.pushNotificationDeviceInactive(device.getUser(), device);
                                deviceRepository.save(device);
                                log.info("Device set to INACTIVE: {}", device.getMacAddress());
                            } catch (Exception e) {
                                log.info("Error while setting device {} to INACTIVE.", device.getMacAddress());
                            }
                        });
    }


}
