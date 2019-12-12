package org.gooru.missioncontrol.processors.learners.personalized;

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


public class FetchPersonalizedLearnersProcessor implements MessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(FetchPersonalizedLearnersProcessor.class);
  private final FetchPersonalizedLearnersService fetchPersonalizedLearnersService =
      new FetchPersonalizedLearnersService(DBICreator.getDbiForDatascopeDB());

  public FetchPersonalizedLearnersProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    try {
      fetchPersonalizedLearners();
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
    return result;
  }

  private void fetchPersonalizedLearners() {
    try {
      Map<String, List<PersonalizedLearnersModel>> personalizedLearners =
          fetchPersonalizedLearnersService.fetchPersonalizedLearners();
      String resultString = new ObjectMapper().writeValueAsString(personalizedLearners);
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
