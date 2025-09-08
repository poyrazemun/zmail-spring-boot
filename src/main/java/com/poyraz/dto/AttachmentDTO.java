package com.poyraz.dto;

import java.util.Arrays;
import java.util.Objects;

public record AttachmentDTO(String fileName, String contentType, byte[] data) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentDTO that = (AttachmentDTO) o;
        return Objects.deepEquals(data, that.data) && Objects.equals(fileName, that.fileName) && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, contentType, Arrays.hashCode(data));
    }

    @Override
    public String toString() {
        return "AttachmentDTO{" +
                "fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
