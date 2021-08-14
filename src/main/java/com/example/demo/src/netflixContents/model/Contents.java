package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Contents {
    private int idx;
    private String previewVideoUrl;
    private String title;
    private int releasedYear;
    private int age;
    private int runtime;
    private String plotSummary;
    private int releaseDate;
    private int status;
    private String createdAt;
    private String updatedAt;
    private String posterImgUrl;
}
