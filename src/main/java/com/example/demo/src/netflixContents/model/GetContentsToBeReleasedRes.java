package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsToBeReleasedRes {
    private int contentsIdx;
    private String releaseDate;
    private String previewVideoUrl;
    private String title;
    private int isAlarm;
    private String typeTxt;
    private String storyLine;
    private String categoryList;
}
