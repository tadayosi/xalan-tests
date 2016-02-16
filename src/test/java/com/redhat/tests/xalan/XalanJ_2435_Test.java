package com.redhat.tests.xalan;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Test case adopted from https://issues.apache.org/jira/browse/XALANJ-2435
 */
public class XalanJ_2435_Test {

    /**
     * Test for java output extensions
     */
    private static final String xsltdocUrlResource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xalan=\"http://xml.apache.org/xalan\"><xsl:output method=\"xml\" xalan:entities=\"http://www.example.org/FileHere\"/><xsl:template match=\"/\"><xsl:message>XSLT message: trying output extensions.</xsl:message><out>OUTPUT TEXT</out></xsl:template></xsl:stylesheet>";
    private static final String xsltdocClassLoad = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xalan=\"http://xml.apache.org/xalan\"><xsl:output method=\"xml\" xalan:content-handler=\"TestXslt$TestContentHandler\" xalan:entities=\"http://www.example.org/example.bin\"/><xsl:template match=\"/\"><xsl:message>XSLT message: trying output extensions.</xsl:message><out>OUTPUT TEXT</out></xsl:template></xsl:stylesheet>";

    @Test
    public void CVE_2014_0107() {
        testXslt("XSLT that accesses URL resource.", xsltdocUrlResource);
        testXslt("XSLT that loads class", xsltdocClassLoad);
    }

    private void testXslt(final String desc, final String xsltDocument) {
        try {
            System.out.println("Testing " + desc);

            // Build DOM for XSLT document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            InputSource source = new InputSource(new ByteArrayInputStream(xsltDocument.getBytes()));
            source.setSystemId("http://example.org/some.xsl");
            final Document doc = dbf.newDocumentBuilder().parse(source);

            // Get TransformerFactory and set secure
            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = tf.newTransformer(new DOMSource(doc));

            // Transform
            transformer.transform(new StreamSource(new StringReader("<document/>")),
                    new StreamResult(new StringWriter()));

            System.out.println("Finished testing " + desc);
        } catch (Exception e) {
            System.out.println("Exception testing " + desc);
            e.printStackTrace(System.out);
        }
    }

    public static final class TestContentHandler {
        static {
            System.out.println("***** TestContentHandler - Loaded *****");
        }
    }
}
