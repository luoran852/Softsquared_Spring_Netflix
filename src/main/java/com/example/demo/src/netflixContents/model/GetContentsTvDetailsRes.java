package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsTvDetailsRes {
    private int contentsIdx;
    private String previewVideoUrl;
    private String title;
    private int releasedYear;
    private int seasonTotalNum;
    private int rankingNum;
    private int seasonNum;
    private int episodeNum;
    private String episodeTitle;
    private String storyLine;
    private String typeTxt;
    private String personName;
    private String remainTime;
    private int isKeep;
    private int isRated;
}

