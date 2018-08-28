package com.example.springautorestdocsundocumented;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringAutoRestDocsUndocumentedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAutoRestDocsUndocumentedApplication.class, args);
    }

    /**
     * Controller.
     */
    @RestController
    static class MyController {

        @GetMapping("/classic")
        public RestDocs classicRestDoc() {
            return new RestDocs("field");
        }

        @GetMapping("/auto")
        public AutoRestDocs autoRestDoc() {
            return new AutoRestDocs("field");
        }
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class RestDocs {
        private final String field;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AutoRestDocs {
        /**
         * Field.
         */
        private final String field;
    }
}
