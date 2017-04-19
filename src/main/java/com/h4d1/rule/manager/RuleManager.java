package com.h4d1.rule.manager;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.rule.RuleService;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.ViewpointRule;
import com.h4d1.rule.entity.type.CategoryType;
import com.h4d1.rule.entity.type.ValueType;
import com.h4d1.rule.entity.type.ViewpointType;
import com.h4d1.rule.respository.CategoryRuleRepository;
import com.h4d1.rule.respository.RuleRepository;
import com.h4d1.rule.respository.ValueRuleRepository;
import com.h4d1.rule.respository.ViewpointRuleRepository;
import com.h4d1.util.UuidGenerator;
import com.h4d1.util.validation.RuleCompareResult;

@Service
@Transactional
public class RuleManager implements RuleService {

	/**
	 * PK인 uuid를 생성하는 서비스
	 */
	@Autowired
	private UuidGenerator uuidGenerator;
	
	@Autowired
	private RuleRepository ruleRepository;
	
	@Autowired
	private ViewpointRuleRepository viewpointRuleRepository;
	
	@Autowired
	private CategoryRuleRepository categoryRuleRepository;
	
	@Autowired
	private ValueRuleRepository valueRuleRepository;
	
	//현재는 필요 없는 것으로 간주 (요청 전문 내에 해당 값을 받고 있음)
	//private final int MAXIMUM_REFLECTION_RATE = 100;
	private static final String CONDITION_OPERATION = "~";
	
	private static final Logger logger = LoggerFactory.getLogger(RuleManager.class);
	
	@Override
	public void createRule(Rule rule) throws ServiceException {
		//기존 활성화 된 규칙이 있을 경우 해당 규칙을 비활성화 시킴
		List<Rule> rules = ruleRepository.findAll();
		
		for (Rule storedRule : rules) {
			//현재 활성화된 룰을 찾음
			if (storedRule.isActive()) {
				storedRule.setActive(false);
				//해당 룰을 업데이트
				ruleRepository.save(storedRule);
			}
		}
		
		//최상위 규칙 노드 아이디 생성
		String ruleId = uuidGenerator.generateUUID();
		
		logger.info("Check rule. [rule={}]", rule);
		
		if (rule != null) {
			//규칙 확인
			if (validateRule(rule)) {
				//필수 필드 초기화
				rule.initMandatoryFieldValue(ruleId);
				rule.setActive(true);
				
				//두번쨰 노드인 가치 관점 규칙 목록을 가져옴
				List<ViewpointRule> viewpointRules = rule.getViewpointRules();
				
				//null 조건 확인 (null일 경우 Excpetion 발생시킴)
				if (viewpointRules != null) {
					//최소 한 가지 이상의 가치 관점 규칙 정보가 필요 (0개일 경우 Exception 발생 시킴)
					if (!viewpointRules.isEmpty()) {
						//가치 관점 목록들을 순회하면서 가치 관점 규칙 정보를 생성하고 세번째 노드(카테고리 규칙)를 순회
						for (ViewpointRule viewpointRule : viewpointRules) {
							//두번째 노드인 가치 관점 규칙 아이디 생성
							String viewpointRuleId = uuidGenerator.generateUUID();
							
							//필수 필드 초기화
							viewpointRule.initMandatoryFieldValue(viewpointRuleId);
							//최상단 노드인 규칙 정보와 연관 관계를 설정
							viewpointRule.setRule(rule);
							//리스트 업데이트
							viewpointRules.set(viewpointRules.indexOf(viewpointRule), viewpointRule);
							
							//세번째 노드인 카테고리 규칙 목록을 가져옴
							List<CategoryRule> categoryRules = viewpointRule.getCategoryRules();
							
							//null 조건 확인 (null일 경우 Excpetion 발생시킴)
							if (categoryRules != null) {
								//최소 한 가지 이상의 카테고리 규칙 정보가 필요 (0개일 경우 Exception 발생 시킴)
								if (!categoryRules.isEmpty()) {
									//카테고리 규칙 목록들을 순회하면서 카테고리 규칙 정보를 생성하고 마지막 노드(가치 규칙)를 탐색
									for (CategoryRule categoryRule : categoryRules) {
										//카테고리 규칙 노드 아이디 생성
										String categoryRuleId = uuidGenerator.generateUUID();
										
										//필수 필드 초기화
										categoryRule.initMandatoryFieldValue(categoryRuleId);
										//두번째 노드인 가치 관점 규칙 정보와 연관관계를 설정
										categoryRule.setViewpointRule(viewpointRule);
										
										//가치 규칙 정보를 가져옴
										ValueRule valueRule = categoryRule.getValueRule();
										
										//null 조건 확인 (null일 경우 Excpetion 발생시킴)
										if (valueRule != null) {
											String valueRuleId = uuidGenerator.generateUUID();
											valueRule.initMandatoryFieldValue(valueRuleId);
											
											//이하 타입들은 기본으로 해당 값을 넣어줌 (불변)
											if (valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_ASC.getName()) 
													|| valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_DESC.getName())) {
												valueRule.setFirstRankCondition("true");
												valueRule.setSecondRankCondition("false");
											}
											
											//CATEGORY_RULE 엔티티와 VALUE_RULE 엔티티는 OneToOne 관계이므로 VALUE_RULE 엔티티 정보를 먼저 저장하고 CATEGORY_RULE과의 연관 관계를 맺음
											categoryRule.setValueRule(valueRule);
											
											//리스트 업데이트
											categoryRules.set(categoryRules.indexOf(categoryRule), categoryRule);
										} else {
											logger.error("valueRule is null. [valueRule={}]", valueRule);
											throw new ServiceException(ServiceError.BAD_REQUEST);
										}
									}
								} else {
									logger.error("categoryRules is empty. [categoryRules={}]", categoryRules);
									throw new ServiceException(ServiceError.BAD_REQUEST);
								}
							} else {
								logger.error("categoryRules is null. [categoryRules={}]", categoryRules);
								throw new ServiceException(ServiceError.BAD_REQUEST);
							}
						}
					} else {
						logger.error("viewpointRules is empty. [viewpointRules={}]", viewpointRules);
						throw new ServiceException(ServiceError.BAD_REQUEST);
					}
					
					//규칙 정보 저장
					//모든 규칙 엔티티의 연관관계 설정 후 Cascade에 의해 자식 규칙 엔티티들까지 모두 저장가능
					ruleRepository.save(rule);
				} else {
					logger.error("viewpointRules is null. [viewpointRules={}]", viewpointRules);
					throw new ServiceException(ServiceError.BAD_REQUEST);
				}
			}
		}
	}
	
	@Override
	public Rule createRule(String id, Rule rule) throws ServiceException {
		//생성된 룰 저장 객체
		Rule createdRule = null;
		
		//기존 활성화 된 규칙이 있을 경우 해당 규칙을 비활성화 시킴
		List<Rule> rules = ruleRepository.findAll();
		
		for (Rule storedRule : rules) {
			//현재 활성화된 룰을 찾음
			if (storedRule.isActive()) {
				storedRule.setActive(false);
				//해당 룰을 업데이트
				ruleRepository.save(storedRule);
			}
		}
		
		logger.info("Check rule. [rule={}]", rule);
		
		if (rule != null) {
			if (validateRule(rule)) {
				//필수 필드 초기화
				rule.initMandatoryFieldValue(id);
				rule.setActive(true);
				
				//두번쨰 노드인 가치 관점 규칙 목록을 가져옴
				List<ViewpointRule> viewpointRules = rule.getViewpointRules();
				
				//null 조건 확인 (null일 경우 Excpetion 발생시킴)
				if (viewpointRules != null) {
					//최소 한 가지 이상의 가치 관점 규칙 정보가 필요 (0개일 경우 Exception 발생 시킴)
					if (!viewpointRules.isEmpty()) {
						//가치 관점 목록들을 순회하면서 가치 관점 규칙 정보를 생성하고 세번째 노드(카테고리 규칙)를 순회
						for (ViewpointRule viewpointRule : viewpointRules) {
							//두번째 노드인 가치 관점 규칙 아이디 생성
							String viewpointRuleId = uuidGenerator.generateUUID();
							
							//필수 필드 초기화
							viewpointRule.initMandatoryFieldValue(viewpointRuleId);
							//최상단 노드인 규칙 정보와 연관 관계를 설정
							viewpointRule.setRule(rule);
							//리스트 업데이트
							viewpointRules.set(viewpointRules.indexOf(viewpointRule), viewpointRule);
							
							//세번째 노드인 카테고리 규칙 목록을 가져옴
							List<CategoryRule> categoryRules = viewpointRule.getCategoryRules();
							
							//null 조건 확인 (null일 경우 Excpetion 발생시킴)
							if (categoryRules != null) {
								//최소 한 가지 이상의 카테고리 규칙 정보가 필요 (0개일 경우 Exception 발생 시킴)
								if (!categoryRules.isEmpty()) {
									//카테고리 규칙 목록들을 순회하면서 카테고리 규칙 정보를 생성하고 마지막 노드(가치 규칙)를 탐색
									for (CategoryRule categoryRule : categoryRules) {
										//카테고리 규칙 노드 아이디 생성
										String categoryRuleId = uuidGenerator.generateUUID();
										
										//필수 필드 초기화
										categoryRule.initMandatoryFieldValue(categoryRuleId);
										//두번째 노드인 가치 관점 규칙 정보와 연관관계를 설정
										categoryRule.setViewpointRule(viewpointRule);
										
										//가치 규칙 정보를 가져옴
										ValueRule valueRule = categoryRule.getValueRule();
										
										//null 조건 확인 (null일 경우 Excpetion 발생시킴)
										if (valueRule != null) {
											String valueRuleId = uuidGenerator.generateUUID();
											valueRule.initMandatoryFieldValue(valueRuleId);
											
											//이하 타입들은 기본으로 해당 값을 넣어줌 (불변)
											if (valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_ASC.getName()) 
													|| valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_DESC.getName())) {
												valueRule.setFirstRankCondition("true");
												valueRule.setSecondRankCondition("false");
											}
											
											//CATEGORY_RULE 엔티티와 VALUE_RULE 엔티티는 OneToOne 관계이므로 VALUE_RULE 엔티티 정보를 먼저 저장하고 CATEGORY_RULE과의 연관 관계를 맺음
											categoryRule.setValueRule(valueRule);
											
											//리스트 업데이트
											categoryRules.set(categoryRules.indexOf(categoryRule), categoryRule);
										} else {
											logger.error("valueRule is null. [valueRule={}]", valueRule);
											throw new ServiceException(ServiceError.BAD_REQUEST);
										}
									}
								} else {
									logger.error("categoryRules is empty. [categoryRules={}]", categoryRules);
									throw new ServiceException(ServiceError.BAD_REQUEST);
								}
							} else {
								logger.error("categoryRules is null. [categoryRules={}]", categoryRules);
								throw new ServiceException(ServiceError.BAD_REQUEST);
							}
						}
					} else {
						logger.error("viewpointRules is empty. [viewpointRules={}]", viewpointRules);
						throw new ServiceException(ServiceError.BAD_REQUEST);
					}
					
					//규칙 정보 저장
					//모든 규칙 엔티티의 연관관계 설정 후 Cascade에 의해 자식 규칙 엔티티들까지 모두 저장가능
					createdRule = ruleRepository.save(rule);
				} else {
					logger.error("viewpointRules is null. [viewpointRules={}]", viewpointRules);
					throw new ServiceException(ServiceError.BAD_REQUEST);
				}
			}
		}
		
		return createdRule;
	}

	@Override
	public Rule getRule(String id) throws ServiceException {
		return ruleRepository.findOne(id);
	}

	@Override
	public Rule getActiveRule() throws ServiceException {
		return ruleRepository.findByIsDefaultTrue();
	}
	
	@Override
	public List<Rule> getRules() throws ServiceException {
		return ruleRepository.findAll();
	}

	@Override
	public ViewpointRule getViewpointRule(String viewpointRuleId) throws ServiceException {
		return viewpointRuleRepository.findOne(viewpointRuleId);
	}

	@Override
	public List<ViewpointRule> getViewpointRules(String ruleId) throws ServiceException {
		return viewpointRuleRepository.findByRuleId(ruleId);
	}

	@Override
	public CategoryRule getCategoryRule(String categoryRuleId) throws ServiceException {
		return categoryRuleRepository.findOne(categoryRuleId);
	}

	@Override
	public List<CategoryRule> getCategoryRules(String viewpointRuleId) throws ServiceException {
		return categoryRuleRepository.findByViewpointRuleId(viewpointRuleId);
	}

	@Override
	public ValueRule getValueRule(String valueRuleId) throws ServiceException {
		return valueRuleRepository.findOne(valueRuleId);
	}

	@Override
	public Rule activateRule(String ruleId) throws ServiceException {
		Rule targetRule = null;
		List<Rule> rules = ruleRepository.findAll();
		
		for (Rule storedRule : rules) {
			//현재 활성화된 룰을 찾음
			if (storedRule.isActive()) {
				storedRule.setActive(false);
				//해당 룰을 업데이트
				ruleRepository.save(storedRule);
			}
			
			if (storedRule.equals(ruleId)) {
				targetRule = storedRule;
			}
		}
		
		if (targetRule != null) {
			targetRule.setActive(true);
			
			//해당 룰을 업데이트
			ruleRepository.save(targetRule);
		} else {
			throw new ServiceException(ServiceError.BAD_REQUEST);
		}
		
		return targetRule;
	}
	
	@Override
	public Rule modifyRule(String id, Rule rule) throws ServiceException {
		Rule searchedRule = getRule(id);
		Rule modifiedRule = null;
		
		if (searchedRule != null) {
			RuleCompareResult compareResult = compareRules(searchedRule, rule);
			
			if (compareResult.isStructureEqual()) {
				Date updateTime = new Date();
				
				//구조가 동일하므로 연관 관계와 업데이트 시간만 업데이트함
				for (ViewpointRule viewpointRule : rule.getViewpointRules()) {
					viewpointRule.setRule(rule);
					viewpointRule.setUpdateTime(updateTime);
					
					for (CategoryRule categoryRule : viewpointRule.getCategoryRules()) {
						categoryRule.setViewpointRule(viewpointRule);
						categoryRule.setUpdateTime(updateTime);
						
						ValueRule valueRule = categoryRule.getValueRule();
						valueRule.setUpdateTime(updateTime);
					}
				}
				
				//혹시 모르니 아이디를 받은 파라미터로 다시 설정 (필요없을 것 같다면 삭제)
				rule.setId(id);
				
				modifiedRule = ruleRepository.save(rule);
			//구조가 변경되었을 경우에는 기존 룰을 삭제하고 같은 아이디로 룰을 새롭게 생성
			} else {
				//기존 룰을 삭제
				ruleRepository.delete(searchedRule);
				
				//룰을 생성
				modifiedRule = createRule(id, rule);
			}
		} else {
			//Not found exception 처리
			throw new ServiceException(ServiceError.NOT_FOUND);
		}
		
		return modifiedRule;
	}
	
	private RuleCompareResult compareRules(Rule originRule, Rule rule) {
		RuleCompareResult ruleCompareResult = new RuleCompareResult(originRule, rule);

		boolean isStructureEqual = false;
		
		int originViewpointRuleTotal = ruleCompareResult.getOriginViewpointRuleMap().size();
		int originCategoryRuleTotal = ruleCompareResult.getOriginCategoryRuleMap().size();
		int originValueRuleTotal = ruleCompareResult.getOriginValueRuleMap().size();
		
		int viewpointRuleCompareCount = 0;
		int categoryRuleCompareCount = 0;
		int valueRuleCompareCount = 0;
		
		for (ViewpointRule viewpointRule : rule.getViewpointRules()) {
			String viewpointRuleId = viewpointRule.getId();
			
			if (ruleCompareResult.getOriginViewpointRuleMap().containsKey(viewpointRuleId)) {
				viewpointRuleCompareCount += 1;
				
				for (CategoryRule categoryRule : viewpointRule.getCategoryRules()) {
					if (ruleCompareResult.getOriginCategoryRuleMap().containsKey(categoryRule.getId())) {
						categoryRuleCompareCount += 1;
						
						ValueRule valueRule = categoryRule.getValueRule();
						
						if (ruleCompareResult.getOriginValueRuleMap().containsKey(valueRule.getId())) {
							valueRuleCompareCount += 1;
						} else {
							break;
						}
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
		
		if (viewpointRuleCompareCount == originViewpointRuleTotal) {
			if (categoryRuleCompareCount == originCategoryRuleTotal) {
				if (valueRuleCompareCount == originValueRuleTotal) {
					isStructureEqual = true;
				}
			}
		}
		
		ruleCompareResult.setStructureEqual(isStructureEqual);
		
		return ruleCompareResult;
	}
	
	private boolean validateRule(Rule rule) throws ServiceException {
		boolean isValid = false;
		
		//첫번째 노드인 규칙 null, isEmpty 체크
		if (rule != null) {
			//규칙의 최대 점수 (전체 룰로 동작)
			int ruleReflectionRateTotal = rule.getReflectionRateTotal();
			
			//규칙의 반영 비율 총합 (전체 룰로 동작)
			int ratingScoreMax = rule.getRatingScoreMax();
			
			//두번째 노드인 관점 규칙 가져옴
			List<ViewpointRule> viewpointRules = rule.getViewpointRules();
			
			//관점 규칙의 null, isEmpty 체크 (최소 한 개 이상의 관점 규칙이 필요함)
			if (viewpointRules != null) {
				if (!viewpointRules.isEmpty()) {
					//가치 관점 규칙 정보들의 가치 반영 비율 총합
					int viewpointRuleReflectionRateTotal = 0;
					
					//가치 관점 규칙들을 순회하면서 반영 비율 총합과 카테고리 관점 규칙들을 체크
					for (ViewpointRule viewpointRule : viewpointRules) {
						//카테고리 규칙 정보들의 가치 반영 비율 총합
						int categoryRuleReflectionRateTotal = 0;
						
						//타입 검사
						if (!(viewpointRule.getType().getName().equals(ViewpointType.CUSTOMER.getName()) 
								|| viewpointRule.getType().getName().equals(ViewpointType.STORE.getName())
								|| viewpointRule.getType().getName().equals(ViewpointType.USER_CUSTOM.getName()))) {
							throw new ServiceException(ServiceError.INVALID_VIEWPOINT_TYPE);
						}
						
						//가치 관점 규칙 정보들의 가치 반영 비율 총합이 동일 한지 확인하기 위하여 현재 순회 중인 노드의 가치 반영 비율을 더함
						viewpointRuleReflectionRateTotal += viewpointRule.getReflectionRate();
						
						//세번째 노드인 카테고리 규칙 목록을 가져옴
						List<CategoryRule> categoryRules = viewpointRule.getCategoryRules();
						
						//카테고리 규칙의 null, isEmpty 체크 (최소 한 개 이상의 카테고리 규칙이 필요함)
						if (categoryRules != null) {
							if (!categoryRules.isEmpty()) {
								for (CategoryRule categoryRule : categoryRules) {
									//타입 검사
									if (!(categoryRule.getType().getName().equals(CategoryType.VISIT_COUNT.getName()) 
											|| categoryRule.getType().getName().equals(CategoryType.LIKE_COUNT.getName())
											|| categoryRule.getType().getName().equals(CategoryType.USER_RATING.getName())
											|| categoryRule.getType().getName().equals(CategoryType.NEWEST_STORE_INFO.getName())
											|| categoryRule.getType().getName().equals(CategoryType.IS_ACTIVE_STORE_EVENT.getName()))) {
										throw new ServiceException(ServiceError.INVALID_CATEGORY_TYPE);
									}
									
									//카테고리 규칙 정보들의 가치 반영 비율 총합이 동일 한지 확인하기 위하여 현재 순회 중인 노드의 가치 반영 비율을 더함
									categoryRuleReflectionRateTotal += categoryRule.getReflectionRate();
									
									//가치 규칙 정보를 가져옴
									ValueRule valueRule = categoryRule.getValueRule();
									
									//카테고리 규칙의 null (필수 조건)
									if (valueRule != null) {
										//각 랭크의 조건식이 문제가 없는지 확인하기 위하여 조건식들을 가져옴
										String firstCond = valueRule.getFirstRankCondition();
										String secondCond = valueRule.getSecondRankCondition();
										String thirdCond = valueRule.getThirdRankCondition();
										
										String firstCondRegex = "^[0-9]+[~]?$";
										String generalRegex = "^[0-9]+[~]{1}$";
										
										//조건식 중 이상을 표현하는 ~를 제거한 뒤 해당 값을 정수 값으로 변환하여 각 조건식 간의 간격이 올바른지 검사
										int firstCondToInt = 0;
										int secondCondToInt = 0;
										int thirdCondToInt = 0;
										
										//각 랭크의 값(평가 값)이 문제가 없는지 확인하기 위하여 랭크 값을 가져옴
										int firstCondValue = valueRule.getFirstRankValue();
										int secondCondValue = valueRule.getSecondRankValue();
										int thirdCondValue = valueRule.getThirdRankValue();
										
										if (valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_ASC.getName()) 
												|| valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_DESC.getName())) {
											//해당 조건은 "true", "false"로 고정되므로 검사를 하지 않음
											//랭크 값(조건에 의해 평가받는 평가값)들의 간격 검사
											//ASC
											if (valueRule.getType().getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_ASC.getName())) {
												//랭크의 값들이 상점 가치 최대 점수 >= 두번째 조건 값 > 첫번째 조건 값  >= 0 을 만족하는지 검사
												if (!(ratingScoreMax >= secondCondValue && secondCondValue > firstCondValue && firstCondValue >= 0)) {
													throw new ServiceException(ServiceError.INVALID_VALUE_ORDER);
												}
											//DESC
											} else {
												//랭크의 값들이 상점 가치 최대 점수 >= 첫번째 조건 값 > 두번째 조건 값 >= 0 을 만족하는지 검사
												if (!(ratingScoreMax >= firstCondValue && firstCondValue > secondCondValue && secondCondValue >= 0)) {
													throw new ServiceException(ServiceError.INVALID_VALUE_ORDER);
												}
											} 
											
											//모든 조건 통과
											isValid = true;
										} else if (valueRule.getType().getName().equals(ValueType.THREE_COND_NUMERIC_AND_VALUE_ASC.getName()) 
												|| valueRule.getType().getName().equals(ValueType.THREE_COND_DATE_AND_VALUE_ASC.getName())
												|| valueRule.getType().getName().equals(ValueType.THREE_COND_NUMERIC_AND_VALUE_DESC.getName()) 
												|| valueRule.getType().getName().equals(ValueType.THREE_COND_DATE_AND_VALUE_DESC.getName())) {
											//조건식부터 검사
											//첫번째 조건식 검사 (첫번째 조건식은 'Number + ~'(Number 이상을 의미) 혹은 Number(해당 Number 만을 의미) 만 올수 있음)
											if (firstCond != null && !firstCond.equals("")) {
												if (firstCond.matches(firstCondRegex)) {
													if (firstCond.contains(CONDITION_OPERATION)) {
														firstCondToInt = Integer.parseInt(firstCond.replaceAll(CONDITION_OPERATION, ""));
													} else {
														firstCondToInt = Integer.parseInt(firstCond);
													}
												} else {
													throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
												}
											} else {
												throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
											}
											
											//두번째 조건식 검사 (두번째 조건식은 'Number + ~'(Number 이상을 의미) 만 올수 있음)
											if (secondCond != null && !secondCond.equals("")) {
												if (secondCond.matches(generalRegex)) {
													secondCondToInt = Integer.parseInt(secondCond.replaceAll(CONDITION_OPERATION, ""));
												} else {
													throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
												}
											} else {
												throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
											}
											
											//세번째 조건식 검사 (두번째 조건식은 'Number + ~'(Number 이상을 의미) 만 올수 있음)
											if (thirdCond != null && !thirdCond.equals("")) {
												if (thirdCond.matches(generalRegex)) {
													thirdCondToInt = Integer.parseInt(thirdCond.replaceAll(CONDITION_OPERATION, ""));
												} else {
													throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
												}
											} else {
												throw new ServiceException(ServiceError.INVALID_FIRST_CONDITION);
											}
											
											//각 조건들의 간격 검사
											if (!(firstCondToInt > secondCondToInt && secondCondToInt > thirdCondToInt && thirdCondToInt >= 0)) {
												throw new ServiceException(ServiceError.INVALID_CONDITIONS_RANGE);
											}
											
											//랭크 값(조건에 의해 평가받는 평가값)들의 간격 검사
											//ASC
											if (valueRule.getType().getName().equals(ValueType.THREE_COND_NUMERIC_AND_VALUE_ASC.getName()) 
													|| valueRule.getType().getName().equals(ValueType.THREE_COND_DATE_AND_VALUE_ASC.getName())) {
												//랭크의 값들이 상점 가치 최대 점수 >= 세번째 조건 값 > 두번째 조건 값 > 첫번째 조건 값  >= 0 을 만족하는지 검사
												if (!(ratingScoreMax >= thirdCondValue && thirdCondValue > secondCondValue && secondCondValue > firstCondValue && firstCondValue >= 0)) {
													logger.error("Check value order. [valueRule={}][ratingScoreMax={}][firstCondValue={}][secondCondValue={}][thirdCondValue={}]", 
															valueRule, ratingScoreMax, firstCondValue, secondCondValue, thirdCondValue);
													throw new ServiceException(ServiceError.INVALID_VALUE_ORDER);
												}
											//DESC
											} else {
												//랭크의 값들이 상점 가치 최대 점수 >= 첫번째 조건 값 > 두번째 조건 값 > 세번째 조건 값  >= 0 을 만족하는지 검사
												if (!(ratingScoreMax >= firstCondValue && firstCondValue > secondCondValue && secondCondValue > thirdCondValue && thirdCondValue >= 0)) {
													logger.error("Check value order. [ratingScoreMax={}][firstCondValue={}][secondCondValue={}][thirdCondValue={}]", 
															valueRule, ratingScoreMax, firstCondValue, secondCondValue, thirdCondValue);
													throw new ServiceException(ServiceError.INVALID_VALUE_ORDER);
												}
											} 
											
											//모든 조건 통과
											isValid = true;
										} else {
											throw new ServiceException(ServiceError.INVALID_VALUE_TYPE);
										}
									} else {
										throw new ServiceException(ServiceError.BAD_REQUEST);
									}
								}
								
								//카테고리 규칙 반영 비율 총합 확인
								if (ruleReflectionRateTotal != categoryRuleReflectionRateTotal) {
									logger.error("Check reflection-rate. [ruleReflectionRateTotal={}][categoryRuleReflectionRateTotal={}]",
											ruleReflectionRateTotal, categoryRuleReflectionRateTotal);
									
									throw new ServiceException(ServiceError.IS_NOT_EQUAL_REFLECTION_RATE_TOTAL_AND_CATEGORY_RULE_REFLECTION_RATE_TOTAL);
								}
							} else {
								throw new ServiceException(ServiceError.BAD_REQUEST);
							}
						} else {
							throw new ServiceException(ServiceError.BAD_REQUEST);
						}
					}
					
					//관점 규칙 반영 비율 총합 확인
					if (ruleReflectionRateTotal != viewpointRuleReflectionRateTotal) {
						logger.error("Check reflection-rate. [ruleReflectionRateTotal={}][viewpointRuleReflectionRateTotal={}]",
								ruleReflectionRateTotal, viewpointRuleReflectionRateTotal);
						
						throw new ServiceException(ServiceError.IS_NOT_EQUAL_REFLECTION_RATE_TOTAL_AND_VIEWPOINT_RULE_REFLECTION_RATE_TOTAL);
					}
				} else {
					throw new ServiceException(ServiceError.BAD_REQUEST);
				}
			} else {
				throw new ServiceException(ServiceError.BAD_REQUEST);
			}
			
			return isValid;
		} else {
			throw new ServiceException(ServiceError.BAD_REQUEST);
		}
	}
}
