package com.example.demo.src.netflixUser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNetflixUserReq {
    private String userName;
    private String email;
    private String pwd;
    private String phoneNum;
}
