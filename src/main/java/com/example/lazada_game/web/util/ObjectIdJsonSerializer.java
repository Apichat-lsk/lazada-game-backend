package com.example.lazada_game.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdJsonSerializer extends StdSerializer<ObjectId> {

    public ObjectIdJsonSerializer() {
        this(null);
    }

    public ObjectIdJsonSerializer(Class<ObjectId> t) {
        super(t);
    }

    @Override
    public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toHexString());
    }
}