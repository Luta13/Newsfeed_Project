package org.sparta.newsfeed.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFIle {
    private String uploadFileName;
    private String storeFileName;
}
