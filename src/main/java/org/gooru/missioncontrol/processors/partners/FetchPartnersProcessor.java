
package org.gooru.missioncontrol.processors.partners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.processors.MessageProcessor;
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

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  public FetchPartnersProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    LOGGER.debug("fetching all partners from database");
    List<PartnerModel> partners = PARTNERS_SERVICE.fetchPartners();

    Map<String, List<PartnerModel>> partnersByTypeMap = new HashMap<>();
    List<String> tenantIds = new ArrayList<>(partners.size());
    partners.forEach(partner -> {
      UUID tenantId = partner.getTenant();
      if (tenantId != null) {
        tenantIds.add(tenantId.toString());
      }

      String partnerType = partner.getPartnerType();
      if (partnersByTypeMap.containsKey(partnerType)) {
        partnersByTypeMap.get(partnerType).add(partner);
      } else {
        List<PartnerModel> partnerList = new ArrayList<>();
        partnerList.add(partner);
        partnersByTypeMap.put(partnerType, partnerList);
      }
    });

    LOGGER.debug("fetching user counts for the tenants");
    Map<String, Long> userCounts = PARTNERS_SERVICE.fetchUserCountsByTenants(tenantIds);
    
    LOGGER.debug("preparing response");
    JsonObject response = prepareResponse(partnersByTypeMap, userCounts);

    LOGGER.debug("sending response: {}", response.toString());
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

  private JsonObject prepareResponse(Map<String, List<PartnerModel>> partnersByTypeMap,
      Map<String, Long> userCounts) {
    JsonObject result = new JsonObject();
    Set<String> keys = partnersByTypeMap.keySet();
    for (String partnerType : keys) {
      JsonArray partnersArray = new JsonArray();
      List<PartnerModel> partners = partnersByTypeMap.get(partnerType);
      partners.forEach(partner -> {
        JsonObject partnerJson = new JsonObject();
        partnerJson.put("partner_id", partner.getId());
        partnerJson.put("partner_name", partner.getOrganizationName());

        UUID tenantId = partner.getTenant();
        if (tenantId != null) {
          Long activeUsers = userCounts.get(tenantId.toString());
          partnerJson.put("active_users", (activeUsers != null) ? activeUsers : 0);
          partnerJson.put("tenant_manager", true);
        } else {
          partnerJson.put("active_users", 0);
          partnerJson.put("tenant_manager", false);
        }
        partnersArray.add(partnerJson);
      });

      result.put(partnerType, partnersArray);
    }
    return result;
  }

}
