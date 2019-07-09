
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class PartnersService {

  private final PartnersDao partnersDao;

  public PartnersService(DBI dbi) {
    this.partnersDao = dbi.onDemand(PartnersDao.class);
  }

  public List<PartnerModel> fetchPartners() {
    return this.partnersDao.fetchPartners();
  }
  
  public List<PartnerModel> fetchPartnersByType(String partnerType) {
    return this.partnersDao.fetchPartnersByType(partnerType);
  }

  public PartnerModel fetchPartner(Long partnerId) {
    return this.partnersDao.fetchPartner(partnerId);
  }

}
