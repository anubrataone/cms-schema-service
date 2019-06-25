package com.cb.springdata.sample;

import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.CompoundSchema;
import com.cms.bp.validator.SchemaValidatorResult;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonValidatorTests {

    @Test
    public void vodTitleTest() throws Exception {
        BpSchema p = new CompoundSchema("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate("{\"numberTest\":\"ee\"}").getCode(), is(SchemaValidatorResult.FIELD_INVALID));
        assertThat(p.validate("{\"numberTest\":\"3\"}").getCode(), is(SchemaValidatorResult.SUCCESS));
        assertThat(p.validate("{\"numberTest\":3}").getCode(), is(SchemaValidatorResult.SUCCESS));

    }

    @Test
    public void vodTitleTest2() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": 3\n" +
                "}";
        BpSchema p = new CompoundSchema("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json).getCode(), is(SchemaValidatorResult.SUCCESS));
    }

    @Test
    public void vodTitleTestExpectFailOnVersion() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"ee\"\n" +
                "}";
        BpSchema p = new CompoundSchema("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json).getCode(), is(SchemaValidatorResult.FIELD_INVALID));
    }

    @Test
    public void attrResourceRefTest() throws IOException {
        String json = "{\n" +
                "  \"BPURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"ee\"\n" +
                "}";
        BpSchema p = new CompoundSchema("bpVodTitle");

        p.reloadSchemaDescriptor();
        assertThat(p.validate(json).getCode(), is(SchemaValidatorResult.FIELD_INVALID));
    }

    @Test
    public void attrResourceRefTest2() throws IOException {
        InputStream in = JsonValidatorTests.class.getClassLoader().getResourceAsStream(
                new StringBuilder("bpVodTitleResource.json").toString());

        String jsonData = IOUtils.toString(in, "UTF-8");
        in.close();

        BpSchema p = new CompoundSchema("bpVodTitle");

        p.reloadSchemaDescriptor();
        SchemaValidatorResult validatorResult = p.validate(jsonData);
        System.out.println(validatorResult.getMsg());
        assertThat(validatorResult.getCode(), is(SchemaValidatorResult.SUCCESS));
    }

}
