package com.example.demo.src.netflixUser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNetflixLoginReq {
    private String email;
    private String pwd;
}
