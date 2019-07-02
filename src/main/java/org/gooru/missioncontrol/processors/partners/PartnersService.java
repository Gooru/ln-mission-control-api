
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class PartnersService {

  private final PartnersDao dao;

  public PartnersService(DBI dbi) {
    this.dao = dbi.onDemand(PartnersDao.class);
  }

  public List<String> fetchPartners() {
    return this.dao.fetchPartners();
  }

  public String fetchPartner(String partnerId) {
    return this.dao.fetchPartner(partnerId);
  }
}
