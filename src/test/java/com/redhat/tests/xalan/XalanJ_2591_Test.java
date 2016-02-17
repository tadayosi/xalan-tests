package com.redhat.tests.xalan;

import static org.custommonkey.xmlunit.XMLAssert.*;

import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Resources;

public class XalanJ_2591_Test {

    @Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void xsltAttributesWithSecureProcessing() throws Exception {
        Source xsl = new StreamSource(getClass().getResourceAsStream("/XalanJ_2591_Test.xsl"));
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Templates templates = factory.newTemplates(xsl);

        Transformer transformer = templates.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter out = new StringWriter();
        transformer.transform(new StreamSource(getClass().getResourceAsStream("/XalanJ_2591_Test-input.xml")),
                new StreamResult(out));

        System.out.println(out.toString());

        assertXMLEqual(
                Resources.toString(getClass().getResource("/XalanJ_2591_Test-expected.xml"), Charset.defaultCharset()),
                out.toString());
    }

}
