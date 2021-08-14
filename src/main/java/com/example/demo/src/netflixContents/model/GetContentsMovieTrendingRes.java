package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieTrendingRes {
    private String typeTxt;
    private int hotIdx;
    private String hotPosterUrl;
}
