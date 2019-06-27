package org.gooru.missioncontrol.processors;

import org.gooru.missioncontrol.processors.responses.MessageResponse;

public interface Processor {

  MessageResponse process();
}
