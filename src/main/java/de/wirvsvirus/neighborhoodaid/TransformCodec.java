package de.wirvsvirus.neighborhoodaid;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.apache.commons.lang3.NotImplementedException;

public class TransformCodec<T> implements MessageCodec<T, T> {
    @Override
    public void encodeToWire(Buffer buffer, T t) {
        throw new NotImplementedException("Not supporting wire decoding/encoding at the moment.");
    }

    @Override
    public T decodeFromWire(int pos, Buffer buffer) {
        throw new NotImplementedException("Not supporting wire decoding/encoding at the moment.");
    }

    @Override
    public T transform(T t) {
        return t;
    }

    @Override
    public String name() {
        return TransformCodec.class.getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
