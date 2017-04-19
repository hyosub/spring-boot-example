package com.h4d1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.h4d1.Application;
import com.h4d1.exception.ServiceException;
import com.h4d1.store.StorePhotoService;
import com.h4d1.store.StoreService;
import com.h4d1.store.entity.Store;
import com.h4d1.store.entity.StorePhoto;
import com.h4d1.util.UuidGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Application.class)
public class JpaTest {
	
	@Autowired
	private UuidGenerator uuidGenerator;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StorePhotoService photoService;
	
	@Test
	public void testAddStorePhoto() {
		String storeId = "f322e09e751a41fe905c1209e001c669";
		
		try {
			Store store = storeService.getStore(storeId);
			
			String storePhotoId = uuidGenerator.generateUUID();
			
			StorePhoto storePhoto = new StorePhoto();
			storePhoto.initMandatoryFieldValue(storePhotoId);
			storePhoto.setUri("/photos/" + storePhotoId);
			storePhoto.setDefault(true);
			storePhoto.setSelfPictured(true);
			storePhoto.setStore(store);
			
			photoService.createStorePhoto(storePhoto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
