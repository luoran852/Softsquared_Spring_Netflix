package com.example.demo.src.board;

import com.example.demo.src.board.model.DeleteBoardReq;
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

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }

    // POST
    public int postOnBoard(PostBoardReq postBoardReq){
        String postOnBoardQuery = "insert into Board (userName, title, contnets) VALUES (?,?,?)";
        Object[] postOnBoardParams = new Object[]{postBoardReq.getUserName(), postBoardReq.getTitle(), postBoardReq.getContents()};
        this.jdbcTemplate.update(postOnBoardQuery, postOnBoardParams);

        return this.jdbcTemplate.queryForObject(postOnBoardQuery,int.class);
    }

//    public int createUser(PostUserReq postUserReq){
//        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
//        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        String lastInserIdQuery = "select last_insert_id()";
//        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
//    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    // PATCH
    public int modifyBoard(PatchBoardReq patchBoardReq){
        String modifyBoardQuery = "update Board set title = ? and contents = ? where boardIdx = ? ";
        Object[] modifyBoardParams = new Object[]{patchBoardReq.getTitle(), patchBoardReq.getContents(), patchBoardReq.getBoardIdx()};

        return this.jdbcTemplate.update(modifyBoardQuery,modifyBoardParams);
    }

    // DELETE
    public int deleteBoard(DeleteBoardReq deleteBoardReq){
        String deleteBoardQuery = "delete Board * where boardIdx = ? ";
        Object[] deleteBoardParams = new Object[]{deleteBoardReq.getBoardIdx()};

        return this.jdbcTemplate.update(deleteBoardQuery,deleteBoardParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
        );

    }



}

