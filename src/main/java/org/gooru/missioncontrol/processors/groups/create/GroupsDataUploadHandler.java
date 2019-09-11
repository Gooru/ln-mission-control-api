
package org.gooru.missioncontrol.processors.groups.create;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.gooru.missioncontrol.bootstrap.component.AppConfiguration;
import org.gooru.missioncontrol.processors.groups.entities.GroupDataEntity;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 31-Aug-2019
 */
public class GroupsDataUploadHandler {

  private final static Logger LOGGER = LoggerFactory.getLogger(GroupsDataUploadHandler.class);

  private final RoutingContext routingContext;
  private final JsonObject session;

  private final Future<MessageResponse> result;

  public GroupsDataUploadHandler(RoutingContext routingContext, JsonObject config) {
    this.routingContext = routingContext;
    this.session = config;
    this.result = Future.future();
  }

  public Future<MessageResponse> upload() {
    try {
      // Receive uploaded files
      Set<FileUpload> filesToUpload = routingContext.fileUploads();
      if (filesToUpload.size() == 0) {
        LOGGER.warn("No file provided to upload");
        result.fail("No file to upload");
        return result;
      }

      if (filesToUpload.size() > 1) {
        LOGGER.warn("trying to upload more that one file");
        result.fail("only 1 file upload is supported");
        return result;
      }

      Iterator<FileUpload> it = filesToUpload.iterator();
      FileUpload file = it.next();
      
      // Check if the extension of the file is zip
      LOGGER.debug("received filename :{} -- uploaded filename :{}", file.fileName(),
          file.uploadedFileName());
      if (!(getFileExtension(file.fileName()).equalsIgnoreCase("csv"))) {
        LOGGER.error("invalid file type provided to upload");
        result.fail("invalid file type provided to upload");
        return result;
      }

      String filePath =
          AppConfiguration.getInstance().fileTempLocation();
      if (filePath == null) {
        LOGGER.warn("no temp file location configured");
      }

      FileSystem fileSystem = this.routingContext.vertx().fileSystem();
      Buffer buffer = fileSystem.readFileBlocking(file.uploadedFileName());

      FileOutputStream fos = new FileOutputStream(new File(filePath + file.fileName()));
      fos.write(buffer.getBytes());
      fos.close();
      
      List<GroupDataEntity> groups = readCsv(filePath + file.fileName());
      LOGGER.debug("number of records to process: {}", groups.size());
      
      new GroupsCreationProcessor(groups, session).process();
      
      // Delete temporary stored file
      try {
        boolean isDeleted = Files.deleteIfExists(Paths.get(file.uploadedFileName()));
        Files.deleteIfExists(Paths.get(filePath + file.fileName()));
        if (!isDeleted) {
          LOGGER.warn("unable to delete temporary file");
        }
        LOGGER.debug("temporary file deleted successfully");
      } catch (IOException e) {
        LOGGER.error("unable to delete temporary file", e);
      }

      this.result.complete(MessageResponseFactory.createPostResponse());
      return this.result;
    } catch (Throwable t) {
      LOGGER.error("error while processing request", t);
      this.result.fail("error while processing request");
      return result;
    }
  }
  
  private static String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.') + 1);
  }
  
  private List<GroupDataEntity> readCsv(String filePath) throws IOException {
    List<GroupDataEntity> groups = new ArrayList<>();
    FileReader csvDataReader = new FileReader(filePath);
    try (Stream<CSVRecord> streams = StreamSupport
        .stream(new CSVParser(csvDataReader, CSVFormat.RFC4180).spliterator(), false)) {
      streams.skip(1).forEach(new Consumer<CSVRecord>() {

        @Override
        public void accept(CSVRecord t) {
          GroupDataEntity group = new GroupDataEntity(t);
          groups.add(group);
        }
      });
    }
    return groups;
  }

}
