package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsTvRankingRes {
    private String typeTxt;
    private int rankingNum;
    private int contentsIdx;
    private String posterImgUrl;
}
