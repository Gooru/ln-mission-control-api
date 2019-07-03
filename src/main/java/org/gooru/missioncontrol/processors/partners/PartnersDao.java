
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public interface PartnersDao {

  @Mapper(PartnerModelMapper.class)
  @SqlQuery("SELECT id, organization_name, tenant, partner_type FROM partner_registry")
  public List<PartnerModel> fetchPartners();

  @Mapper(PartnerModelMapper.class)
  @SqlQuery("SELECT id, organization_name, tenant, partner_type FROM partner_registry WHERE id = :partnerId")
  public PartnerModel fetchPartner(@Bind("partnerId") Long partnerId);
}
