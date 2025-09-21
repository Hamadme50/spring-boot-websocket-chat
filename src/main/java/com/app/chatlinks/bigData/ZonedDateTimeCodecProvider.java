package com.app.chatlinks.bigData;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.ZonedDateTime;
public class ZonedDateTimeCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == ZonedDateTime.class) {
            return (Codec<T>) new ZonedDateTimeCodec();
        }
        return null;
    }
}