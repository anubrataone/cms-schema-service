package com.cb.springdata.sample;

import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.CompoundSchema;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonValidatorTests {

    @Test
    public void vodTitleTest() throws Exception {
        BpSchema p = new CompoundSchema();
        p.setSchemaFileName("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate("{\"numberTest\":\"ee\"}"), is(Boolean.FALSE));
        assertThat(p.validate("{\"numberTest\":\"3\"}"), is(Boolean.TRUE));
        assertThat(p.validate("{\"numberTest\":3}"), is(Boolean.TRUE));

    }

    @Test
    public void vodTitleTest2() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": 3\n" +
                "}";
        BpSchema p = new CompoundSchema();
        p.setSchemaFileName("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json), is(Boolean.TRUE));
    }

    @Test
    public void vodTitleTestExpectFailOnVersion() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"ee\"\n" +
                "}";
        BpSchema p = new CompoundSchema();
        p.setSchemaFileName("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json), is(Boolean.FALSE));
    }

    @Test
    public void attrResourceRefTest() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"ee\"\n" +
                "}";
        BpSchema p = new CompoundSchema();
        p.setSchemaFileName("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json), is(Boolean.FALSE));
    }
}
