package com.example.demo.src.netflixUser;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.netflixUser.model.PostNetflixUserReq;
import com.example.demo.src.netflixUser.model.PostNetflixUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class NetflixUserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NetflixUserDao netflixUserDao;
    private final NetflixUserProvider netflixUserProvider;
    private final JwtService jwtService;


    @Autowired
    public NetflixUserService(NetflixUserDao netflixUserDao, NetflixUserProvider netflixUserProvider, JwtService jwtService) {
        this.netflixUserDao = netflixUserDao;
        this.netflixUserProvider = netflixUserProvider;
        this.jwtService = jwtService;

    }

    // [POST] 회원가입 API
    public PostNetflixUserRes signUp(PostNetflixUserReq postNetflixUserReq) throws BaseException {

        // 이메일 중복체크
        if (netflixUserProvider.checkEmail(postNetflixUserReq.getEmail()) == 1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            // 비밀번호 암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postNetflixUserReq.getPwd());
            postNetflixUserReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            int idx = netflixUserDao.signUp(postNetflixUserReq);
            // jwt 발급
            String jwt = jwtService.createJwt(idx);
            return new PostNetflixUserRes(jwt, idx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
