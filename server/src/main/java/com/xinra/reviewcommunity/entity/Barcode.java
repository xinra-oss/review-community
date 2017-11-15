package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Barcode extends MarketSpecificEntity {
  
  /**
   * Creates a barcode.
   */
  public Barcode() {
    // Barcode doesn't need a serial but has one because MarketSpecificEntity extends from
    // SerialEntity. Without multi-inheritance/traits this is currently simplest way to
    // solve this. If this becomes an issue for other entities as well, we'll reconsider.
    setSerial(-1);
  }

  @NonNull
  private String code;
  
  /**
   * If this is {@code null} it means that the product with this barcode is not in the scope of this
   * application.
   */
  @OneToOne
  private Product product;
  
}
