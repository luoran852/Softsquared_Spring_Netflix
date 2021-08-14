package com.example.demo.src.netflixUser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NetflixUser {
    private int idx;
    private String email;
    private String pwd;
    private String userName;
    private String phoneNum;
    private int membership;
    private int status;
    private String createdAt;
    private String updatedAt;
}
