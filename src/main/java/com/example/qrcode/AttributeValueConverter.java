package com.example.qrcode;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class AttributeValueConverter {
    public static AttributeValue fromJavaValue(Object o) {
        if (o instanceof String) {
            return AttributeValue.builder().s((String) o).build();
        } else if (o instanceof Integer) {
            return AttributeValue.builder().n(o.toString()).build();
        } else if (o instanceof Long) {
            return AttributeValue.builder().n(o.toString()).build();
        } else if (o instanceof Double) {
            return AttributeValue.builder().n(o.toString()).build();
        } else if (o instanceof Float) {
            return AttributeValue.builder().n(o.toString()).build();
        } else if (o instanceof Boolean) {
            return AttributeValue.builder().bool((Boolean) o).build();
        } else if (o instanceof byte[]) {
            return AttributeValue.builder().b(SdkBytes.fromByteArray((byte[]) o)).build();
        } else if (o == null) {
            return AttributeValue.builder().nul(true).build();
        } else {
            throw new IllegalArgumentException("Unsupported attribute type: " + o.getClass().getName());
        }
    }
}
