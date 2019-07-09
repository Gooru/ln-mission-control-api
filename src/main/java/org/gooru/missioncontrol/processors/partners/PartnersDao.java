
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
  @SqlQuery("SELECT id, organization_name, tenant, partner, partner_type, website, partner_logo, countries FROM partner_registry"
      + " WHERE is_visible = true")
  public List<PartnerModel> fetchPartners();

  @Mapper(PartnerModelMapper.class)
  @SqlQuery("SELECT id, organization_name, tenant, partner_type FROM partner_registry WHERE id = :partnerId AND is_visible = true")
  public PartnerModel fetchPartner(@Bind("partnerId") Long partnerId);

  @Mapper(PartnerModelMapper.class)
  @SqlQuery("SELECT id, organization_name, tenant, partner, partner_type, website, partner_logo, countries FROM partner_registry"
      + " WHERE partner_type = :partnerType AND is_visible = true")
  public List<PartnerModel> fetchPartnersByType(@Bind("partnerType") String partnerType);

}
