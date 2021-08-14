package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieWatchingCateRes {
    private String typeTxt;
    private String categoryTxt;
    private int watchingIdx;
    private String watchingPosterUrl;
    private String watchingRemainTime;
}