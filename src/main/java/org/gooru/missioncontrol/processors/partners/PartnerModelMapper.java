
package org.gooru.missioncontrol.processors.partners;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
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
    model.setIntro(r.getString("intro"));
    String tenantId = r.getString("tenant");
    if (tenantId != null) {
      model.setTenant(UUID.fromString(tenantId));
    }

    String partner = r.getString("partner");
    if (partner != null) {
      model.setPartner(UUID.fromString(partner));
    }

    Array countries = r.getArray("countries");

    Array videos = r.getArray("videos");
    if (videos != null) {
      model.setVideos((String[]) videos.getArray());
    }
    
    Array images = r.getArray("images");
    if (images != null) {
      model.setImages((String[]) images.getArray());
    }
    
    if (countries != null) {
      model.setCountries(Arrays.stream((Long[]) countries.getArray()).collect(Collectors.toList()));
    }
    Array states = r.getArray("states");
    if (states != null) {
      model.setStates(Arrays.stream((Long[]) states.getArray()).collect(Collectors.toList()));
    }
    return model;
  }

}
