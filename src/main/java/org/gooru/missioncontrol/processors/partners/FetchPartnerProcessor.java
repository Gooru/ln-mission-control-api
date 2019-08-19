
package org.gooru.missioncontrol.processors.partners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.constants.AppConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.dbhelpers.CountryModel;
import org.gooru.missioncontrol.processors.dbhelpers.DBHelperService;
import org.gooru.missioncontrol.processors.dbhelpers.StateModel;
import org.gooru.missioncontrol.processors.dbhelpers.SubjectCategoryModel;
import org.gooru.missioncontrol.processors.dbhelpers.SubjectModel;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class FetchPartnerProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(FetchPartnerProcessor.class);

  private final static PartnersService PARTNERS_SERVICE =
      new PartnersService(DBICreator.getDbiForDefaultDS());

  private final static DBHelperService DBHELPER_SERVICE =
      new DBHelperService(DBICreator.getDbiForDefaultDS());

  private final static PartnersDataService PARTNERS_DATA_SERVICE =
      new PartnersDataService(DBICreator.getDbiForDatascopeDB());

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private Map<Long, CountryModel> countriesMap = new HashMap<>();
  private Map<Long, StateModel> statesMap = new HashMap<>();


  public FetchPartnerProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    LOGGER.debug("fetching partner data");

    JsonObject requestBody = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
    String strRefresh = requestBody.getString("refresh", null);
    String partnerIdAsString = requestBody.getString("partnerId", null);
    Boolean isRefresh =
        (strRefresh == null || strRefresh.isEmpty()) ? false : Boolean.valueOf(strRefresh);
    Long partnerId = Long.valueOf(partnerIdAsString);

    if (!isRefresh) {
      String cacheData = PARTNERS_SERVICE.fetchFromCacheByPartnerId(partnerId,
          AppConstants.CACHE_TYPE_PARTNER_STATS_API);
      if (cacheData != null) {
        JsonObject response = new JsonObject(cacheData);
        result.complete(MessageResponseFactory.createGetResponse(response));
        LOGGER.debug("sending response from cache");
        return result;
      } else {
        LOGGER.debug("no data found in cache");
      }
    }

    LOGGER.debug("fetching data from data store");
    JsonObject response = fetchPartnerData(partnerId);
    PARTNERS_SERVICE.updateCacheByPartnerId(partnerId, response.toString(),
        AppConstants.CACHE_TYPE_PARTNER_STATS_API);

    LOGGER.debug("sending fresh response from DB");
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

  private JsonObject fetchPartnerData(Long partnerId) {
    PartnerModel partner = PARTNERS_SERVICE.fetchPartner(partnerId);
    JsonObject response = new JsonObject();
    if (partner != null) {
      countriesMap =
          partner.getCountries() != null ? DBHELPER_SERVICE.fetchCountries(partner.getCountries())
              : null;
      statesMap =
          partner.getStates() != null ? DBHELPER_SERVICE.fetchStates(partner.getStates()) : null;

      Map<String, StatsByTenantPartnerModel> statsByClientMap =
          PARTNERS_DATA_SERVICE.fetchStatsByTenantPartner();


      LOGGER.debug("preparing response");
      response = prepareResponse(partner, statsByClientMap);
    }
    return response;
  }

  private JsonObject prepareResponse(PartnerModel partner,
      Map<String, StatsByTenantPartnerModel> statsByClientMap) {
    JsonObject partnerJson = new JsonObject();
    partnerJson.put("partner_id", partner.getId());
    partnerJson.put("partner_name", partner.getOrganizationName());
    partnerJson.put("website", partner.getWebsite());
    partnerJson.put("logo", partner.getLogo());
    partnerJson.put("countries", resolveCountries(partner.getCountries()));
    partnerJson.put("states", resolveStates(partner.getStates()));
    partnerJson.put("videos", reslove(partner.getVideos()));
    partnerJson.put("images", reslove(partner.getImages()));
    UUID clientId = (partner.getPartner() != null) ? partner.getPartner() : partner.getTenant();
    if (clientId != null) {
      StatsByTenantPartnerModel statByClient = statsByClientMap.get(clientId.toString());
      if (statByClient != null) {
        Long totalStudents = statByClient.getTotalStudents();
        partnerJson.put("total_students", totalStudents);

        Long totalTeachers = statByClient.getTotalTeachers();
        partnerJson.put("total_teachers", totalTeachers);

        Long totalOthers = statByClient.getTotalOthers();
        partnerJson.put("total_others", totalOthers);

        Long totalUsers = totalStudents + totalTeachers + totalOthers;

        partnerJson.put("total_users", totalUsers);

        partnerJson.put("total_classes", statByClient.getTotalClasses());
        partnerJson.put("tenant_manager", true);
        partnerJson.put("total_competencies_gained", statByClient.getTotalCompetenciesGained());
        partnerJson.put("total_timespent", statByClient.getTotalTimespent());
        partnerJson.put("total_activities_conducted", statByClient.getTotalActivitiesConducted());
        partnerJson.put("total_navigator_courses", statByClient.getTotalNavgiatorCourses());
        partnerJson.put("subject_stats", this.fetchSubjectUsageStats(clientId.toString()));
        partnerJson.put("category_stats", this.fetchCategoryUsageStats(clientId.toString()));
        partnerJson.put("content_type_stats", this.fetchContentUsageStats(clientId.toString()));

      } else {
        populateEmpty(partnerJson);
      }
    } else {
      populateEmpty(partnerJson);
    }

    return partnerJson;
  }

  private JsonArray resolveCountries(List<Long> countries) {
    if (countries == null || countries.size() == 0) {
      return new JsonArray();
    }

    JsonArray countryArray = new JsonArray();
    for (Long countryId : countries) {
      JsonObject countryJson = new JsonObject();
      CountryModel countryModel = countriesMap.get(countryId);
      Long id = countryModel.getId();
      countryJson.put("id", id);
      countryJson.put("name", countryModel.getName());
      countryJson.put("code", countryModel.getCode());
      countryArray.add(countryJson);
    }
    return countryArray;
  }

  private JsonArray resolveStates(List<Long> states) {
    if (states == null || states.size() == 0) {
      return new JsonArray();
    }

    JsonArray stateArray = new JsonArray();
    for (Long stateId : states) {
      JsonObject stateJson = new JsonObject();
      StateModel stateModel = statesMap.get(stateId);
      Long id = stateModel.getId();
      stateJson.put("id", id);
      stateJson.put("name", stateModel.getName());
      stateJson.put("code", stateModel.getCode());
      stateJson.put("countryId", stateModel.getCountryId());
      stateArray.add(stateJson);
    }
    return stateArray;
  }

  private JsonArray reslove(String[] dataSet) {
    JsonArray result = null;
    if (dataSet != null) {
      result = new JsonArray();
      for (String data : dataSet) {
        result.add(data);
      }
    }
    return result;
  }

  private JsonArray fetchSubjectUsageStats(String clientId) {
    List<StatsBySubjectModel> statsBySubject =
        PARTNERS_DATA_SERVICE.fetchSubjectStatsByTenantPartner(clientId);
    JsonArray subjectsUsageStats = new JsonArray();
    if (statsBySubject != null && !statsBySubject.isEmpty()) {
      List<String> subjectCodes = new ArrayList<>();
      statsBySubject.forEach((subject) -> subjectCodes.add(subject.getSubjectCode()));
      Map<String, SubjectModel> subjects = DBHELPER_SERVICE.fetchSubjects(subjectCodes);
      statsBySubject.forEach((subjectStats) -> {
        JsonObject subjectsUsageStat = new JsonObject();
        SubjectModel subject = subjects.get(subjectStats.getSubjectCode());
        subjectsUsageStat.put("id", subject.getId());
        subjectsUsageStat.put("name", subject.getName());
        subjectsUsageStat.put("code", subject.getCode());
        subjectsUsageStat.put("category_id", subject.getCategoryId());
        subjectsUsageStat.put("total_count", subjectStats.getTotalCount());
        subjectsUsageStats.add(subjectsUsageStat);
      });
    }
    return subjectsUsageStats;
  }

  private JsonArray fetchCategoryUsageStats(String clientId) {
    List<StatsBySubjectCategoryModel> statsBySubjectCategories =
        PARTNERS_DATA_SERVICE.fetchSubjectCategoryStatsByTenantPartner(clientId);
    JsonArray subjectCategoriesUsageStats = new JsonArray();
    if (statsBySubjectCategories != null && !statsBySubjectCategories.isEmpty()) {
      List<String> categoryCodes = new ArrayList<>();
      statsBySubjectCategories.forEach((category) -> categoryCodes.add(category.getCategoryCode()));
      Map<String, SubjectCategoryModel> subjects =
          DBHELPER_SERVICE.fetchSubjectCategories(categoryCodes);
      statsBySubjectCategories.forEach((subjectCategoriesStats) -> {
        JsonObject subjectCategoriesUsageStat = new JsonObject();
        SubjectCategoryModel category = subjects.get(subjectCategoriesStats.getCategoryCode());
        subjectCategoriesUsageStat.put("id", category.getId());
        subjectCategoriesUsageStat.put("name", category.getName());
        subjectCategoriesUsageStat.put("code", category.getCode());
        subjectCategoriesUsageStat.put("total_count", subjectCategoriesStats.getTotalCount());
        subjectCategoriesUsageStats.add(subjectCategoriesUsageStat);
      });
    }
    return subjectCategoriesUsageStats;
  }

  private JsonArray fetchContentUsageStats(String clientId) {
    List<StatsByContentModel> statsByContent =
        PARTNERS_DATA_SERVICE.fetchContentStatsByTenantPartner(clientId);
    JsonArray contentUsageStats = new JsonArray();
    if (statsByContent != null && !statsByContent.isEmpty()) {
      List<Long> countryIds = new ArrayList<>();
      statsByContent.forEach((content) -> countryIds.add(content.getCountryId()));
      Map<Long, CountryModel> countries = DBHELPER_SERVICE.fetchCountries(countryIds);
      statsByContent.forEach((contentStats) -> {
        JsonObject contentUsageStat = new JsonObject();
        CountryModel country = countries.get(contentStats.getCountryId());
        contentUsageStat.put("country_code", country.getCode());
        contentUsageStat.put("content_type", contentStats.getContentType());
        contentUsageStat.put("total_count", contentStats.getTotalCount());
        contentUsageStats.add(contentUsageStat);
      });
    }
    return contentUsageStats;
  }


  private void populateEmpty(JsonObject partnerJson) {
    partnerJson.put("total_students", 0);
    partnerJson.put("total_teachers", 0);
    partnerJson.put("total_others", 0);
    partnerJson.put("total_users", 0);
    partnerJson.put("total_classes", 0);
    partnerJson.put("tenant_manager", false);
    partnerJson.put("total_competencies_gained", 0);
    partnerJson.put("total_timespent", 0);
    partnerJson.put("total_activities_conducted", 0);
    partnerJson.put("total_navigator_courses", 0);
  }

}
