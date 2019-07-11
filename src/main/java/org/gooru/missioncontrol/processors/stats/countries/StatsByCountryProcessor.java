
package org.gooru.missioncontrol.processors.stats.countries;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.constants.AppConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.dbhelpers.CountryModel;
import org.gooru.missioncontrol.processors.dbhelpers.DBHelperService;
import org.gooru.missioncontrol.processors.partners.PartnersDataService;
import org.gooru.missioncontrol.processors.partners.PartnersService;
import org.gooru.missioncontrol.processors.partners.StatsByCountryModel;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 03-Jul-2019
 */
public class StatsByCountryProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(StatsByCountryProcessor.class);

  private final static DBHelperService DBHELPER_SERVICE =
      new DBHelperService(DBICreator.getDbiForDefaultDS());
  
  private final static PartnersService PARTNERS_SERVICE = new PartnersService(DBICreator.getDbiForDefaultDS());

  private final static PartnersDataService PARTNERS_DATA_SERVICE =
      new PartnersDataService(DBICreator.getDbiForDatascopeDB());

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;
  
  private Long globalStudents = 0l;
  private Long globalTeachers = 0l;
  private Long globalOthers = 0l;

  private Map<Long, CountryModel> countriesMap = new HashMap<>();

  public StatsByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    LOGGER.debug("fetching stats by countries");
    JsonObject requestBody = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
    String strRefresh = requestBody.getString("refresh", null);
    Boolean isRefresh = (strRefresh == null || strRefresh.isEmpty()) ? false : Boolean.valueOf(strRefresh);
    
    if (!isRefresh) {
      String cacheData = PARTNERS_SERVICE.fetchFromCache(null, AppConstants.CACHE_TYPE_COUNTRIES_STATS_API);
      if (cacheData != null) {
        JsonObject response = new JsonObject(cacheData);
        result.complete(MessageResponseFactory.createGetResponse(response));
        LOGGER.debug("sending data from cache");
        return result;
      } else {
        LOGGER.debug("no data found in cache");
      }
    }
    
    LOGGER.debug("fetching fresh data from data store");
    LocalDate now = LocalDate.now();
    Map<Long, StatsByCountryModel> statsByCountryMap =
        PARTNERS_DATA_SERVICE.fetchStatsByCountry(now.getMonthValue(), now.getYear());

    countriesMap = DBHELPER_SERVICE.fetchCountries();

    JsonArray countriesStatsArray = new JsonArray();
    Set<Long> countryIdSet = statsByCountryMap.keySet();
    for (Long countryId : countryIdSet) {
      StatsByCountryModel statsModel = statsByCountryMap.get(countryId);
      CountryModel countryModel = countriesMap.get(countryId);
      countriesStatsArray.add(prepareCountryResponse(statsModel, countryModel));
    }

    JsonObject response = new JsonObject();
    response.put("countries", countriesStatsArray);
    response.put("global_total_students", globalStudents);
    response.put("global_total_teachers", globalTeachers);
    response.put("global_total_others", globalOthers);
    PARTNERS_SERVICE.updateCache(null, response.toString(), AppConstants.CACHE_TYPE_COUNTRIES_STATS_API);
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

  private JsonObject prepareCountryResponse(StatsByCountryModel statsModel,
      CountryModel countryModel) {
    JsonObject result = new JsonObject();
    result.put("id", countryModel.getId());
    result.put("country_name", countryModel.getName());
    result.put("country_code", countryModel.getCode());

    Long totalStudents = statsModel.getTotalStudents();
    globalStudents = globalStudents + totalStudents;
    result.put("total_students", totalStudents);

    Long totalTeachers = statsModel.getTotalTeachers();
    globalTeachers = globalTeachers + totalTeachers;
    result.put("total_teachers", totalTeachers);

    Long totalOthers = statsModel.getTotalOthers();
    globalOthers = globalOthers + totalOthers;
    result.put("total_others", totalOthers);

    Long totalUsers = totalStudents + totalTeachers + totalOthers;
    result.put("total_users", totalUsers);

    result.put("total_classes", statsModel.getTotalClasses());
    result.put("total_competencies_gained", statsModel.getTotalCompetenciesGained());
    result.put("total_timespent", statsModel.getTotalTimespent());
    result.put("total_activities_conducted", statsModel.getTotalActivitiesConducted());
    result.put("total_navigator_courses", statsModel.getTotalNavgiatorCourses());
    return result;
  }

}
