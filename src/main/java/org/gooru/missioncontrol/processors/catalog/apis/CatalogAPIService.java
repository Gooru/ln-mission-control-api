package org.gooru.missioncontrol.processors.catalog.apis;

import org.gooru.missioncontrol.bootstrap.component.AppConfiguration;
import org.gooru.missioncontrol.constants.Constants;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;

public class CatalogAPIService {

  private HttpClient httpClient;

  public CatalogAPIService(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public Future<String> fetchTranscripts(String resourceIds) {

    Future<String> future = Future.future();
    RequestOptions reqOptions = getReqOptions();
    reqOptions.setURI(Constants.TRANSCRIPT_API_URI + resourceIds);

    return callGetAPI(reqOptions, future);

  }
  

  public Future<String> fetchSummary(String resourceIds) {

    Future<String> future = Future.future();
    RequestOptions reqOptions = getReqOptions();
    reqOptions.setURI(Constants.SUMMARY_API_URI + resourceIds);

    return callGetAPI(reqOptions, future);

  }

  private RequestOptions getReqOptions() {
    AppConfiguration appConfig = AppConfiguration.getInstance();
    RequestOptions reqOptions = new RequestOptions();
    reqOptions.setHost(appConfig.apiHost());
    reqOptions.setPort(appConfig.apiServerPort());
    return reqOptions;
  }
  
  private Future<String> callGetAPI(RequestOptions reqOptions, Future<String> future){
    this.httpClient.getNow(reqOptions, new Handler<HttpClientResponse>() {

      @Override
      public void handle(HttpClientResponse httpClientResponse) {

        httpClientResponse.bodyHandler(new Handler<Buffer>() {

          @Override
          public void handle(Buffer buffer) {
            String result = buffer.getString(0, buffer.length());
            future.complete(result);
          }
        });
     
      }
    });
    return future;
  }

}
