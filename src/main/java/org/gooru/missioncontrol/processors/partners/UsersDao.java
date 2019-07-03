
package org.gooru.missioncontrol.processors.partners;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 02-Jul-2019
 */
public interface UsersDao {

  @Mapper(UserCountModelMapper.class)
  @SqlQuery("SELECT count(id) as user_count, tenant_id FROM users WHERE tenant_id = ANY(:tenantIds::uuid[]) AND is_deleted = false GROUP BY tenant_id")
  public List<UserCountModel> fetchUserCountByTenants(@Bind("tenantIds") String tenantIds);
}
