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

import static java.lang.Math.*;

public class GeoLocation {

    private final static Logger logger = LoggerFactory.getLogger(GeoLocation.class);

    public static double sphericalDistance(double lon1, double lat1, double lon2, double lat2) {
        // https://stackoverflow.com/a/27943/5401763
        // Straight up stolen code:
        double earthRadius = 6371;
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = pow(sin(dLat / 2), 2) +
                cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                        pow(sin(dLon / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return earthRadius * c;
    }

    public static double sphericalDistance(Address from, Address to) {
        return sphericalDistance(Double.parseDouble(from.getLongitude()), Double.parseDouble(from.getLatitude()),
                Double.parseDouble(to.getLongitude()), Double.parseDouble(to.getLatitude()));
    }

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
