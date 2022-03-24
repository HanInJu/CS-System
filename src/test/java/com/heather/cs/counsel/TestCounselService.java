package com.heather.cs.counsel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.heather.cs.category.dto.Category;
import com.heather.cs.category.mapper.CategoryMapper;
import com.heather.cs.charger.dto.Charger;
import com.heather.cs.charger.mapper.ChargerMapper;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.mapper.CounselMapper;
import com.heather.cs.counsel.service.CounselService;
import com.heather.cs.user.dto.User;

@ExtendWith(MockitoExtension.class)
public class TestCounselService {

	@InjectMocks
	private CounselService counselService;

	@Mock
	private CategoryMapper categoryMapper;

	@Mock
	private CounselMapper counselMapper;

	@Mock
	private ChargerMapper chargerMapper;

	@Test
	@DisplayName("Check category validation")
	public void testValidateCategory() {
		// given
		Category category = new Category();
		category.setId(1L);
		category.setParentId(null);

		// when
		Mockito.when(categoryMapper.selectExistsCategory(category.getId())).thenReturn(true);
		Mockito.when(categoryMapper.selectExistsChildCategory(category.getId())).thenReturn(false);

		// then
		Assertions.assertDoesNotThrow(() -> counselService.validateCategory(category.getId()));
	}

	@Test
	@DisplayName("Register Counsel")
	public void testRegisterCounsel() {
		// given
		Counsel counsel = new Counsel();
		counsel.setCategoryId(22L);
		counsel.setTitle("Counsel Title For Test");
		counsel.setContent("This is counsel for test!");
		counsel.setCustomerName("Heather");
		counsel.setCustomerEmail("Heather@gmail.com");

		Charger charger = new Charger();
		charger.setUserId("Counselor");

		// when
		Mockito.when(categoryMapper.selectExistsCategory(counsel.getCategoryId())).thenReturn(true);
		Mockito.when(categoryMapper.selectExistsChildCategory(counsel.getCategoryId())).thenReturn(false);
		Mockito.when(chargerMapper.selectOneAvailableCounselor(counsel.getCategoryId())).thenReturn(charger);
		Mockito.doNothing().when(counselMapper).insertCounsel(counsel);
		counsel.setId(1L); // 크게 의미있는 건 아님  인위적으로 좀 값을 가하는 것도 괜찮고 사실 given에서 세팅해서 작ㅇ버해도 문제는 없음
		// 몇 번 돌아갔는지, 분기 지켜서 하는 게 중요
		// controller : Open API를 제공할 때는 짜기도 함
		Mockito.doNothing().when(counselMapper).insertCounselHistory(counsel.getId());

		// then
		Assertions.assertDoesNotThrow(() -> counselService.registerCounsel(counsel));
	}

	@Test
	@DisplayName("Assign uncharged counsel")
	public void testAssignCounsel() {
		// given
		String managerId = "heather";

		Counsel counsel1 = new Counsel();
		counsel1.setCategoryId(1L);
		counsel1.setTitle("Counsel Title For Test");
		counsel1.setContent("This is counsel for test!");
		counsel1.setCustomerName("Heather");
		counsel1.setCustomerEmail("Heather@gmail.com");

		Counsel counsel2 = new Counsel();
		counsel2.setCategoryId(2L);
		counsel2.setTitle("Counsel Title For Test");
		counsel2.setContent("This is counsel for test!");
		counsel2.setCustomerName("Heather");
		counsel2.setCustomerEmail("Heather@gmail.com");

		List<Counsel> unchargedCounselList = new ArrayList<>();
		unchargedCounselList.add(counsel1);
		unchargedCounselList.add(counsel2);

		Charger counselor1 = new Charger();
		counselor1.setUserId("counselor1");
		counselor1.setNumberOfCounsel(1);

		Charger counselor2 = new Charger();
		counselor2.setUserId("counselor2");
		counselor1.setNumberOfCounsel(2);

		List<Charger> counselorList = new ArrayList<>();
		counselorList.add(counselor1);
		counselorList.add(counselor2);

		// when
		Mockito.when(counselMapper.selectUnassignedCounselList(managerId)).thenReturn(unchargedCounselList);
		Mockito.when(chargerMapper.selectAvailableCounselorList(managerId)).thenReturn(counselorList);

		for (int i = 0; i < unchargedCounselList.size(); i++) { //X
			Counsel counselForUpdate = unchargedCounselList.get(i);
			Mockito.doNothing().when(counselMapper).updateCounselCharger(counselForUpdate);
			counselForUpdate.setId((long)i);// 여기는 등록하는 부분 아닌데 왜 이렇게 함?
			Mockito.doNothing().when(counselMapper).insertCounselHistory(counselForUpdate.getId());

		}
		// doNothing()이면
		// Mockito.anyObject 같은 값을 주면 되고, 사이즈만큼 실행됐는지 확인하면 됨
		// 이런 식으로 일일이 하지는 않음

		// then
		Assertions.assertDoesNotThrow(() -> counselService.assignCounsels(managerId));
	}

	//
}
