package com.example.demo;

import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultipartFileByteArrayEditor extends ByteArrayPropertyEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) value;
            try {
                super.setValue(file.getBytes());
            } catch (IOException e) {
                throw new IllegalArgumentException("Error converting MultipartFile to byte[]", e);
            }
        } else {
            super.setValue(value);
        }
    }
}