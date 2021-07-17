package com.example.demo.src.board;

import com.example.demo.src.board.model.GetBoardsRes;
import com.example.demo.src.board.model.PatchBoardReq;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BoardDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET
    public List<GetBoardsRes> getBoards (){
        String getBoardsQuery = "select * from Board";
        return this.jdbcTemplate.query(getBoardsQuery,
                (rs,rowNum) -> new GetBoardsRes(
                        rs.getInt("boardIdx"),
                        rs.getString("userName"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getString("createdAt"))
        );
    }


    // POST
    public int postOnBoard(PostBoardReq postBoardReq){
        String postOnBoardQuery = "insert into Board (userName, title, contents) VALUES (?,?,?)";
        Object[] postOnBoardParams = new Object[]{postBoardReq.getUserName(), postBoardReq.getTitle(), postBoardReq.getContents()};

        return this.jdbcTemplate.update(postOnBoardQuery, postOnBoardParams);
    }

//    public int createUser(PostUserReq postUserReq){
//        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
//        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        String lastInserIdQuery = "select last_insert_id()";
//        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
//    }


    // PATCH
    public int modifyBoard(PatchBoardReq patchBoardReq, int boardIdx){
        String modifyBoardQuery = "update Board set title = ?, contents = ? where boardIdx = ?";
        Object[] modifyBoardParams = new Object[]{patchBoardReq.getTitle(), patchBoardReq.getContents(), boardIdx};

        return this.jdbcTemplate.update(modifyBoardQuery,modifyBoardParams);
    }

    // DELETE
    public int deleteBoard(int boardIdx){
        String deleteBoardQuery = "delete from Board where boardIdx = ? ";

        return this.jdbcTemplate.update(deleteBoardQuery, boardIdx);
    }



}

