package org.gooru.missioncontrol.bootstrap.component.jdbi;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.gooru.missioncontrol.app.data.JsonNodeHolder;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonNodeHolderMapper implements ResultSetMapper<JsonNodeHolder> {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonNodeHolderMapper.class);
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  @Override
  public JsonNodeHolder map(final int index, final ResultSet resultSet,
      final StatementContext statementContext) throws SQLException {
    try {
      JsonNode result = new ObjectMapper().readTree(resultSet.getString(1));
      return new JsonNodeHolder(result);
    } catch (IOException e) {
      LOGGER.warn("Error converting data to JsonNode", e);
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.ERROR,
          resourceBundle.getString("internal.error"));
    }
  }

}
