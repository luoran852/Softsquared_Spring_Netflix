package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieDetailsRes {
    private int contentsIdx;
    private String previewVideoUrl;
    private String title;
    private int releasedYear;
    private int runtime;
    private int rankingNum;
    private String RemainTime;
    private String plotSummary;
    private String typeTxt;
    private String personName;
    private int isKeep;
    private int isRated;
}
