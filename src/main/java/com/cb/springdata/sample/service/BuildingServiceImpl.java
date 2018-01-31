package com.cb.springdata.sample.service;

import com.cb.springdata.sample.entities.Building;
import com.cb.springdata.sample.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<Building> findByCompanyId(String companyId) {
        return buildingRepository.findByCompanyId(companyId);
    }

    public List<Building> findByCompanyIdAndNameLike(String companyId, String name, int page) {
        return buildingRepository.findByCompanyIdAndNameLikeOrderByName(companyId, name, new PageRequest(page, 20))
                .getContent();
    }

    @Override
    public Building findByCompanyAndAreaId(String companyId, String areaId) {
        return buildingRepository.findByCompanyAndAreaId(companyId, areaId);
    }

    @Override
    public List<Building> findByPhoneNumber(String telephoneNumber) {
        return buildingRepository.findByPhoneNumber(telephoneNumber);
    }

    @Override
    public Building findById(String buildingId) {
        return buildingRepository.findOne(buildingId);
    }

    @Override
    public Building save(@Valid Building building) {
        return buildingRepository.save(building);
    }

    @Override
    public Long countBuildings(String companyId) {
        return buildingRepository.countBuildings(companyId);
    }

}
