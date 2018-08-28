package com.example.springautorestdocsundocumented;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SpringAutoRestDocsUndocumentedApplication.MyController.class)
public class SpringAutoRestDocsUndocumentedApplicationTests {

    private MockMvc mvc;
    private RestDocumentationResultHandler documentationHandler;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {

        documentationHandler = document("{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                .alwaysDo(documentationHandler)
                .apply(documentationConfiguration(restDocumentation)
                        .uris().withScheme("http").withHost("localhost").withPort(8080)
                        .and().snippets()
                        .withDefaults(
                                AutoDocumentation.requestFields().failOnUndocumentedFields(true),
                                AutoDocumentation.responseFields().failOnUndocumentedFields(true),
                                AutoDocumentation.pathParameters().failOnUndocumentedParams(true),
                                AutoDocumentation.requestParameters().failOnUndocumentedParams(true)))
                .build();
    }

    @Test
    public void classic_rest_docs() throws Exception {
        // this will fail because my field is not documented by javadoc
        this.mvc.perform(get("/classic"))
                .andExpect(status().isOk())
                .andDo(this.documentationHandler.document(
                        responseFields(
                                fieldWithPath("field").description("My field in classic rest doc")
                        )
                ));
    }

    @Test
    public void auto_rest_docs() throws Exception {
        this.mvc.perform(get("/auto"))
                .andExpect(status().isOk());
    }
}
