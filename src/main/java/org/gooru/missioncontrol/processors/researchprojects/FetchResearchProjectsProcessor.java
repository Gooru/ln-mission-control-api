package org.gooru.missioncontrol.processors.researchprojects;

import java.util.List;
import java.util.Map;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;


public class FetchResearchProjectsProcessor implements MessageProcessor {

  private final Future<MessageResponse> result;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(FetchResearchProjectsProcessor.class);
  private final FetchResearchProjectsService fetchResearchProjectsService =
      new FetchResearchProjectsService(DBICreator.getDbiForDefaultDS());

  public FetchResearchProjectsProcessor(Vertx vertx, Message<JsonObject> message) {
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      fetchResearchProjects();
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
    return result;
  }

  private void fetchResearchProjects() {
    try {
      Map<String, List<ResearchProjectsModel>> researchProjects =
          fetchResearchProjectsService.fetchResearchProjects();
      String resultString = new ObjectMapper().writeValueAsString(researchProjects);
      result.complete(MessageResponseFactory.createGetResponse(new JsonObject(resultString)));
    } catch (JsonProcessingException e) {
      LOGGER.error("Not able to convert data to JSON", e);
      result.fail(e);
    } catch (DecodeException e) {
      LOGGER.warn("Not able to convert data to JSON", e);
      result.fail(e);
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
  }
}
