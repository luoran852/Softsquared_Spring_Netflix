package com.example.demo.src.board;

import com.example.demo.src.board.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/boards")
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    @Autowired
    private final BoardService boardService;
    @Autowired
    private final JwtService jwtService;




    public BoardController(BoardProvider boardProvider, BoardService boardService, JwtService jwtService){
        this.boardProvider = boardProvider;
        this.boardService = boardService;
        this.jwtService = jwtService;
    }

    /**
     * 게시판 글 가져오기 API
     * [GET] /boards
     * @return BaseResponse<GetBoardsRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/boards
    public BaseResponse<List<GetBoardsRes>> getBoards() {
        try{
            // Get Boards
            List<GetBoardsRes> getBoardsRes = boardProvider.getBoards();
            return new BaseResponse<>(getBoardsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 게시판 글 올리기 API
     * [POST] /boards
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBoardRes> postOnBoard(@RequestBody PostBoardReq postBoardReq) {
        // 이름, 제목, 내용이 모두 입력되었는지 확인
//        if(postBoardReq.getUserName() == null || postBoardReq.getTitle() == null || postBoardReq.getContents() == null){
//            return new BaseResponse<>(POST_BOARDS_EMPTY_VALUES);
//        }
        try{
            PostBoardRes postBoardRes = boardService.postOnBoard(postBoardReq);
            return new BaseResponse<>(postBoardRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 게시판글 수정하기 API
     * [PATCH]/boards
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{boardIdx}")
    public BaseResponse<String> modifyBoard(@PathVariable("boardIdx") int boardIdx, @RequestBody Board board) {
        try {
//            //jwt에서 idx추출.
//            int boardIdxByJwt = jwtService.getBoardIdx();
//            //boardIdx와 접근한 게시글이 같은지 확인
//            if (boardIdx != boardIdxByJwt) {
//                return new BaseResponse<>(INVALID_BOARD_JWT);
//            }
            //같다면제목,내용변경
            PatchBoardReq patchBoardReq = new PatchBoardReq(board.getTitle(), board.getContents());
            boardService.modifyBoard(patchBoardReq, boardIdx);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시판 글 지우기 API
     * [DELETE] /boards/:boardIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{boardIdx}")
    public BaseResponse<String> deleteBoard(@PathVariable("boardIdx") int boardIdx) {
        try {
//            //jwt에서 idx추출.
//            int boardIdxByJwt = jwtService.getBoardIdx();
//            //boardIdx와 접근한 게시글이 같은지 확인
//            if (boardIdx != boardIdxByJwt) {
//                return new BaseResponse<>(INVALID_BOARD_JWT);
//            }
            //같다면 해당 게시글 삭제
//            DeleteBoardReq deleteBoardReq = new DeleteBoardReq(boardIdx);
            boardService.deleteBoard(boardIdx);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
