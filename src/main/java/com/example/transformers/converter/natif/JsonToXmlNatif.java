package com.example.transformers.converter.natif;

import java.util.*;

public class JsonToXmlNatif {

    public JsonToXmlNatif() {

    }

    public String convert(String jsonInput) {
        try {
            jsonInput = jsonInput.trim();
            Object parsed = parseJson(jsonInput);

            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            if (parsed instanceof Map) {
                Map<String, Object> jsonMap = (Map<String, Object>) parsed;
                for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                    convertToXml(entry.getKey(), entry.getValue(), xml, 0);
                }
            }

            return xml.toString();
        } catch (Exception e) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<error>" + escapeXml(e.getMessage()) + "</error>";
        }
    }

    private Object parseJson(String json) {
        json = json.trim();

        if (json.startsWith("{")) {
            return parseObject(json);
        } else if (json.startsWith("[")) {
            return parseArray(json);
        } else if (json.startsWith("\"")) {
            return unescapeJson(json.substring(1, json.length() - 1));
        } else if (json.equals("true") || json.equals("false")) {
            return Boolean.parseBoolean(json);
        } else if (json.equals("null")) {
            return null;
        } else {
            try {
                if (json.contains(".")) {
                    return Double.parseDouble(json);
                } else {
                    return Integer.parseInt(json);
                }
            } catch (NumberFormatException e) {
                return json;
            }
        }
    }

    private Map<String, Object> parseObject(String json) {
        Map<String, Object> map = new LinkedHashMap<>();
        json = json.substring(1, json.length() - 1).trim();

        if (json.isEmpty()) {
            return map;
        }

        List<String> pairs = splitTopLevel(json, ',');

        for (String pair : pairs) {
            int colonIndex = findTopLevelColon(pair);
            if (colonIndex == -1) continue;

            String key = pair.substring(0, colonIndex).trim();
            String value = pair.substring(colonIndex + 1).trim();

            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }

            map.put(key, parseJson(value));
        }

        return map;
    }

    private List<Object> parseArray(String json) {
        List<Object> list = new ArrayList<>();
        json = json.substring(1, json.length() - 1).trim();

        if (json.isEmpty()) {
            return list;
        }

        List<String> elements = splitTopLevel(json, ',');

        for (String element : elements) {
            list.add(parseJson(element.trim()));
        }

        return list;
    }

    private List<String> splitTopLevel(String str, char delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for (char c : str.toCharArray()) {
            if (escaped) {
                current.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                current.append(c);
                continue;
            }

            if (c == '"') {
                inString = !inString;
                current.append(c);
                continue;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == delimiter && depth == 0) {
                    result.add(current.toString().trim());
                    current = new StringBuilder();
                    continue;
                }
            }

            current.append(c);
        }

        if (current.length() > 0) {
            result.add(current.toString().trim());
        }

        return result;
    }

    private int findTopLevelColon(String str) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '"') {
                inString = !inString;
                continue;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    depth++;
                } else if (c == '}' || c == ']') {
                    depth--;
                } else if (c == ':' && depth == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void convertToXml(String key, Object value, StringBuilder xml, int indent) {
        if (key.equals("#text")) {
            return;
        }

        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;

            addIndent(xml, indent);
            xml.append("<").append(key);

            // Séparer les attributs des éléments
            Map<String, Object> attributes = new LinkedHashMap<>();
            Map<String, Object> elements = new LinkedHashMap<>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();

                if (k.equals("#text")) {
                    continue;
                } else if (!(v instanceof Map) && !(v instanceof List)) {
                    attributes.put(k, v);
                } else {
                    elements.put(k, v);
                }
            }

            // Ajouter les attributs
            for (Map.Entry<String, Object> attr : attributes.entrySet()) {
                xml.append(" ").append(attr.getKey()).append("=\"");
                xml.append(escapeXml(String.valueOf(attr.getValue()))).append("\"");
            }

            // Vérifier le contenu
            boolean hasTextContent = map.containsKey("#text");
            boolean hasElements = !elements.isEmpty();

            if (!hasTextContent && !hasElements) {
                xml.append("/>\n");
            } else if (hasTextContent && !hasElements) {
                xml.append(">");
                xml.append(escapeXml(String.valueOf(map.get("#text"))));
                xml.append("</").append(key).append(">\n");
            } else {
                xml.append(">\n");
                for (Map.Entry<String, Object> entry : elements.entrySet()) {
                    convertToXml(entry.getKey(), entry.getValue(), xml, indent + 2);
                }
                addIndent(xml, indent);
                xml.append("</").append(key).append(">\n");
            }
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            for (Object item : list) {
                convertToXml(key, item, xml, indent);
            }
        } else if (value == null) {
            addIndent(xml, indent);
            xml.append("<").append(key).append("/>\n");
        } else {
            addIndent(xml, indent);
            xml.append("<").append(key).append(">");
            xml.append(escapeXml(String.valueOf(value)));
            xml.append("</").append(key).append(">\n");
        }
    }

    private void addIndent(StringBuilder xml, int indent) {
        for (int i = 0; i < indent; i++) {
            xml.append(" ");
        }
    }

    private String escapeXml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String unescapeJson(String value) {
        return value.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}
