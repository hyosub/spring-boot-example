package com.h4d1.util.json.response;

import java.util.List;

import com.h4d1.util.json.RuleVo;

import lombok.Data;

@Data
public class GetRulesResponse {
	private List<RuleVo> rules;
	
	public GetRulesResponse() {
	
	}
	
	public GetRulesResponse(List<RuleVo> rules) {
		this.rules = rules;
	}
}
