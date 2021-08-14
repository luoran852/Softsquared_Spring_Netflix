package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieWatchingRes {
    private String typeTxt;
    private int watchingIdx;
    private String watchingPosterUrl;
    private String watchingRemainTime;
}
