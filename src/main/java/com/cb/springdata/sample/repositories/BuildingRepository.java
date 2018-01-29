package com.cb.springdata.sample.repositories;

import com.cb.springdata.sample.entities.Building;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "building")
public interface BuildingRepository extends CouchbaseRepository<Building, String> {

    List<Building> findByCompanyId(String companyId);

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and companyId = $1 and $2 within #{#n1ql.bucket}")
    Building findByCompanyAndAreaId(String companyId, String areaId);

    List<Building> findByCompanyIdAndNameLike(String companyId, String name);

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} AND ANY phone IN phoneNumbers SATISFIES phone = $1 END")
    List<Building> findByPhoneNumber(String telephoneNumber);

    @Query("SELECT COUNT(*) AS count FROM #{#n1ql.bucket} WHERE #{#n1ql.filter} and companyId = $1")
    Long countBuildings(String companyId);

}
