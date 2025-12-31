package com.example.transformers.converter.api;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.json.XML;



public class XmlToJsonApi {

    public XmlToJsonApi() {

    }


    public String convert(String xmlInput) {
        try {
            JSONObject jsonObject = XML.toJSONObject(xmlInput);
            return jsonObject.toString(4);
        }catch(Exception e){
            return "Invalid XML format " + e.getMessage();
        }
    }
}
