package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsTvKeepRes {
    private String typeTxt;
    private int keepIdx;
    private String keepPosterUrl;
}
