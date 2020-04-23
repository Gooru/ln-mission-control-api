package org.gooru.missioncontrol.processors.researchprojects;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import io.vertx.core.json.JsonObject;


public class FetchResearchProjectsModelMapper implements ResultSetMapper<ResearchProjectsModel> {


  @Override
  public ResearchProjectsModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    ResearchProjectsModel model = new ResearchProjectsModel();
    model.setId(r.getLong(MapperFields.ID));
    model.setTitle(r.getString(MapperFields.TITLE));
    model.setSummary(r.getString(MapperFields.SUMMARY));
    model.setCategory(r.getString(MapperFields.CATEGORY));
    model.setDescription(r.getString(MapperFields.DESCRIPTION));
    Array data = r.getArray(MapperFields.DATA);
    if (data != null) {
      model.setData((String[]) data.getArray());
    }
    Array teams = r.getArray(MapperFields.TEAMS);
    if (teams != null) {
      model.setTeams((String[]) teams.getArray());
    }
    String publications = r.getString(MapperFields.PUBLICATIONS);
    if (publications != null) {
      model.setPublications(new JsonObject(publications));
    }
    return model;
  }

  static final class MapperFields {

    private MapperFields() {
      throw new AssertionError();
    }

    static final String ID = "id";
    static final String TITLE = "title";
    static final String SUMMARY = "summary";
    static final String CATEGORY = "category";
    static final String DESCRIPTION = "description";
    static final String TEAMS = "teams";
    static final String PUBLICATIONS = "publications";
    static final String DATA = "data";
  }

}
