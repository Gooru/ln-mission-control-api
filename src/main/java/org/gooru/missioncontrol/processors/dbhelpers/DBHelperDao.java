
package org.gooru.missioncontrol.processors.dbhelpers;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 09-Jul-2019
 */
public interface DBHelperDao {

  @Mapper(CountryModelMapper.class)
  @SqlQuery("SELECT id, name, code FROM country_ds")
  public List<CountryModel> fetchCountries();

}
