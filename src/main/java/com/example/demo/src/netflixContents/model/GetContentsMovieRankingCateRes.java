package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieRankingCateRes {
    private String typeTxt;
    private String categoryTxt;
    private int rankingNum;
    private int contentsIdx;
    private String posterImgUrl;
}
