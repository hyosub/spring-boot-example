package com.h4d1.store.value.manager;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.h4d1.exception.ServiceException;
import com.h4d1.rule.RuleService;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.ViewpointRule;
import com.h4d1.rule.entity.type.CategoryType;
import com.h4d1.rule.entity.type.ValueType;
import com.h4d1.store.StoreService;
import com.h4d1.store.entity.Store;
import com.h4d1.store.repository.StoreRepository;
import com.h4d1.store.value.StoreValueService;
import com.h4d1.store.value.entity.StoreValue;

@Service
@Transactional
public class StoreValueManager implements StoreValueService {

	/**
	 * 상점 정보를 관리하는 서비스
	 */
	@Autowired
	private StoreService storeService;
	
	/**
	 * 규칙 정보를 관리하는 서비스
	 */
	@Autowired
	private RuleService ruleService;
	
	/**
	 * 상점 정보를 관리하는 JpaRepository
	 */
	@Autowired
	private StoreRepository storeRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreValueManager.class);
	
	@Override
	public void evaluateStoreValue() throws ServiceException {
		List<Store> stores = storeService.getAllStores();
		
		Rule activeRule = ruleService.getActiveRule();
		
		//float entireReflectionRate = activeRule.getReflectionRateTotal() * 1.0f;
		
		for (Store store : stores) {
			//상점 가치를 가져옴
			StoreValue storeValue = store.getStoreValue();
			
			List<ViewpointRule> viewpointRules = activeRule.getViewpointRules();
			
			//산정된 각 관점 가치들을 더한 값. 최종 상점 평가 가치
			float finalEvaluatedValue = 0;
			
			for (ViewpointRule viewpointRule : viewpointRules) {
				List<CategoryRule> categoryRules = viewpointRule.getCategoryRules();
				
				//관점 가치 산정 비율
				int viewPointReflectionRate = viewpointRule.getReflectionRate();
				
				//하나의 관점의 각 카테고리 평가 점수 총합
				float categoryEvaluatedValueTotal = 0;
				//하나의 관점 가치 산정 값 (산정 비율 곱함)
				float viewpointEvaluatedValue = 0;
				
				for (CategoryRule categoryRule : categoryRules) {
					CategoryType categoryType = categoryRule.getType();
					int categoryReflectionRate = categoryRule.getReflectionRate();
					
					ValueRule valueRule = categoryRule.getValueRule();
					
					ValueType valueType = valueRule.getType();
					
					String firstCond = valueRule.getFirstRankCondition();
					String secondCond = valueRule.getSecondRankCondition();
					String thirdCond = valueRule.getThirdRankCondition();
					
					int firstValue = valueRule.getFirstRankValue();
					int secondValue = valueRule.getSecondRankValue();
					int thirdValue = valueRule.getThirdRankValue();
					
					int firstCondToInt = 0;
					int secondCondToInt = 0;
					int thirdCondToInt = 0;
					
					boolean firstCondToBoolean = false;
					boolean secondCondToBoolean = false;
					
					boolean isFirstCondGreaterThen = false;
					
					//조건에 의해 평가된 점수
					float evaluatedValue = 0;
					
					//연산자 조건을 랭크 타입에 맞게 변환
					if (valueType.getName().equals(ValueType.THREE_COND_NUMERIC_AND_VALUE_ASC.getName())
							|| valueType.getName().equals(ValueType.THREE_COND_NUMERIC_AND_VALUE_DESC.getName())
							||valueType.getName().equals(ValueType.THREE_COND_DATE_AND_VALUE_ASC.getName())
							|| valueType.getName().equals(ValueType.THREE_COND_DATE_AND_VALUE_DESC.getName())) {
						
						if (firstCond.contains("~")) {
							firstCondToInt = Integer.parseInt(firstCond.replaceAll("~", ""));
							isFirstCondGreaterThen = true;
						} else {
							firstCondToInt = Integer.parseInt(firstCond);
						}
						
						secondCondToInt = Integer.parseInt(secondCond.replaceAll("~", ""));
						thirdCondToInt = Integer.parseInt(thirdCond.replaceAll("~", ""));
					} else if (valueType.getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_ASC.getName())
							|| valueType.getName().equals(ValueType.TWO_COND_BOOL_AND_VALUE_DESC.getName())) {
						firstCondToBoolean = Boolean.parseBoolean(firstCond);
						secondCondToBoolean = Boolean.parseBoolean(secondCond);
					} else {
						//TODO Exception (없는 랭크 타입 Exc)
					}
					
					//카테고리 타입에 맞게 평가 점수를 계산
					if (categoryType.getName().equals(CategoryType.VISIT_COUNT.getName())) {
						//상점의 총 조회 수
						int visitCount = store.getVisitCount();
						
						evaluatedValue = evaluateThreeCondNumericRankTypeValue(visitCount, isFirstCondGreaterThen,
								firstCondToInt, secondCondToInt, thirdCondToInt, categoryReflectionRate, firstValue,
								secondValue, thirdValue);
					} else if (categoryType.getName().equals(CategoryType.LIKE_COUNT.getName())) {
						//상점의 총 좋아요 수
						int likeCount = store.getLikeCount();
						
						evaluatedValue = evaluateThreeCondNumericRankTypeValue(likeCount, isFirstCondGreaterThen,
								firstCondToInt, secondCondToInt, thirdCondToInt, categoryReflectionRate, firstValue,
								secondValue, thirdValue);
					} else if (categoryType.getName().equals(CategoryType.USER_RATING.getName())) {
						//상점의 평균 점수
						float userRatingAverage = store.getUserRatingAverage();
								
						evaluatedValue = evaluateThreeCondNumericRankTypeValue(userRatingAverage, isFirstCondGreaterThen,
								firstCondToInt, secondCondToInt, thirdCondToInt, categoryReflectionRate, firstValue,
								secondValue, thirdValue);
					} else if (categoryType.getName().equals(CategoryType.NEWEST_STORE_INFO.getName())) {
						Date lastUpdateTime = store.getUpdateTime();

						evaluatedValue = evaluateThreeCondDateRankTypeValue(lastUpdateTime, isFirstCondGreaterThen,
								firstCondToInt, secondCondToInt, thirdCondToInt, categoryReflectionRate, firstValue,
								secondValue, thirdValue);
					} else if (categoryType.getName().equals(CategoryType.IS_ACTIVE_STORE_EVENT.getName())) {
						boolean isEvent = store.isEvent();
						
						evaluatedValue = evaluateTwoCondBooleanRankTypeValue(isEvent, firstCondToBoolean, secondCondToBoolean,
								categoryReflectionRate, firstValue, secondValue);
					}
					
					categoryEvaluatedValueTotal += evaluatedValue;
					
					logger.info("Total categoryValue is updated. [categoryEvaluatedValueTotal={}]", categoryEvaluatedValueTotal);
				}
				
				//관점 가치 산정
				viewpointEvaluatedValue = categoryEvaluatedValueTotal * (viewPointReflectionRate / 100f);
				
				//최종 평가 가치에 산정된 관점 가치 값을 더함
				finalEvaluatedValue += viewpointEvaluatedValue;
			}
			
			//소수점 2자리 이하 수는 버림
			finalEvaluatedValue = Float.parseFloat(String.format("%.2f", finalEvaluatedValue));
			
			//산정된 최종 평가 가치와 가치 평가가 되었다는 표시 및 업데이트 시간 갱신
			storeValue.setValue(finalEvaluatedValue);
			storeValue.setEvaluated(true);
			storeValue.setUpdateTime(new Date());
			
			storeRepository.save(store);
		}
	}
	
	private float evaluateThreeCondNumericRankTypeValue(float evaluateTarget, 
			boolean isFirstCondGreaterThen, int firstCondToInt, int secondCondToInt, int thirdCondToInt, 
			int categoryReflectionRate, int firstValue, int secondValue, int thirdValue) {
		float evaluatedValue = 0;
		
		boolean isMatchedFirstCond = false;
		boolean isMatchedSecondCond = false;
		boolean isMatchedThirdCond = false;
		
		if (isFirstCondGreaterThen) {
			if (evaluateTarget >= firstCondToInt) {
				isMatchedFirstCond = true;
			} else if (evaluateTarget >= secondCondToInt) {
				isMatchedSecondCond = true;
			} else if (evaluateTarget >= thirdCondToInt) {
				isMatchedThirdCond = true;
			}
		} else {
			if (evaluateTarget == firstCondToInt) {
				isMatchedFirstCond = true;
			} else if (evaluateTarget >= secondCondToInt) {
				isMatchedSecondCond = true;
			} else if (evaluateTarget >= thirdCondToInt) {
				isMatchedThirdCond = true;
			}
		}
		
		logger.info("Check condition. [isMatchedFirstCond={}][isMatchedSecondCond={}][isMatchedThirdCond={}]", 
				isMatchedFirstCond, isMatchedSecondCond, isMatchedThirdCond);
		
		if (isMatchedFirstCond) {
			logger.info("FirstCond is matched. [firstValue={}][categoryReflectionRate={}]", firstValue, categoryReflectionRate);
			
			evaluatedValue = firstValue * (categoryReflectionRate / 100f);
		} else if (isMatchedSecondCond) {
			logger.info("SecondCond is matched. [secondValue={}][categoryReflectionRate={}]", secondValue, categoryReflectionRate);
			
			evaluatedValue = secondValue * (categoryReflectionRate / 100f);
		} else if (isMatchedThirdCond) {
			logger.info("ThirdCond is matched. [thirdValue={}][categoryReflectionRate={}]", thirdValue, categoryReflectionRate);
			
			evaluatedValue = thirdValue * (categoryReflectionRate / 100f);
		} else {
			logger.error("This value cannot to evaluate. Check value. [evaluateTarget={}]", evaluateTarget);
		}
		
		logger.info("Value is evaluated. [evaluatedValue={}]", evaluatedValue);
		
		return evaluatedValue;
	}
	
	private float evaluateThreeCondDateRankTypeValue(Date evaluateTarget, 
			boolean isFirstCondGreaterThen, int firstCondToInt, int secondCondToInt, int thirdCondToInt, 
			int categoryReflectionRate, int firstValue, int secondValue, int thirdValue) {
		float evaluatedValue = 0;
		
		boolean isMatchedFirstCond = false;
		boolean isMatchedSecondCond = false;
		boolean isMatchedThirdCond = false;
		
		Date currentTime = new Date();
		
		//시간 차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
		long diff = currentTime.getTime() - evaluateTarget.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		if (isFirstCondGreaterThen) {
			if (diffDays >= firstCondToInt) {
				isMatchedFirstCond = true;
			} else if (diffDays >= secondCondToInt) {
				isMatchedSecondCond = true;
			} else if (diffDays >= thirdCondToInt) {
				isMatchedThirdCond = true;
			}
		} else {
			if (diffDays == firstCondToInt) {
				isMatchedFirstCond = true;
			} else if (diffDays >= secondCondToInt) {
				isMatchedSecondCond = true;
			} else if (diffDays >= thirdCondToInt) {
				isMatchedThirdCond = true;
			}
		}
		
		if (isMatchedFirstCond) {
			evaluatedValue = firstValue * (categoryReflectionRate / 100f);
		} else if (isMatchedSecondCond) {
			evaluatedValue = secondValue * (categoryReflectionRate / 100f);
		} else if (isMatchedThirdCond) {
			evaluatedValue = thirdValue * (categoryReflectionRate / 100f);
		} else {
			logger.error("This value cannot to evaluate. Check value. [evaluateTarget={}]", evaluateTarget);
		}
		
		logger.info("Value is evaluated. [evaluatedValue={}]", evaluatedValue);
		
		return evaluatedValue;
	}
	
	private float evaluateTwoCondBooleanRankTypeValue(boolean evaluateTarget, boolean firstCondToBoolean, boolean secondCondToBoolean,
			int categoryReflectionRate, int firstValue, int secondValue) {
		float evaluatedValue = 0;
		
		if (firstCondToBoolean == evaluateTarget) {
			evaluatedValue = firstValue * (categoryReflectionRate / 100f);
		} else if (secondCondToBoolean == evaluateTarget) {
			evaluatedValue = secondValue * (categoryReflectionRate / 100f);
		}
		
		logger.info("Value is evaluated. [evaluatedValue={}]", evaluatedValue);
		
		return evaluatedValue;
	}
}
