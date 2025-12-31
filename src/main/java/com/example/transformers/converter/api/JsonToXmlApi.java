package com.example.transformers.converter.api;

import org.json.JSONObject;
import org.json.XML;

public class JsonToXmlApi {

    public JsonToXmlApi() {

    }

    public String convert(String jsonInput) {

        try{
            JSONObject jsonObject = new JSONObject(jsonInput);
            String xmlOutput = XML.toString(jsonObject);
            return formatXml(xmlOutput);
        } catch (Exception e) {
            return "Invalid JSON format " + e.getMessage();
        }
    }

    private String formatXml(String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new java.io.ByteArrayInputStream(xml.getBytes()));

            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            return xml; // Retourner le XML non formaté en cas d'erreur
        }
    }
}
