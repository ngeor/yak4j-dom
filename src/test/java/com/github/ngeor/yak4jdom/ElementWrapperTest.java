package com.github.ngeor.yak4jdom;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementWrapperTest {
    @Nested
    class PathTest {
        @Test
        void test() {
            String contents = """
                <project>
                    <parent>
                        <groupId />
                    </parent>
                </project>""";
            DocumentWrapper document = DocumentWrapper.parseString(contents);
            ElementWrapper rootElement = document.getDocumentElement();
            assertThat(rootElement.path()).isEqualTo("project");
            ElementWrapper firstChild = rootElement.firstElement("parent").orElseThrow();
            assertThat(firstChild.path()).isEqualTo("project/parent");
            ElementWrapper firstGrandChild = firstChild.firstElement("groupId").orElseThrow();
            assertThat(firstGrandChild.path()).isEqualTo("project/parent/groupId");
        }
    }

    @Nested
    class TestXmlTest {
        private ElementWrapper rootElement;

        @BeforeEach
        void beforeEach() throws IOException {
            try (InputStream is = getClass().getResourceAsStream("/test.xml")) {
                DocumentWrapper document = DocumentWrapper.parse(is);
                assertThat(document).isNotNull();
                rootElement = document.getDocumentElement();
            }
        }

        @Test
        void getNodeName() {
            assertThat(rootElement.getNodeName()).isEqualTo("test");
        }

        @Test
        void getChildElements() {
            assertThat(rootElement.getChildElements())
                    .extracting(ElementWrapper::getNodeName)
                    .containsOnly("greeting");
        }

        @Test
        void getTextContent() {
            assertThat(rootElement.getChildElements())
                    .extracting(ElementWrapper::getTextContent)
                    .containsOnly("Hello, world");
        }

        @Test
        void firstElement_notFound() {
            assertThat(rootElement.firstElement("hello")).isEmpty();
        }

        @Test
        void firstElement_found() {
            assertThat(rootElement.firstElement("greeting"))
                    .isNotEmpty()
                    .hasValueSatisfying(e -> assertThat(e.getTextContent()).isEqualTo("Hello, world"));
        }

        @Test
        void firstElementText_notFound() {
            assertThat(rootElement.firstElementText("hello")).isNull();
        }

        @Test
        void firstElementText_found() {
            assertThat(rootElement.firstElementText("greeting")).isEqualTo("Hello, world");
        }
    }
}
