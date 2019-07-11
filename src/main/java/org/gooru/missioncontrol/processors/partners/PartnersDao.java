
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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
  
  @SqlQuery("SELECT data FROM mission_control_cache WHERE partner_type IS NULL AND cache_type = :cacheType")
  public String fetchFromCache(@Bind("cacheType") String cacheType);
  
  @SqlQuery("SELECT data FROM mission_control_cache WHERE partner_type = :partnerType AND cache_type = :cacheType")
  public String fetchFromCacheByPartnerType(@Bind("partnerType") String partnerType, @Bind("cacheType") String cacheType);
  
  @SqlQuery("SELECT count(*) FROM mission_control_cache WHERE partner_type IS NULL AND cache_type = :cacheType")
  public Integer checkIfCacheExists(@Bind("cacheType") String cacheType);
  
  @SqlQuery("SELECT count(*) FROM mission_control_cache WHERE partner_type = :partnerType AND cache_type = :cacheType")
  public Integer checkIfCacheExistsByPartnerType(@Bind("partnerType") String partnerType, @Bind("cacheType") String cacheType);
  
  
  @SqlUpdate("INSERT INTO mission_control_cache(partner_type, data, cache_type, last_updated) VALUES (:partnerType, :data::jsonb, :cacheType, now())")
  public void insertCache(@Bind("partnerType") String partnerType, @Bind("data") String data, @Bind("cacheType") String cacheType);
  
  @SqlUpdate("UPDATE mission_control_cache SET data = :data::jsonb, last_updated = now() WHERE partner_type IS NULL AND cache_type = :cacheType")
  public void updateCache(@Bind("data") String data, @Bind("cacheType") String cacheType);
  
  @SqlUpdate("UPDATE mission_control_cache SET data = :data::jsonb, last_updated = now() WHERE partner_type = :partnerType AND cache_type = :cacheType")
  public void updateCacheByPartnerType(@Bind("partnerType") String partnerType, @Bind("data") String data, @Bind("cacheType") String cacheType);

}
