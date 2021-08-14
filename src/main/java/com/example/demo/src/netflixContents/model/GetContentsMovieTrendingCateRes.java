package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieTrendingCateRes {
    private String typeTxt;
    private String categoryTxt;
    private int hotIdx;
    private String hotPosterUrl;
}
