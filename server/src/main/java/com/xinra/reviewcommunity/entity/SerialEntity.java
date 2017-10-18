package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.BaseEntity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Getter;


@Getter
@MappedSuperclass
public class SerialEntity extends BaseEntity {

  private int serial;
  
  /**
   * Sets the serial of this entity. This must be called once (and only once) before persisting the
   * entity.
   * @param serial must not be 0
   */
  public void setSerial(int serial) {
    if (this.serial != 0) {
      throw new IllegalStateException("Serial can only be set once");
    }
    if (serial == 0) {
      throw new IllegalArgumentException("Serial must not be 0");
    }
    this.serial = serial;
  }
  
  @PrePersist
  private void validate() {
    if (serial == 0) {
      throw new IllegalStateException("Serial must be set before saving to the database");
    }
  }

}
