package com.example.transformers.converter.natif;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.StringReader;
import java.util.*;

public class XmlToJsonNatif {

    public XmlToJsonNatif() {

    }

    public String convert(String xmlInput) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlInput)));

            Element root = document.getDocumentElement();
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"").append(root.getNodeName()).append("\": ");
            convertElementToJson(root, json, 2);
            json.append("\n}");

            return json.toString();
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private void convertElementToJson(Element element, StringBuilder json, int indent) {
        // Regrouper les enfants par nom
        Map<String, List<Element>> childrenByName = new LinkedHashMap<>();
        NodeList children = element.getChildNodes();
        String textContent = null;

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;
                String name = childElement.getNodeName();
                childrenByName.computeIfAbsent(name, k -> new ArrayList<>()).add(childElement);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getTextContent().trim();
                if (!text.isEmpty()) {
                    textContent = text;
                }
            }
        }

        // Récupérer les attributs
        NamedNodeMap attributes = element.getAttributes();
        boolean hasAttributes = attributes.getLength() > 0;
        boolean hasChildren = !childrenByName.isEmpty();
        boolean hasTextContent = textContent != null;

        // Cas 1: Élément avec seulement du texte (pas d'attributs ni d'enfants)
        if (!hasAttributes && !hasChildren && hasTextContent) {
            json.append("\"").append(escapeJson(textContent)).append("\"");
            return;
        }

        // Cas 2: Élément vide (pas d'attributs, pas d'enfants, pas de texte)
        if (!hasAttributes && !hasChildren && !hasTextContent) {
            json.append("null");
            return;
        }

        // Cas 3: Élément avec attributs et/ou enfants
        json.append("{\n");

        int itemsAdded = 0;

        // Ajouter les attributs comme éléments normaux (sans @)
        if (hasAttributes) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                if (itemsAdded > 0) {
                    json.append(",\n");
                }
                addIndent(json, indent);
                json.append("\"").append(attr.getNodeName()).append("\": ");
                json.append("\"").append(escapeJson(attr.getNodeValue())).append("\"");
                itemsAdded++;
            }
        }

        // Ajouter le contenu texte si présent avec enfants ou attributs
        if (hasTextContent && (hasChildren || hasAttributes)) {
            if (itemsAdded > 0) {
                json.append(",\n");
            }
            addIndent(json, indent);
            json.append("\"#text\": \"").append(escapeJson(textContent)).append("\"");
            itemsAdded++;
        }

        // Traiter les éléments enfants
        for (Map.Entry<String, List<Element>> entry : childrenByName.entrySet()) {
            if (itemsAdded > 0) {
                json.append(",\n");
            }
            addIndent(json, indent);
            json.append("\"").append(entry.getKey()).append("\": ");

            List<Element> elements = entry.getValue();
            if (elements.size() > 1) {
                // Plusieurs éléments avec le même nom → Array
                json.append("[\n");
                for (int i = 0; i < elements.size(); i++) {
                    addIndent(json, indent + 2);
                    convertElementToJson(elements.get(i), json, indent + 2);
                    if (i < elements.size() - 1) {
                        json.append(",");
                    }
                    json.append("\n");
                }
                addIndent(json, indent);
                json.append("]");
            } else {
                // Un seul élément
                convertElementToJson(elements.get(0), json, indent + 2);
            }
            itemsAdded++;
        }

        json.append("\n");
        addIndent(json, indent - 2);
        json.append("}");
    }

    private void addIndent(StringBuilder json, int indent) {
        for (int i = 0; i < indent; i++) {
            json.append(" ");
        }
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
