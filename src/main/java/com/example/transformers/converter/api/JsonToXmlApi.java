package com.example.transformers.converter.api;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class JsonToXmlApi {

    public JsonToXmlApi() {
    }

    public String convert(String jsonInput) {
        try {
            JSONObject jsonObject = new JSONObject(jsonInput);
            String xmlOutput = XML.toString(jsonObject);
            return formatXml(xmlOutput);
        } catch (Exception e) {
            return "Invalid JSON format " + e.getMessage();
        }
    }

    private String formatXml(String xml) {
        try {
            String wrappedXml = "<root>" + xml + "</root>";

            InputSource src = new InputSource(new StringReader(wrappedXml));
            Node document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(src)
                    .getDocumentElement();

            // Utilisation de LSSerializer (plus moderne et robuste pour le formatage)
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();

            // Configuration du Pretty Print
            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
            writer.getDomConfig().setParameter("xml-declaration", false);

            // On sérialise uniquement les enfants de notre fausse racine "root"
            // Cela permet de récupérer le XML formaté sans la balise <root> ajoutée artificiellement
            StringBuilder sb = new StringBuilder();
            var childNodes = document.getChildNodes();
            for(int i=0; i<childNodes.getLength(); i++) {
                sb.append(writer.writeToString(childNodes.item(i)));
                sb.append("\n"); // Ajout manuel du saut de ligne entre les blocs principaux
            }

            return sb.toString().trim();

        } catch (Exception e) {
            // En cas d'erreur, on retourne le XML brut pour au moins voir le résultat
            System.err.println("Erreur de formatage : " + e.getMessage());
            return xml;
        }
    }
}