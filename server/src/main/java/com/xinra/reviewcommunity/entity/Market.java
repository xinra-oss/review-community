package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Market extends BaseEntity {
  
  public interface UpdateListener extends EntityUpdateListener<Market> {}

  private String name;
  private @NonNull String slug;
  
  @PostRemove
  @PostUpdate
  @PostPersist
  private void onUpdate() {
    EntityUpdateListener.delegateToBean(UpdateListener.class, this);
  }
  
}
