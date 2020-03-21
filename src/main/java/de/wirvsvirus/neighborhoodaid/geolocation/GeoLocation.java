package de.wirvsvirus.neighborhoodaid.geolocation;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class GeoLocation {
    public static void ConvertGeoLocation(String Street, String City, String Country, Vertx vertx){
        WebClient client = WebClient.create(vertx);

        client
                .get(80,"nominatim.openstreetmap.org","search?street=" + Street + "&city=" +City+"&country="+Country+"&format=json")
                .send(ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();

                        JsonObject body = response.bodyAsJsonArray().getJsonObject(0);
                        String lat = body.getString("lat");
                        String lon = body.getString("lon");


                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                });
    }


}
