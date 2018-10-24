package io.sparkled.rest.json;

import io.sparkled.model.util.GsonProvider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UnsignedByteArrayTypeAdapterTest {

    @Test
    public void serialize() throws Exception {
        byte[] bytes = new byte[]{0, 1, 127, -128, -2, -1};
        String json = GsonProvider.get().toJson(bytes);
        assertThat(json, is("[0,1,127,128,254,255]"));
    }
}