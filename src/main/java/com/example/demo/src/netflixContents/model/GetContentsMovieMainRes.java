package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieMainRes {
    private String typeTxt;
    private String categoryList;
    private int contentsIdx;
    private String posterImgUrl;
    private int isKeep;
}
