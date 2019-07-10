
package org.gooru.missioncontrol.processors.partners;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.dbhelpers.CountryModel;
import org.gooru.missioncontrol.processors.dbhelpers.DBHelperService;
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
 * @author szgooru Created On 01-Jul-2019
 */
public class FetchPartnersProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(FetchPartnersProcessor.class);

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

  public FetchPartnersProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    LOGGER.debug("fetching all partners from database");
    
    JsonObject requestBody = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
    String partnerType = requestBody.getString("type", null);
    
    List<PartnerModel> partners = new ArrayList<>();
    if (partnerType != null) {
      partners = PARTNERS_SERVICE.fetchPartnersByType(partnerType);
    } else {
      partners = PARTNERS_SERVICE.fetchPartners();
    }

    Map<String, List<PartnerModel>> partnersByTypeMap = new HashMap<>();
    partners.forEach(partner -> {
      String type = partner.getPartnerType();
      if (partnersByTypeMap.containsKey(type)) {
        partnersByTypeMap.get(type).add(partner);
      } else {
        List<PartnerModel> partnerList = new ArrayList<>();
        partnerList.add(partner);
        partnersByTypeMap.put(type, partnerList);
      }
    });

    countriesMap = DBHELPER_SERVICE.fetchCountries();

    LocalDate now = LocalDate.now();
    Map<String, StatsByTenantPartnerModel> statsByClientMap =
        PARTNERS_DATA_SERVICE.fetchStatsByTenantPartner(now.getMonthValue(), now.getYear());

    LOGGER.debug("preparing response");
    JsonObject response = prepareResponse(partnersByTypeMap, statsByClientMap);

    LOGGER.debug("sending response: {}", response.toString());
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

  private JsonObject prepareResponse(Map<String, List<PartnerModel>> partnersByTypeMap,
      Map<String, StatsByTenantPartnerModel> statsByClientMap) {
    JsonObject result = new JsonObject();
    Set<String> keys = partnersByTypeMap.keySet();
    
    Long overallTotalUsers = 0l;
    Set<Long> totalPartners = new HashSet<>();
    for (String partnerType : keys) {
      JsonArray partnersArray = new JsonArray();
      List<PartnerModel> partners = partnersByTypeMap.get(partnerType);
      for (PartnerModel partner : partners) {
        JsonObject partnerJson = new JsonObject();
        partnerJson.put("partner_id", partner.getId());
        totalPartners.add(partner.getId());
        partnerJson.put("partner_name", partner.getOrganizationName());
        partnerJson.put("website", partner.getWebsite());
        partnerJson.put("logo", partner.getLogo());
        partnerJson.put("countries", resolveCountries(partner.getCountries()));

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
            overallTotalUsers = overallTotalUsers + totalUsers;
            partnerJson.put("total_users", totalUsers);

            partnerJson.put("total_classes", statByClient.getTotalClasses());
            partnerJson.put("tenant_manager", true);
          } else {
            populateEmpty(partnerJson);
          }
        } else {
          populateEmpty(partnerJson);
        }
        partnersArray.add(partnerJson);
      }

      result.put(partnerType, partnersArray);
    }
    
    JsonObject overallStatsJson = new JsonObject();
    overallStatsJson.put("total_partners", totalPartners.size());
    overallStatsJson.put("total_users", overallTotalUsers);
    overallStatsJson.put("total_countries", 2);
    result.put("overall_stats", overallStatsJson);
    return result;
  }

  private JsonArray resolveCountries(Long[] countries) {
    if (countries == null || countries.length == 0) {
      return new JsonArray();
    }

    JsonArray countryArray = new JsonArray();
    for (Long countryId : countries) {
      JsonObject countryJson = new JsonObject();
      CountryModel countryModel = countriesMap.get(countryId);
      countryJson.put("id", countryModel.getId());
      countryJson.put("name", countryModel.getName());
      countryJson.put("code", countryModel.getCode());

      countryArray.add(countryJson);
    }
    return countryArray;
  }

  private void populateEmpty(JsonObject partnerJson) {
    partnerJson.put("total_students", 0);
    partnerJson.put("total_teachers", 0);
    partnerJson.put("total_others", 0);
    partnerJson.put("total_users", 0);
    partnerJson.put("total_classes", 0);
    partnerJson.put("tenant_manager", false);
  }
}
