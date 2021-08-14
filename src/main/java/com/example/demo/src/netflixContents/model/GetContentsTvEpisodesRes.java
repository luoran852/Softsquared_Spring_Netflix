package com.example.demo.src.netflixContents.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsTvEpisodesRes {
    private int seasonNum;
    private int episodeNum;
    private String episodeTitle;
    private int runningTime;
    private String storyLine;
}
