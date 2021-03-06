package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.PatchBoardReq;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BoardDao boardDao;
    private final BoardProvider boardProvider;
    private final JwtService jwtService;


    @Autowired
    public BoardService(BoardDao boardDao, BoardProvider boardProvider, JwtService jwtService) {
        this.boardDao = boardDao;
        this.boardProvider = boardProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostBoardRes postOnBoard(PostBoardReq postBoardReq) throws BaseException {

        try{
            int userIdx = boardDao.postOnBoard(postBoardReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostBoardRes(jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // PATCH
    public void modifyBoard(PatchBoardReq patchBoardReq, int boardIdx) throws BaseException {
        try{
            int result = boardDao.modifyBoard(patchBoardReq, boardIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_BOARD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // DELETE
    public void deleteBoard(int boardIdx) throws BaseException {
        try{
            int result = boardDao.deleteBoard(boardIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_BOARD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
