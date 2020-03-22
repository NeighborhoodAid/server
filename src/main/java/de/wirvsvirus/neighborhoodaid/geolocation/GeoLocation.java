package de.wirvsvirus.neighborhoodaid.geolocation;

import de.wirvsvirus.neighborhoodaid.db.model.Address;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoLocation {

    private final static Logger logger = LoggerFactory.getLogger(GeoLocation.class);

    public static void requestGeoLocation(Vertx vertx, Address address, Handler<AsyncResult<Address>> handler) {
        WebClient client = WebClient.create(vertx);
        client
                .get(443, "nominatim.openstreetmap.org", "/search")
                .ssl(true)
                .addQueryParam("street", address.getHouseNumber() + " " + address.getStreet())
                .addQueryParam("city", address.getCity())
                .addQueryParam("country", "germany")
                .addQueryParam("postalcode", address.getPostcode())
                .addQueryParam("countrycodes", "de")
                .addQueryParam("format", "json")
                .send(ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();
                        try {
                            JsonObject body = response.bodyAsJsonArray().getJsonObject(0);
                            String lon = body.getString("lon");
                            String lat = body.getString("lat");

                            handler.handle(Future.succeededFuture(address.withGeoLocation(lon, lat)));
                        } catch (DecodeException ex) {
                            logger.error("Error parsing response: ", ex);
                            handler.handle(Future.failedFuture(ex));
                        }

                    } else {
                        logger.error("Something went wrong " + ar.cause().getMessage());
                        handler.handle(Future.failedFuture(ar.cause()));
                    }
                });
    }
}
