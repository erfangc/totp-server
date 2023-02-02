package com.example.qrcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

public class JsonToDynamoDb {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, AttributeValue> convert(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        Map<String, AttributeValue> item = jsonNodeToAttributeValueMap(jsonNode);
        return item;
    }

    private static Map<String, AttributeValue> jsonNodeToAttributeValueMap(JsonNode node) {
        Map<String, AttributeValue> result = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (value.isObject()) {
                result.put(key, AttributeValue.builder().m(jsonNodeToAttributeValueMap(value)).build());
            } else if (value.isArray()) {
                List<AttributeValue> attributeValues = new ArrayList<>();
                for (JsonNode item : value) {
                    attributeValues.add(AttributeValue.builder().m(jsonNodeToAttributeValueMap(item)).build());
                }
                result.put(key, AttributeValue.builder().l(attributeValues).build());
            } else if (value.isBoolean()) {
                result.put(key, AttributeValue.builder().bool(value.asBoolean()).build());
            } else if (value.isNumber()) {
                result.put(key, AttributeValue.builder().n(value.asText()).build());
            } else if (value.isTextual()) {
                result.put(key, AttributeValue.builder().s(value.asText()).build());
            }
        }
        return result;
    }

    public static void main(String[] args) throws JsonProcessingException {
        Map<String, AttributeValue> item = convert("""
                { "name": "Dude", "score": 0.2, "active": true, "other": null, "hobbies": [{"name": "reading"}, {"name": "food"}]}
                """);
        System.out.println(item);
    }
}
