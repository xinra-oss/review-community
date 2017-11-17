package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.reviewcommunity.entity.ChildSerial;
import com.xinra.reviewcommunity.entity.Serial;
import com.xinra.reviewcommunity.entity.SerialEntity;
import com.xinra.reviewcommunity.repo.ChildSerialRepository;
import com.xinra.reviewcommunity.repo.SerialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(
    // This ensures that there are NO concurrent transactions. It prevents any concurrency related
    // issues but is not really necessary. If performance becomes an issue, we can switch to
    // pessimistic locking which is more suitable here.
    isolation = Isolation.SERIALIZABLE,
    // The serial is bumped in its own transaction. Because of this, the consuming transaction
    // allows concurrency. If the consuming transaction rolls back after acquiring a serial, the
    // serial is lost and remains unused.
    propagation = Propagation.REQUIRES_NEW)
public class SerialService extends AbstractService {
  
  private @Autowired SerialRepository<Serial> serialRepo;
  private @Autowired ChildSerialRepository<ChildSerial> childSerialRepo;

  /**
   * Gets the next top-level serial of an entity class.
   */
  public int getNextSerial(Class<? extends SerialEntity> entityType) {
    Serial serial = serialRepo.findByName(entityType.getName());
    if (serial == null) {
      serial = entityFactory.createEntity(Serial.class);
      serial.setName(entityType.getName());
    }
    final int nextSerial = serial.getLastSerial() + 1;
    serial.setLastSerial(nextSerial);
    serialRepo.save(serial);
    log.debug("Top-level serial of {} incremented to {}.", entityType.getName(), nextSerial);
    return nextSerial;
  }
  
  /**
   * Get the next child serial of an entity.
   * @param name the name of the child. Must be unique within the entity class.
   */
  public int getNextChildSerial(EntityPk entityPk, String name) {
    ChildSerial serial = childSerialRepo.findByEntityIdAndName(entityPk.getId(), name);
    if (serial == null) {
      serial = entityFactory.createEntity(ChildSerial.class);
      serial.setName(name);
      serial.setEntityId(entityPk.getId());
    }
    final int nextSerial = serial.getLastSerial() + 1;
    serial.setLastSerial(nextSerial);
    childSerialRepo.save(serial);
    log.debug("Child serial '{}' of entity with id {} incremented to {}.", name, entityPk,
        nextSerial);
    return nextSerial;
  }

}
