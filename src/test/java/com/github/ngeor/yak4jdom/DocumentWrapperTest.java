package com.github.ngeor.yak4jdom;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DocumentWrapperTest {
    @Nested
    class ParseInputStreamTest {
        @Test
        void parse() throws IOException {
            try (InputStream is = getClass().getResourceAsStream("/test.xml")) {
                DocumentWrapper document = DocumentWrapper.parse(is);
                assertThat(document).isNotNull();
                ElementWrapper rootElement = document.getDocumentElement();
                assertThat(rootElement).isNotNull();
            }
        }
    }
}
