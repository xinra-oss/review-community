package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.dto.BrandDto;
import com.xinra.reviewcommunity.dto.CreateBrandDto;
import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.repo.BrandRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class BrandService extends AbstractService {

  private @Autowired BrandRepository<Brand> brandRepo;

  /**
   * Creates a new Brand.
   */
  public void createBrand(@NonNull CreateBrandDto createBrandDto) {

    Brand brand = entityFactory.createEntity(Brand.class);
    brand.setName(createBrandDto.getName());

    int serial = serviceProvider.getService(SerialService.class).getNextSerial(Brand.class);
    brand.setSerial(serial);
    brandRepo.save(brand);

    log.info("Created brand with name '{}'", createBrandDto.getName());
  }

  /**
   * Returns a list of all brands.
   */
  public List<BrandDto> getAllBrands() {
    return Streams.stream(brandRepo.findAll())
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  private BrandDto toDto(Brand brand) {
    BrandDto brandDto = dtoFactory.createDto(BrandDto.class);

    brandDto.setName(brand.getName());
    brandDto.setSerial(brand.getSerial());

    return brandDto;
  }
}
