package com.example.demo.src.netflixUser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProfilesRes {
    private int userIdx;
    private String userName;
    private String profileImageUrl;
    private String profileName;
    private int forChild;
}
