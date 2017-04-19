package com.h4d1.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.h4d1.exception.ServiceException;
import com.h4d1.exception.type.ServiceError;
import com.h4d1.member.entity.Like;
import com.h4d1.member.entity.Member;
import com.h4d1.member.entity.UserRating;
import com.h4d1.member.repository.LikeRepository;
import com.h4d1.member.repository.MemberRepository;
import com.h4d1.member.repository.UserRatingRepository;
import com.h4d1.rule.entity.CategoryRule;
import com.h4d1.rule.entity.Rule;
import com.h4d1.rule.entity.ValueRule;
import com.h4d1.rule.entity.ViewpointRule;
import com.h4d1.rule.entity.type.CategoryType;
import com.h4d1.rule.entity.type.ValueType;
import com.h4d1.rule.entity.type.ViewpointType;
import com.h4d1.rule.respository.RuleRepository;
import com.h4d1.store.StoreService;
import com.h4d1.store.entity.Store;
import com.h4d1.store.entity.StoreAddress;
import com.h4d1.store.entity.StoreEvent;
import com.h4d1.store.entity.StorePhoto;
import com.h4d1.store.entity.StoreProfile;
import com.h4d1.store.repository.StoreRepository;
import com.h4d1.store.value.entity.StoreValue;
import com.h4d1.util.UuidGenerator;

@RestController
@RequestMapping(value="/tests")
public class TestController {
	
	@Autowired
	private UuidGenerator uuidGenerator;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private UserRatingRepository userRatingRepository;
	
	@Autowired
	private RuleRepository ruleRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(method=RequestMethod.POST, value="/data")
	public void createTestData() throws ServiceException {
		try {
			int memberCount = memberRepository.findAll().size();
			int storeCount = storeRepository.findAll().size();
			
			int memberIncrementSize = 1000;
			int storeIncrementSize = 100;
			int storePhotoIncrementSize = 5;
			int storeEventIncrementSize = 3;
			
			int requestCountLikeTotal = 10000;
			int requestCountIncrementVisitTotal = 10000;
			int requestCountUserRatingTotal = 10000;
			
			/* 규칙 상수 값 정의 */
			//가치 산정을 위한 최대 점수
			int ratingScoreMax = 3;
			//가치 산정을 위한 가치 반영 비율
			int reflectionRateTotal = 100;
			
			//고객 관점 가치 산정을 위한 반영 비율
			int customerViewpointRuleReflectionRate = 70;
			//고객 관점 타입
			ViewpointType customerViewpointType = ViewpointType.CUSTOMER;
			
			//조회 수 카테고리 가치 산정을 위한 반영 비율 
			int visitCategoryRuleReflectionRate = 30;
			//조회 수 카테고리 타입
			CategoryType visitCategoryType = CategoryType.VISIT_COUNT;
			//조회 수 랭크 타입 (산정 조건이 3단계를 나타내는 숫자(이상, 이하)이며 산정 값은 내림차순으로 값이 하향 3->2->1)
			ValueType visitValueType = ValueType.THREE_COND_NUMERIC_AND_VALUE_DESC;
			//조회 수 첫 번째 조건 (1000 이상)
			String visitFirstCond = "1000~";
			//조회 수 두 번째 조건 (200 이상)
			String visitSecondCond = "200~";
			//조회 수 세 번째 조건 (0 이상)
			String visitThirdCond = "0~";
			//조회 수 첫 번째 조건 산정 값
			int visitFirstValue = 3;
			//조회 수 두 번째 조건 산정 값
			int visitSecondValue = 2;
			//조회 수 세 번째 조건 산정 값
			int visitThirdValue = 1;
			
			//좋아요 수 카테고리 가치 산정을 위한 반영 비율
			int likeCategoryRuleReflectionRate = 40;
			//좋아요 수 카테고리 타입
			CategoryType likeCategoryType = CategoryType.LIKE_COUNT;
			//좋아요 수 랭크 타입 (산정 조건이 3단계를 나타내는 숫자(이상, 이하)이며 산정 값은 내림차순으로 값이 하향 3->2->1)
			ValueType likeValueType = ValueType.THREE_COND_NUMERIC_AND_VALUE_DESC;
			//좋아요 수 첫 번째 조건 (1000 이상)
			String likeFirstCond = "400~";
			//좋아요 수 두 번째 조건 (200 이상)
			String likeSecondCond = "110~";
			//좋아요 수 세 번째 조건 (0 이상)
			String likeThirdCond = "0~";
			//좋아요 수 첫 번째 조건 산정 값
			int likeFirstValue = 3;
			//좋아요 수 두 번째 조건 산정 값
			int likeSecondValue = 2;
			//좋아요 수 세 번째 조건 산정 값
			int likeThirdValue = 1;
			
			//사용자 평점 카테고리 가치 산정을 위한 반영 비율
			int userRatingCategoryRuleReflectionRate= 30;
			//사용자 평점 카테고리 타입
			CategoryType userRatingCategoryType = CategoryType.USER_RATING;
			//사용자 평점 랭크 타입 (산정 조건이 3단계를 나타내는 숫자(이상, 이하)이며 산정 값은 내림차순으로 값이 하향 3->2->1)
			ValueType userRatingRankType = ValueType.THREE_COND_NUMERIC_AND_VALUE_DESC;
			//사용자 평점 첫 번째 조건 (정확히 10)
			String userRatingFirstCond = "10";
			//사용자 평점 두 번째 조건 (5 이상)
			String userRatingSecondCond = "5~";
			//사용자 평점 세 번째 조건 (0 이상)
			String userRatingThirdCond = "0~";
			//사용자 평점 첫 번째 조건 산정 값
			int userRatingFirstValue = 3;
			//사용자 평점 두 번째 조건 산정 값
			int userRatingSecondValue = 2;
			//사용자 평점 세 번째 조건 산정 값
			int userRatingThirdValue = 1;
			
			
			//상점 관점 가치 산정을 위한 반영 비율
			int storeViewpointRuleReflectionRate = 30;
			//고객 관점 타입
			ViewpointType storeViewpointType = ViewpointType.STORE;
			
			//상점 정보 최신 카테고리 가치 산정을 위한 반영 비율 
			int newCategoryRuleReflectionRate = 55;
			//상점 정보 최신 카테고리 타입
			CategoryType newCategoryType = CategoryType.NEWEST_STORE_INFO;
			//상점 정보 최신 랭크 타입 (산정 조건이 3단계를 나타내는 날짜(~일 이상 ~일 이하)이며 산정 값은 오름차순으로 값이 상향 1->2->3)
			ValueType newRankType = ValueType.THREE_COND_DATE_AND_VALUE_ASC;
			//상점 정보 최신 첫 번째 조건 (8일 이상)
			String newFirstCond = "8~";
			//상점 정보 최신 두 번째 조건 (2일 이상)
			String newSecondCond = "2~";
			//상점 정보 최신 세 번째 조건 (0 이상)
			String newThirdCond = "0~";
			//상점 정보 최신 첫 번째 조건 산정 값
			int newFirstValue = 1;
			//상점 정보 최신 두 번째 조건 산정 값
			int newSecondValue = 2;
			//상점 정보 최신 세 번째 조건 산정 값
			int newThirdValue = 3;
			
			//이벤트 중 카테고리 가치 산정을 위한 반영 비율 
			int eventCategoryRuleReflectionRate = 45;
			//이벤트 중 카테고리 타입
			CategoryType eventCategoryType = CategoryType.IS_ACTIVE_STORE_EVENT;
			//이벤트 중 랭크 타입 (산정 조건이 2단계를 나타내는 bool(O, X)이며 산정 값은 내림차순으로 값이 하향 3->2->3)
			ValueType eventRankType = ValueType.TWO_COND_BOOL_AND_VALUE_DESC;
			//이벤트 중 첫 번째 조건 (O)
			String eventFirstCond = "true";
			//이벤트 중 두 번째 조건 (X)
			String eventSecondCond = "false";
			//이벤트 중 첫 번째 조건 산정 값
			int eventFirstValue = 3;
			//이벤트 중 두 번째 조건 산정 값
			int eventSecondValue = 0;
			
			
			//사용자 정보 생성 혹은 추가
			for (int index = 0; index < memberIncrementSize; index++) {
				String id = uuidGenerator.generateUUID();
				String email = "user" + (index + 1) + "@test.com";
				String name = "user" + (index + 1);
				
				Member member = new Member(id);
				member.setEmail(email);
				member.setName(name);
				
				memberRepository.save(member);
			}
			
			//상점 정보 생성
			for (int storeIndex = 0; storeIndex < storeIncrementSize; storeIndex++) {
				String storeId = uuidGenerator.generateUUID();
				
				Store store = new Store();
				store.initMandatoryFieldValue(storeId);
				store.setName("상점" + (storeIndex + 1));
				
				//상점 사진 정보 생성
				for (int storePhotoIndex = 0; storePhotoIndex < storePhotoIncrementSize; storePhotoIndex++) {
					String storePhotoId = uuidGenerator.generateUUID();
					
					StorePhoto storePhoto = new StorePhoto();
					storePhoto.initMandatoryFieldValue(storePhotoId);
					storePhoto.setUri("/photos/" + storePhotoId + "/file");
					
					if (storePhotoIndex == 0) {
						storePhoto.setSelfPictured(true);
						storePhoto.setDefault(true);
					}
					
					storePhoto.setStore(store);
					
					store.addStorePhoto(storePhoto);
				}
				
				//상점 이벤트 정보 생성
				for (int storeEventIndex = 0; storeEventIndex < storeEventIncrementSize; storeEventIndex++) {
					String storeEventId = uuidGenerator.generateUUID();
					
					StoreEvent storeEvent = new StoreEvent();
					storeEvent.initMandatoryFieldValue(storeEventId);
					storeEvent.setDescription("이벤트" + (storeEventIndex  + 1) + " 설명");
					
					Date startTime = new Date();
					
					storeEvent.setStartTime(startTime);
					storeEvent.setEndTime(startTime);
					
					if (storeEventIndex == (storeEventIncrementSize - 1)) {
						//짝수일 때만
						if ((storeIndex % 2) == 0 ) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							calendar.add(Calendar.DATE, 10);
							
							storeEvent.setStartTime(startTime);
							storeEvent.setEndTime(new Date(calendar.getTime().getTime()));
						} 
					}
					
					storeEvent.setStore(store);
					
					store.addStoreEvent(storeEvent);
				}
				
				//상점 주소 정보 생성
				String storeAddressId = uuidGenerator.generateUUID();
				
				StoreAddress storeAddress = new StoreAddress();
				storeAddress.initMandatoryFieldValue(storeAddressId);
				storeAddress.setStreet("서울특별시 특별구 특별로 1000");
				storeAddress.setDetail("특별 빌딩 1-" + (storeIndex + 1) + "호");
				
				//상점 세부 정보 생성
				String storeProfileId = uuidGenerator.generateUUID();
				
				StoreProfile storeProfile = new StoreProfile();
				storeProfile.initMandatoryFieldValue(storeProfileId);
				storeProfile.setDescription("상점" + (storeIndex + 1) + " 설명");
				storeProfile.setPhoneNumber("+82-400-" + (storeIndex + 1));
				storeProfile.setStoreAddress(storeAddress);
				
				store.setStoreProfile(storeProfile);
				
				//상점 가치 정보 생성
				String storeValueId = uuidGenerator.generateUUID();
				
				StoreValue storeValue = new StoreValue();
				storeValue.initMandatoryFieldValue(storeValueId);
				
				store.setStoreValue(storeValue);
				
				storeRepository.save(store);
			}
			
			//저장된 상점 전체 정보
			List<Store> storedStores = storeRepository.findAll();
			
			//저장된 사용자 전체 정보
			List<Member> storedMembers = memberRepository.findAll();
			
			//상점 수 갱신
			storeCount = storedStores.size();
			
			//갱신된 사용자 수를 가져옴
			memberCount = storedMembers.size();
			
			for (int requestLikeIndex = 0; requestLikeIndex < requestCountLikeTotal; requestLikeIndex++) {
				 int storeRandomIndex = (int) (Math.random() * storeCount);
				 int memberRandomIndex = (int) (Math.random() * memberCount);
				 
				 Store targetStore = storedStores.get(storeRandomIndex);
				 Member targetMember = storedMembers.get(memberRandomIndex);
				 
				 Like searchedLike = likeRepository.findByMemberIdAndStoreId(targetMember.getId(), targetStore.getId());
				 
				 if (searchedLike == null) {
					 String likeId = uuidGenerator.generateUUID();
					 
					 Like like = new Like(likeId);
					 like.setMember(targetMember);
					 like.setStore(targetStore);
					 
					 likeRepository.save(like);
					 
					 targetStore.incrementLikeCount();
					 
					 storeRepository.save(targetStore);
				 }
			}
			
			for (int requestIncrementVisitIndex = 0; requestIncrementVisitIndex < requestCountIncrementVisitTotal; requestIncrementVisitIndex++) {
				 int storeRandomIndex = (int) (Math.random() * storeCount);
				 
				 Store targetStore = storedStores.get(storeRandomIndex);
				 targetStore.incrementVisitCount();
				 
				 storeRepository.save(targetStore);
			}
			
			for (int requestUserRatingIndex = 0; requestUserRatingIndex < requestCountUserRatingTotal; requestUserRatingIndex++) {
				 int storeRating = (int) (Math.random() * 10) + 1;
				
				 int storeRandomIndex = (int) (Math.random() * storeCount);
				 int memberRandomIndex = (int) (Math.random() * memberCount);
				 
				 Store targetStore = storedStores.get(storeRandomIndex);
				 Member targetMember = storedMembers.get(memberRandomIndex);
				 
				 UserRating searchedUserRating = userRatingRepository.findByMemberIdAndStoreId(targetMember.getId(), targetStore.getId());
				 
				 if (searchedUserRating == null) {
					 String userRatingId = uuidGenerator.generateUUID();
					 
					 UserRating userRating = new UserRating(userRatingId);
					 userRating.setMember(targetMember);
					 userRating.setStore(targetStore);
					 userRating.setRatingScore(storeRating);
					 
					 userRatingRepository.save(userRating);
					 
					 List<UserRating> userRatings = userRatingRepository.findByStoreId(targetStore.getId());
					 
					 float userRatingAverage = 0f;
					 float userRatingTotal = 0f;
					 float userRatingCount = userRatings.size();
					 
					 for (UserRating storedUserRating : userRatings) {
						 userRatingTotal += storedUserRating.getRatingScore();
					 }
					 
					 userRatingAverage = userRatingTotal / userRatingCount;
					 
					 targetStore.setUserRatingAverage(userRatingAverage);
					 
					 storeRepository.save(targetStore);
				 }
			}
			
			//규칙 정보 생성
			String ruleId = uuidGenerator.generateUUID();
			
			Rule rule = new Rule();
			rule.initMandatoryFieldValue(ruleId);
			rule.setActive(true);
			//가치 산정을 위한 최대 점수 3점
			rule.setRatingScoreMax(ratingScoreMax);
			//가치 상정을 위한 가치 반영 비율 100
			rule.setReflectionRateTotal(reflectionRateTotal);
			
			//고객 관점 규칙 생성
			String customerViewpointRuleId = uuidGenerator.generateUUID();
			
			ViewpointRule customerViewpointRule = new ViewpointRule();
			customerViewpointRule.initMandatoryFieldValue(customerViewpointRuleId);
			customerViewpointRule.setType(customerViewpointType);
			customerViewpointRule.setReflectionRate(customerViewpointRuleReflectionRate);
			customerViewpointRule.setRule(rule);
			
			rule.addViewpointRule(customerViewpointRule);
			
			//조회 수 카테고리 규칙 생성
			String visitCategoryRuleId = uuidGenerator.generateUUID();
			
			CategoryRule visitCategoryRule = new CategoryRule();
			visitCategoryRule.initMandatoryFieldValue(visitCategoryRuleId);
			visitCategoryRule.setType(visitCategoryType);
			visitCategoryRule.setReflectionRate(visitCategoryRuleReflectionRate);
			visitCategoryRule.setViewpointRule(customerViewpointRule);
			
			customerViewpointRule.addCategoryRule(visitCategoryRule);
			
			//조회 수 가치 규칙 생성
			String visitValueRuleId = uuidGenerator.generateUUID();
			
			ValueRule visitValueRule = new ValueRule();
			visitValueRule.initMandatoryFieldValue(visitValueRuleId);
			visitValueRule.setType(visitValueType);
			visitValueRule.setFirstRankCondition(visitFirstCond);
			visitValueRule.setSecondRankCondition(visitSecondCond);
			visitValueRule.setThirdRankCondition(visitThirdCond);
			visitValueRule.setFirstRankValue(visitFirstValue);
			visitValueRule.setSecondRankValue(visitSecondValue);
			visitValueRule.setThirdRankValue(visitThirdValue);
			
			visitCategoryRule.setValueRule(visitValueRule);
			
			//좋아요 수 카테고리 규칙 생성
			String likeCategoryRuleId = uuidGenerator.generateUUID();
			
			CategoryRule likeCategoryRule = new CategoryRule();
			likeCategoryRule.initMandatoryFieldValue(likeCategoryRuleId);
			likeCategoryRule.setType(likeCategoryType);
			likeCategoryRule.setReflectionRate(likeCategoryRuleReflectionRate);
			likeCategoryRule.setViewpointRule(customerViewpointRule);
			
			customerViewpointRule.addCategoryRule(likeCategoryRule);
			
			//좋아요 가치 규칙 생성
			String likeValueRuleId = uuidGenerator.generateUUID();
			
			ValueRule likeValueRule = new ValueRule();
			likeValueRule.initMandatoryFieldValue(likeValueRuleId);
			likeValueRule.setType(likeValueType);
			likeValueRule.setFirstRankCondition(likeFirstCond);
			likeValueRule.setSecondRankCondition(likeSecondCond);
			likeValueRule.setThirdRankCondition(likeThirdCond);
			likeValueRule.setFirstRankValue(likeFirstValue);
			likeValueRule.setSecondRankValue(likeSecondValue);
			likeValueRule.setThirdRankValue(likeThirdValue);
			
			likeCategoryRule.setValueRule(likeValueRule);
			
			//사용자 평점 카테고리 규칙 생성
			String userRatingCategoryRuleId = uuidGenerator.generateUUID();
			
			CategoryRule userRatingCategoryRule = new CategoryRule();
			userRatingCategoryRule.initMandatoryFieldValue(userRatingCategoryRuleId);
			userRatingCategoryRule.setType(userRatingCategoryType);
			userRatingCategoryRule.setReflectionRate(userRatingCategoryRuleReflectionRate);
			userRatingCategoryRule.setViewpointRule(customerViewpointRule);
			
			customerViewpointRule.addCategoryRule(userRatingCategoryRule);
			
			//사용자 평점 규칙 생성
			String userRatingValueRuleId = uuidGenerator.generateUUID();
			
			ValueRule userRatingValueRule = new ValueRule();
			userRatingValueRule.initMandatoryFieldValue(userRatingValueRuleId);
			userRatingValueRule.setType(userRatingRankType);
			userRatingValueRule.setFirstRankCondition(userRatingFirstCond);
			userRatingValueRule.setSecondRankCondition(userRatingSecondCond);
			userRatingValueRule.setThirdRankCondition(userRatingThirdCond);
			userRatingValueRule.setFirstRankValue(userRatingFirstValue);
			userRatingValueRule.setSecondRankValue(userRatingSecondValue);
			userRatingValueRule.setThirdRankValue(userRatingThirdValue);
			
			userRatingCategoryRule.setValueRule(userRatingValueRule);
			
			
			//상점 관점 규칙 생성
			String storeViewpointRuleId = uuidGenerator.generateUUID();
			
			ViewpointRule storeViewpointRule = new ViewpointRule();
			storeViewpointRule.initMandatoryFieldValue(storeViewpointRuleId);
			storeViewpointRule.setType(storeViewpointType);
			storeViewpointRule.setReflectionRate(storeViewpointRuleReflectionRate);
			storeViewpointRule.setRule(rule);
			
			rule.addViewpointRule(storeViewpointRule);
			
			//정보의 최신성 카테고리 규칙 생성
			String newCategoryRuleId = uuidGenerator.generateUUID();
			
			CategoryRule newCategoryRule = new CategoryRule();
			newCategoryRule.initMandatoryFieldValue(newCategoryRuleId);
			newCategoryRule.setType(newCategoryType);
			newCategoryRule.setReflectionRate(newCategoryRuleReflectionRate);
			newCategoryRule.setViewpointRule(storeViewpointRule);
			
			storeViewpointRule.addCategoryRule(newCategoryRule);
			
			//정보의 최신성 규칙 생성
			String newValueRuleId = uuidGenerator.generateUUID();
			
			ValueRule newValueRule = new ValueRule();
			newValueRule.initMandatoryFieldValue(newValueRuleId);
			newValueRule.setType(newRankType);
			newValueRule.setFirstRankCondition(newFirstCond);
			newValueRule.setSecondRankCondition(newSecondCond);
			newValueRule.setThirdRankCondition(newThirdCond);
			newValueRule.setFirstRankValue(newFirstValue);
			newValueRule.setSecondRankValue(newSecondValue);
			newValueRule.setThirdRankValue(newThirdValue);
			
			newCategoryRule.setValueRule(newValueRule);
			
			//정보의 최신성 카테고리 규칙 생성
			String eventCategoryRuleId = uuidGenerator.generateUUID();
			
			CategoryRule eventCategoryRule = new CategoryRule();
			eventCategoryRule.initMandatoryFieldValue(eventCategoryRuleId);
			eventCategoryRule.setType(eventCategoryType);
			eventCategoryRule.setReflectionRate(eventCategoryRuleReflectionRate);
			eventCategoryRule.setViewpointRule(storeViewpointRule);
			
			storeViewpointRule.addCategoryRule(eventCategoryRule);
			
			//정보의 최신성 규칙 생성
			String eventValueRuleId = uuidGenerator.generateUUID();
			
			ValueRule eventValueRule = new ValueRule();
			eventValueRule.initMandatoryFieldValue(eventValueRuleId);
			eventValueRule.setType(eventRankType);
			eventValueRule.setFirstRankCondition(eventFirstCond);
			eventValueRule.setSecondRankCondition(eventSecondCond);
			eventValueRule.setFirstRankValue(eventFirstValue);
			eventValueRule.setSecondRankValue(eventSecondValue);
			
			eventCategoryRule.setValueRule(eventValueRule);
			
			ruleRepository.save(rule);
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/photo")
	public void createStorePhoto(@RequestParam(required=true) String storeId) throws ServiceException {
		try {
			logger.info("Check request-param. [storeId={}]", storeId);
			
			Store store = storeService.getStore(storeId);
			
			String storePhotoId = uuidGenerator.generateUUID();
			
			StorePhoto storePhoto = new StorePhoto();
			storePhoto.initMandatoryFieldValue(storePhotoId);
			storePhoto.setUri("/photos/" + storePhotoId);
			storePhoto.setDefault(true);
			storePhoto.setSelfPictured(true);
			storePhoto.setStore(store);
			
			store.addStorePhoto(storePhoto);
			
			storeRepository.save(store);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured. Handle exception.", e);
			throw new ServiceException(ServiceError.UNKNOWN_ERROR);
		}
	}
}
