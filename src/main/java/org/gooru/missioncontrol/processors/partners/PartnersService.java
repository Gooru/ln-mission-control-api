
package org.gooru.missioncontrol.processors.partners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gooru.missioncontrol.processors.utils.HelperUtility;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class PartnersService {

  private final PartnersDao partnersDao;
  private final UsersDao usersDao;

  public PartnersService(DBI dbi) {
    this.partnersDao = dbi.onDemand(PartnersDao.class);
    this.usersDao = dbi.onDemand(UsersDao.class);
  }

  public List<PartnerModel> fetchPartners() {
    return this.partnersDao.fetchPartners();
  }

  public PartnerModel fetchPartner(Long partnerId) {
    return this.partnersDao.fetchPartner(partnerId);
  }
  
  public Map<String, Long> fetchUserCountsByTenants(List<String> tenantIds) {
    List<UserCountModel> userCounts =usersDao.fetchUserCountByTenants(HelperUtility.toPostgresArrayString(tenantIds));
    Map<String, Long> userCountMap = new HashMap<>(userCounts.size());
    userCounts.forEach(count -> {
      userCountMap.put(count.getTenantId().toString(), count.getCount());
    });
    return userCountMap;
  }
}
