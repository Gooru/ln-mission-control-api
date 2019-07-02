package org.gooru.missioncontrol.processors;

import org.gooru.missioncontrol.processors.responses.MessageResponse;
import io.vertx.core.Future;

public interface MessageProcessor {
  Future<MessageResponse> process();
}
