package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBoardsRes {
    private int boardIdx;
    private String userName;
    private String title;
    private String contents;
    private String createdAt;
}