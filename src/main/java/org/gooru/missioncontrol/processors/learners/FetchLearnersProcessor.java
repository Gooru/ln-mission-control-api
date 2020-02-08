package org.gooru.missioncontrol.processors.learners;

import java.util.List;
import java.util.Map;
import org.gooru.missioncontrol.app.data.EventBusMessage;
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


public class FetchLearnersProcessor implements MessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;
  private EventBusMessage eventBusMessage;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(FetchLearnersProcessor.class);
  private final FetchLearnersService fetchLearnersService =
      new FetchLearnersService(DBICreator.getDbiForDefaultDS());

  public FetchLearnersProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      fetchLearners();
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
    return result;
  }

  private void fetchLearners() {
    try {
      this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
      UserListCommand command =
          UserListCommand.builder(eventBusMessage.getRequestBody(), eventBusMessage.getSession());
      Map<String, List<LearnersModel>> learners =
          fetchLearnersService.fetchLearners(command);
      String resultString = new ObjectMapper().writeValueAsString(learners);
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
