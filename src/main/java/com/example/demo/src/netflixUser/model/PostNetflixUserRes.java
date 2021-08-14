package com.example.demo.src.netflixUser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNetflixUserRes {
    private String jwt;
    private int idx;
}
