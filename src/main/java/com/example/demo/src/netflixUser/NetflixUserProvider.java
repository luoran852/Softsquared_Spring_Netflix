package com.example.demo.src.netflixUser;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.netflixUser.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class NetflixUserProvider {

    private final NetflixUserDao netflixUserDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NetflixUserProvider(NetflixUserDao netflixUserDao, JwtService jwtService) {
        this.netflixUserDao = netflixUserDao;
        this.jwtService = jwtService;
    }


    public int checkEmail(String email) throws BaseException{
        try{
            return netflixUserDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [POST] 로그인 API
    public PostNetflixLoginRes logIn(PostNetflixLoginReq postNetflixLoginReq, int userIdxByJwt) throws BaseException{
//        String email = netflixUserDao.getEmail(postNetflixLoginReq); //db에 저장된 pwd
        String pwd = netflixUserDao.getPwd(postNetflixLoginReq); //db에 저장된 pwd

        // 이메일 존재X
        if (this.checkEmail(postNetflixLoginReq.getEmail()) == 0){
            throw new BaseException(POST_USERS_NO_EXIST_EMAIL);
        }

        String password = null;
        try {
            // 사용자가 입력한 pwd
//            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(postNetflixLoginReq.getPwd());

            //암호화
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postNetflixLoginReq.getPwd());


        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        System.out.println("pwd: " + pwd);
        System.out.println("password: " + password);

        // 비밀번호 비교
        if (!pwd.equals(password)) {
            throw new BaseException(PASSWORD_DIFFERENT_ERROR);
        }

        try {
            int idx = netflixUserDao.logIn(postNetflixLoginReq).getIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(idx != userIdxByJwt){
                throw new BaseException(INVALID_USER_JWT);
            }

            return new PostNetflixLoginRes(idx);

        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    // [GET] 프로필 조회 API
    public List<GetProfilesRes> getProfiles(int userIdx) throws BaseException {
        try {
            List<GetProfilesRes> getProfilesRes = netflixUserDao.getProfiles(userIdx);
            return getProfilesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


//    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
//        try{
//            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


}
