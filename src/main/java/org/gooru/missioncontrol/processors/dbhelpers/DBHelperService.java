
package org.gooru.missioncontrol.processors.dbhelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public class DBHelperService {

  private final DBHelperDao dao;

  public DBHelperService(DBI dbi) {
    this.dao = dbi.onDemand(DBHelperDao.class);
  }

  public Map<Long, CountryModel> fetchCountries() {
    List<CountryModel> countries = this.dao.fetchCountries();
    Map<Long, CountryModel> countryMap = new HashMap<>();
    countries.forEach(country -> {
      countryMap.put(country.getId(), country);
    });

    return countryMap;
  }
}
