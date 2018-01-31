package com.cb.springdata.sample.service;

import com.cb.springdata.sample.entities.Building;

import javax.validation.Valid;
import java.util.List;

public interface BuildingService {

    Building save(@Valid Building building);

    Building findById(String buildingId);

    List<Building> findByCompanyId(String companyId);

    Building findByCompanyAndAreaId(String companyId, String areaId);

    List<Building> findByCompanyIdAndNameLike(String companyId, String name, int page);

    List<Building> findByPhoneNumber(String telephoneNumber);

    Long countBuildings(String companyId);

}
