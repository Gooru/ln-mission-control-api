
package org.gooru.missioncontrol.processors.stats.countries;

import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 03-Jul-2019
 */
public class StatsByCountryProcessor implements MessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;
  
  public StatsByCountryProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }
  
  @Override
  public Future<MessageResponse> process() {
    String json  = "{\n" + 
        "    \"countries\": [{\n" + 
        "        \"id\": 101,\n" + 
        "        \"country_name\": \"India\",\n" + 
        "        \"country_code\": \"IN\",\n" + 
        "        \"total_student\": 784412,\n" + 
        "        \"total_teacher\": 13108,\n" + 
        "        \"total_other\": 5677,\n" + 
        "        \"active_student\": 56448,\n" + 
        "        \"active_classroom\": 1500,\n" + 
        "        \"competencies_gained\": 54789,\n" + 
        "        \"total_timespent\": 89554554,\n" + 
        "        \"activities_conducted\": 6321,\n" + 
        "        \"navigator_courses\": 45\n" + 
        "    }, {\n" + 
        "        \"id\": 231,\n" + 
        "        \"country_name\": \"United States\",\n" + 
        "        \"country_code\": \"US\",\n" + 
        "        \"total_student\": 70411,\n" + 
        "        \"total_teacher\": 13108,\n" + 
        "        \"total_other\": 1213,\n" + 
        "        \"active_student\": 21581,\n" + 
        "        \"active_classroom\": 2181,\n" + 
        "        \"competencies_gained\": 2019821,\n" + 
        "        \"total_timespent\": 562248555,\n" + 
        "        \"activities_conducted\": 563277,\n" + 
        "        \"navigator_courses\": 56\n" + 
        "    }, {\n" + 
        "        \"id\": 30,\n" + 
        "        \"country_name\": \"Brazil\",\n" + 
        "        \"country_code\": \"BR\",\n" + 
        "        \"total_student\": 13102,\n" + 
        "        \"total_teacher\": 13108,\n" + 
        "        \"total_other\": 1213,\n" + 
        "        \"active_student\": 21581,\n" + 
        "        \"active_classroom\": 2181,\n" + 
        "        \"competencies_gained\": 345,\n" + 
        "        \"total_timespent\": 123455690,\n" + 
        "        \"activities_conducted\": 1234,\n" + 
        "        \"navigator_courses\": 12\n" + 
        "    }, {\n" + 
        "        \"id\": 44,\n" + 
        "        \"country_name\": \"China\",\n" + 
        "        \"country_code\": \"CN\",\n" + 
        "        \"total_student\": 5622,\n" + 
        "        \"total_teacher\": 215,\n" + 
        "        \"total_other\": 865,\n" + 
        "        \"active_student\": 4500,\n" + 
        "        \"active_classroom\": 658,\n" + 
        "        \"competencies_gained\": 6584,\n" + 
        "        \"total_timespent\": 123455690,\n" + 
        "        \"activities_conducted\": 1234,\n" + 
        "        \"navigator_courses\": 7\n" + 
        "    }]\n" + 
        "}";
    JsonObject response = new JsonObject(json);
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

}
