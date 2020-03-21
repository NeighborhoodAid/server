package de.wirvsvirus.neighborhoodaid;

import io.vertx.core.Launcher;

public class VertxLauncher extends Launcher {

    public static void main(String[] args) {
        new VertxLauncher().dispatch(args);
    }

    @Override
    protected String getMainVerticle() {
        return StarterVerticle.class.getCanonicalName();

    }

}
