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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class JsonValidatorTests {


    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() throws Exception {

        try (Stream<Path> paths = Files.walk(Paths.get(new URI(JsonValidatorTests.class.getResource("/schemas").toString())))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {

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

    @Test
    public void validateBpUrnPassRequired() throws IOException {
        String json = "{\n" +
                "  \"bpURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"EE\"\n" +
                "}";
        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");
        SchemaValidatorResult validateResult = p.validate(json);

        assertThat(validateResult.getMsg().toString(), containsString("bpURN pass 'required' validation step"));
    }

    @Test
    public void validateMissingRequiredEntityType() throws IOException {
        String json = "{\n" +
                "  \"bpURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"EE\"\n" +
                "}";
        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");
        SchemaValidatorResult validateResult = p.validate(json);

        assertThat(validateResult.getMsg().toString(), containsString("Missing Required Node - entityType"));
    }


    @Test
    public void validateBpVersionIsNotANumber() throws IOException {
        String json = "{\n" +
                "  \"bpURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"EE\"\n" +
                "}";
        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");
        SchemaValidatorResult validateResult = p.validate(json);

        assertThat(validateResult.getMsg().toString(), containsString("\"EE\": is not a number"));
    }

    @Test
    public void validateBpVersionIsANumber() throws IOException {
        String json = "{\n" +
                "  \"bpURN\": \"BP URN Value Here\",\n" +
                "  \"BPName\": \"BP Name here\",\n" +
                "  \"bpVersion\": \"1\"\n" +
                "}";
        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");
        SchemaValidatorResult validateResult = p.validate(json);

        assertThat(validateResult.getMsg().toString(), containsString("Valid - Node Name:bpVersion"));
    }
    //fail-on-image-validating-vod-title-resource.json
    @Test
    public void successValidatingVodTitleResource() throws IOException {
        InputStream in = JsonValidatorTests.class.getClassLoader().getResourceAsStream(
                new StringBuilder("success-validating-vod-title-resource.json").toString());

        String jsonData = IOUtils.toString(in, "UTF-8");
        in.close();

        BpSchema p = BpSchemaFactory.getInstance().getSchemaByName("VODTitle");

        //  p.reloadSchemaDescriptor();
        SchemaValidatorResult validatorResult = p.validate(jsonData);
        System.out.println(validatorResult.getMsg());
        assertThat(validatorResult.getCode(), is(SchemaValidatorResult.SUCCESS));
    }

}
