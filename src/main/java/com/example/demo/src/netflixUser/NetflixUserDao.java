package com.example.demo.src.netflixUser;

import com.example.demo.src.netflixUser.model.GetProfilesRes;
import com.example.demo.src.netflixUser.model.NetflixUser;
import com.example.demo.src.netflixUser.model.PostNetflixLoginReq;
import com.example.demo.src.netflixUser.model.PostNetflixUserReq;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NetflixUserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // [POST] 회원가입
    public int signUp(PostNetflixUserReq postNetflixUserReq){
        String createUserQuery = "insert into User (userName, pwd, email, phoneNum) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postNetflixUserReq.getUserName(), postNetflixUserReq.getPwd(), postNetflixUserReq.getEmail(), postNetflixUserReq.getPhoneNum()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    // [POST] 회원가입
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    // [POST] 로그인
    public NetflixUser logIn(PostNetflixLoginReq postNetflixLoginReq){
        String getPwdQuery = "select idx, email, pwd, userName, phoneNum, membership, status, createdAt, updatedAt from User where email = ?";
        String getPwdParams = postNetflixLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new NetflixUser(
                        rs.getInt("idx"),
                        rs.getString("pwd"),
                        rs.getString("email"),
                        rs.getString("userName"),
                        rs.getString("phoneNum"),
                        rs.getInt("membership"),
                        rs.getInt("status"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt")
                ),
                getPwdParams
        );

    }

    // [GET] 프로필 조회
    public List<GetProfilesRes> getProfiles(int userIdx){
        String getProfilesQuery = "select userIdx, userName, profileImageUrl, profileName, forChild\n" +
                "from Profile P\n" +
                "         inner join User U on P.userIdx = U.idx\n" +
                "where U.idx = ?";
        int getProfilesParams = userIdx;
        return this.jdbcTemplate.query(getProfilesQuery,
                (rs, rowNum) -> new GetProfilesRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("profileImageUrl"),
                        rs.getString("profileName"),
                        rs.getInt("forChild")),
                getProfilesParams);
    }

    public String getPwd(PostNetflixLoginReq postNetflixLoginReq) {
        String getPwdQuery = "select pwd from User where email = ?";
        String getPwdParams = postNetflixLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery, String.class, getPwdParams);

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


}
