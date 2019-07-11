
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class PartnersService {

  private final static Logger LOGGER = LoggerFactory.getLogger(PartnersService.class);

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

  public String fetchFromCache(String partnerType, String cacheType) {
    if (partnerType == null) {
      return this.partnersDao.fetchFromCache(cacheType);
    }
    return this.partnersDao.fetchFromCacheByPartnerType(partnerType, cacheType);
  }

  public void updateCache(String partnerType, String data, String cacheType) {
    Integer count = -1;
    if (partnerType == null) {
      count = this.partnersDao.checkIfCacheExists(cacheType);
    } else {
      count = this.partnersDao.checkIfCacheExistsByPartnerType(partnerType, cacheType);
    }
    
    if (count == 0) {
      LOGGER.debug("inserting into cache table = partnerType :{} || cacheType :{}", partnerType,
          cacheType);
      this.partnersDao.insertCache(partnerType, data, cacheType);
    } else {
      LOGGER.debug("updating cache table = partnerType :{} || cacheType :{}", partnerType,
          cacheType);
      if (partnerType == null) {
        this.partnersDao.updateCache(data, cacheType);
      } else {
        this.partnersDao.updateCacheByPartnerType(partnerType, data, cacheType);
      }
    }
  }
}
