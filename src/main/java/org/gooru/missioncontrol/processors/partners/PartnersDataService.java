
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

  public Map<Long, StatsByCountryModel> fetchStatsByCountry() {
    List<StatsByCountryModel> stats = this.dao.fetchStatsByCountry();
    return this.mapStatsByCountry(stats);
  }

  public Map<Long, StatsByCountryModel> fetchStatsByCountry(Integer month, Integer year) {
    List<StatsByCountryModel> stats = this.dao.fetchMonthAndYearWiseStatsByCountry(month, year);
    return this.mapStatsByCountry(stats);
  }

  public Map<String, StatsByTenantPartnerModel> fetchStatsByTenantPartner() {
    List<StatsByTenantPartnerModel> stats = this.dao.fetchStatsByTenantPartners();
    return this.mapStatsByTenantPartner(stats);
  }

  public Map<String, StatsByTenantPartnerModel> fetchStatsByTenantPartner(Integer month,
      Integer year) {
    List<StatsByTenantPartnerModel> stats =
        this.dao.fetchMonthAndYearWiseStatsByTenantPartners(month, year);
    return this.mapStatsByTenantPartner(stats);
  }

  public List<DistributionBySubjectModel> fetchSubjectDistributionByTenantPartner(String clientId) {
    return this.dao.fetchDistributionBySubject(clientId);
  }

  public List<DistributionBySubjectCategoryModel> fetchSubjectCategoryDistributionByTenantPartner(
      String clientId) {
    return this.dao.fetchDistributionBySubjectCategory(clientId);
  }
  
  public List<DistributionByContentModel> fetchContentDistributionByTenantPartner(
      String clientId) {
    return this.dao.fetchDistributionByContent(clientId);
  }

  public List<StatsByContentModel> fetchContentStatsByTenantPartner(String clientId) {
    return this.dao.fetchStatsByContent(clientId);
  }

  private Map<String, StatsByTenantPartnerModel> mapStatsByTenantPartner(
      List<StatsByTenantPartnerModel> stats) {
    Map<String, StatsByTenantPartnerModel> statsMap = new HashMap<>();
    stats.forEach(stat -> {
      String clientId = (stat.getPartner() != null) ? stat.getPartner() : stat.getTenant();
      statsMap.put(clientId, stat);
    });
    return statsMap;
  }

  private Map<Long, StatsByCountryModel> mapStatsByCountry(List<StatsByCountryModel> stats) {
    Map<Long, StatsByCountryModel> statsMap = new HashMap<>();
    stats.forEach(stat -> {
      statsMap.put(stat.getCountryId(), stat);
    });
    return statsMap;
  }
}
