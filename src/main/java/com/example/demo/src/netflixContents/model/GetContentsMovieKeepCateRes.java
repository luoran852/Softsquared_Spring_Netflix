package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsMovieKeepCateRes {
    private String typeTxt;
    private String categoryTxt;
    private int keepIdx;
    private String keepPosterUrl;
}
