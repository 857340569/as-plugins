package com.onlypxz.plugins.autofindviews.entity;

/**
 * Created by tracker on 2017/6/23.
 */
public class XmlElement {
    private String fieldType;//变量类型
    private String fieldName;//变量名

    public XmlElement(String fieldType, String fieldName) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
