
package org.gooru.missioncontrol.processors.partners;

import java.sql.Array;
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
    model.setWebsite(r.getString("website"));
    model.setLogo(r.getString("partner_logo"));
    String tenantId = r.getString("tenant");
    if (tenantId != null) {
      model.setTenant(UUID.fromString(tenantId));
    }
    
    String partner = r.getString("partner");
    if (partner != null) {
      model.setPartner(UUID.fromString(partner));
    }
    
    Array countries = r.getArray("countries");
    if (countries != null) {
      model.setCountries((Long[]) countries.getArray());
    }
    return model;
  }

}
