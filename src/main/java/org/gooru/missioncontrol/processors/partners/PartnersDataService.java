
package org.gooru.missioncontrol.processors.partners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public class PartnersDataService {

  private final PartnersDataDao dao;

  public PartnersDataService(DBI dbi) {
    this.dao = dbi.onDemand(PartnersDataDao.class);
  }

  public Map<Long, StatsByCountryModel> fetchStatsByCountry(Integer month, Integer year) {
    List<StatsByCountryModel> stats = this.dao.fetchStatsByCountry(month, year);
    Map<Long, StatsByCountryModel> statsMap = new HashMap<>();
    stats.forEach(stat -> {
      statsMap.put(stat.getCountryId(), stat);
    });
    return statsMap;
  }

  public Map<String, StatsByTenantPartnerModel> fetchStatsByTenantPartner(Integer month,
      Integer year) {
    List<StatsByTenantPartnerModel> stats = this.dao.fetchStatsByTenantPartners(month, year);
    Map<String, StatsByTenantPartnerModel> statsMap = new HashMap<>();
    stats.forEach(stat -> {
      String clientId = (stat.getPartner() != null) ? stat.getPartner() : stat.getTenant();
      statsMap.put(clientId, stat);
    });
    return statsMap;
  }
}
