package com.example.practicacomov.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModifyPetition {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("value")
    @Expose
    private String value;

    public ModifyPetition(String name, String field, String value) {
        this.name = name;
        this.field = field;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
