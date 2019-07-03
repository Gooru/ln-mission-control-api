
package org.gooru.missioncontrol.processors.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author szgooru Created On 02-Jul-2019
 */
public class PartnerModelMapper implements ResultSetMapper<PartnerModel> {

  @Override
  public PartnerModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    PartnerModel model = new PartnerModel();
    model.setId(r.getLong("id"));
    model.setOrganizationName(r.getString("organization_name"));
    model.setPartnerType(r.getString("partner_type"));
    String tenantId = r.getString("tenant");
    if (tenantId != null) {
      model.setTenant(UUID.fromString(tenantId));
    }
    return model;
  }

}
