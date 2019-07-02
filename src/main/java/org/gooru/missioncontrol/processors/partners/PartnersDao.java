
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public interface PartnersDao {

  @SqlQuery("")
  public List<String> fetchPartners();

  @SqlQuery("")
  public String fetchPartner(String partnerId);
}
