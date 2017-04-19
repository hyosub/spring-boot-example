package com.h4d1.util.json.response;

import java.util.List;

import com.h4d1.util.json.StorePhotoVo;

import lombok.Data;

@Data
public class GetStorePhotosResponse {
	private List<StorePhotoVo> storePhotos;
	
	public GetStorePhotosResponse() {
	
	}
	
	public GetStorePhotosResponse(List<StorePhotoVo> storePhotos) {
		this.storePhotos = storePhotos;
	}
}
