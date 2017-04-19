package com.h4d1.util.json.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.h4d1.util.json.StoreSummaryVo;

import lombok.Data;

@Data
public class GetStoresResponse {
	@JsonProperty("stores")
	private List<StoreSummaryVo> storeSummaries;
	
	public GetStoresResponse() {
	
	}
	
	public GetStoresResponse(List<StoreSummaryVo> storeSummaries) {
		this.storeSummaries = storeSummaries;
	}
}
