package com.h4d1.rule.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.rule.RuleService;
import com.h4d1.rule.entity.Rule;
import com.h4d1.store.value.StoreValueService;
import com.h4d1.util.json.RuleVo;
import com.h4d1.util.json.response.GetRulesResponse;

@RestController
@RequestMapping(value="/rules")
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@Autowired
	private StoreValueService storeValueService;
	
	private static final Logger logger = LoggerFactory.getLogger(RuleController.class);
	
	@RequestMapping(method=RequestMethod.POST)
	public void createRule(@RequestBody(required=true) Rule rule) throws ServiceException {
		try {
			logger.info("Check request-param. [rule={}]", rule);
			
			//규칙 및 하위 규칙 엔티티 생성
			ruleService.createRule(rule);
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	@ResponseBody
	public RuleVo getRule(@PathVariable(required=true) String id) throws ServiceException {
		try {
			logger.info("Check request-param. [none]");
			
			//서비스로부터 특정 규칙 정보를 가져옴
			Rule searchedRule = ruleService.getRule(id);
			RuleVo ruleVo = null;
			
			if (searchedRule != null) {
				ruleVo = new RuleVo();
				ruleVo.updateFields(searchedRule);
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			return ruleVo;
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/active")
	@ResponseBody
	public RuleVo getActiveRule() throws ServiceException {
		try {
			logger.info("Check request-param. [none]");
			
			//서비스로부터 활성화된 규칙 정보 목록을 가져옴
			Rule activeRule = ruleService.getActiveRule();
			RuleVo ruleVo = null;
			
			if (activeRule != null) {
				ruleVo = new RuleVo();
				ruleVo.updateFields(activeRule);
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			return ruleVo;
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public GetRulesResponse getRules() throws ServiceException {
		try {
			logger.info("Check request-param. [none]");
			
			//서비스로부터 규칙 정보 목록을 가져옴
			List<Rule> rules = ruleService.getRules();
			List<RuleVo> ruleVos = new ArrayList<RuleVo>();
			
			for (Rule rule : rules) {
				RuleVo ruleVo = new RuleVo();
				ruleVo.updateFields(rule);
				ruleVos.add(ruleVo);
			}
			
			//결과 리턴 (Jackson이 자동으로 json을 생성)
			return new GetRulesResponse(ruleVos);
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/{id}")
	@ResponseBody
	public RuleVo modifyRule(@PathVariable(required=true) String id,
			@RequestBody(required=true) Rule rule) throws ServiceException {
		try {
			logger.info("Check request-param. [id={}][rule={}]", id, rule);
			
			Rule modifiedRule = ruleService.modifyRule(id, rule);
			RuleVo ruleVo = null;
			
			if (modifiedRule != null) {
				ruleVo = new RuleVo();
				ruleVo.updateFields(modifiedRule);
			} else {
				//Not found exception 처리
				throw new ServiceException(ServiceError.NOT_FOUND);
			}
			
			return ruleVo;
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/value")
	public void evaluateStoreValues() throws ServiceException {
		try {
			logger.info("Check request-param. [none]");
			
			storeValueService.evaluateStoreValue();
		} catch (ServiceException e) {
			logger.error("ServiceException occured. Handle exception.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
}
