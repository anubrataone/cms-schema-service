package com.cb.springdata.sample;

import com.cms.bp.schema.core.BpSchema;
import com.cms.bp.schema.core.BpSchemaFactory;
import com.cms.bp.schema.core.CompoundSchema;
import com.cms.bp.validator.SchemaValidatorResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonValidatorTests {


    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() throws Exception {

        try (Stream<Path> paths = Files.walk(Paths.get(new URI(JsonValidatorTests.class.getResource("/schemas").toString())))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        System.out.println(path);
                        try {
                            String content = new String(Files.readAllBytes(path));
                            String fileNameWithOutExt = FilenameUtils.removeExtension(path.getFileName().toString());
                            char[] fileNameWithOutExtArr = fileNameWithOutExt.toCharArray();
                            fileNameWithOutExtArr[0] = Character.toUpperCase(fileNameWithOutExtArr[0]);
                            fileNameWithOutExt = new String(fileNameWithOutExtArr);

                            JsonNode schemaNode = mapper.readTree(content);

                            if (schemaNode.get("bpName") != null) {
                                BpSchemaFactory.getInstance().createBpSchemaInstance(schemaNode, "bpName", null, content);
                            } else {
                                BpSchemaFactory.getInstance().createBpSchemaInstance(schemaNode, "attrName", "attrClass", content);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

    }

    public static void main(String args[]) throws Exception {
        try (Stream<Path> paths = Files.walk(Paths.get(new URI(JsonValidatorTests.class.getResource("/schemas").toString())))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        System.out.println(FilenameUtils.removeExtension(path.getFileName().toString()));
                    });
        }
    }

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

        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");

        //  p.reloadSchemaDescriptor();
        SchemaValidatorResult validatorResult = p.validate(jsonData);
        System.out.println(validatorResult.getMsg());
        assertThat(validatorResult.getCode(), is(SchemaValidatorResult.SUCCESS));
    }

}
