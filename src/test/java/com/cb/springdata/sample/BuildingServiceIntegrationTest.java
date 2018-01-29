package com.cb.springdata.sample;

import com.cb.springdata.sample.entities.Area;
import com.cb.springdata.sample.entities.Building;
import com.cb.springdata.sample.service.BuildingService;
import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BuildingServiceIntegrationTest extends SampleApplicationTests {

    public static final String BUILDING_1 = "building::1";
    public static final String COMPANY_2 = "company::2";
    public static final String WHITE = "white";
    public static final String BLACK = "black";

    @Autowired
    private BuildingService buildingService;



    @Test
    public void testSave() {
        val building = new Building(BUILDING_1, "couchbase",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);

        Building newBuilding = buildingService.findById(BUILDING_1);
        assertThat(newBuilding, equalTo(building));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveWithMissingNameField() {
        val building = new Building("building::1", null,
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);
    }

    @Test
    public void testFindByCompanyId() {
        val building = new Building(BUILDING_1, "building",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);

        val newBuildings = buildingService.findByCompanyId(COMPANY_2);
        assertThat(newBuildings, equalTo(Arrays.asList(building)));
    }

    @Test
    public void testFindByCompanyIdAndNameLike() {
        String buildingName = "couchbase";
        val building = new Building(BUILDING_1, "couchbase",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);

        val building2 = new Building("building::4", "AnotherBuilding",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building2);

        val newBuildings = buildingService.findByCompanyIdAndNameLike(COMPANY_2, "cou%");

        assertThat(newBuildings, hasSize(1));
        assertThat(buildingName, equalTo(newBuildings.get(0).getName()));
    }


    @Test
    public void testFindArea2LevelsDeep(){
        String targetAreaId = "ref1ref1";

        val areas = Arrays.asList(
                new Area("ref1","ref1name", Arrays.asList(
                        new Area(targetAreaId,"ref1ref1name", null))),
                new Area("ref2","ref2name",  null));
        val building1 = new Building(BUILDING_1, "couchbase",
                "companyId::1", areas, new ArrayList<>(), WHITE);

        val areas2 = Arrays.asList(
                new Area("ref3","ref3name", null),
                new Area("ref4","ref4name", null));
        val building2 =  new Building("buildingId::2", "couchbase manchester",
                "companyId::1", areas2, new ArrayList<>(), WHITE);

        buildingService.save(building1);
        buildingService.save(building2);

        val foundBusiness = buildingService.findByCompanyAndAreaId(COMPANY_2, targetAreaId);

        assertThat(foundBusiness, equalTo(building1));
    }

    @Test
    public void testFindPhoneNumber() {
        String targetPhoneNumber = "+44 (0) 203 837 9130";
        val building = new Building(BUILDING_1, "building",
                COMPANY_2, new ArrayList<>(), Arrays.asList(targetPhoneNumber,
                "1-650-417-7500", "+1 (415) 963-4174", "1-650-417-7500", "1-650-964-7935"), WHITE);
        buildingService.save(building);

        val building2 = new Building("building::3", "Some other building",
                COMPANY_2, new ArrayList<>(), Arrays.asList("+44 (0) 333 837 9130", "5-750-417-7500"), WHITE);
        buildingService.save(building2);

        val foundBuildings = buildingService.findByPhoneNumber(targetPhoneNumber);

        assertThat(foundBuildings, hasSize(1));
        assertThat(foundBuildings.get(0).getId(), equalTo(BUILDING_1));
    }

    @Test
    public void testCount() {
        val building = new Building(BUILDING_1, "building1",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);

        val building2 = new Building("building::4", "building2",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building2);

        val buildingFromOtherCompany = new Building("building::5", "buildingFromOtherCompany",
                "company::5", new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(buildingFromOtherCompany);

        Long count = buildingService.countBuildings(COMPANY_2);

        assertTrue(count == 2);
    }


    @Test
    public void testInvertColors() {

        val building2Id = "building::7";
        val building = new Building(BUILDING_1, "building1",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), WHITE);
        buildingService.save(building);

        val building2 = new Building(building2Id, "building2",
                COMPANY_2, new ArrayList<>(), new ArrayList<>(), BLACK);
        buildingService.save(building2);

        List<Building> updatedBuildings = buildingService.invertColors();

        Building firstBuilding = buildingService.findById(BUILDING_1);
        assertThat(firstBuilding.getColor(), equalTo(BLACK));


        Building secondBuilding = buildingService.findById(building2Id);
        assertThat(secondBuilding.getColor(), equalTo(WHITE));
    }
}
