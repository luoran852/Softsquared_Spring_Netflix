package com.example.demo.src.netflixUser;

import com.example.demo.src.netflixUser.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class NetflixUserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final NetflixUserProvider netflixUserProvider;
    @Autowired
    private final NetflixUserService netflixUserService;
    @Autowired
    private final JwtService jwtService;




    public NetflixUserController(NetflixUserProvider netflixUserProvider, NetflixUserService netflixUserService, JwtService jwtService){
        this.netflixUserProvider = netflixUserProvider;
        this.netflixUserService = netflixUserService;
        this.jwtService = jwtService;
    }


    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostNetflixUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostNetflixUserRes> signUp(@RequestBody PostNetflixUserReq postNetflixUserReq) {

        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        // 빈값 체크
        if(postNetflixUserReq.getEmail().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(postNetflixUserReq.getPwd().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        if(postNetflixUserReq.getUserName().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_USERNAME);
        }

        // 이메일 정규표현
        if(!isRegexEmail(postNetflixUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostNetflixUserRes postNetflixUserRes = netflixUserService.signUp(postNetflixUserReq);
            return new BaseResponse<>(postNetflixUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

         //비밀번호 길이 추가
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostNetflixLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostNetflixLoginRes> logIn(@RequestBody PostNetflixLoginReq postNetflixLoginReq) throws BaseException {

        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();

        try{
            // TODO: 로그인 값들에 대한 형식적인 validation 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.

            // 이메일, 비번 빈값 체크
            if (postNetflixLoginReq.getEmail().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            if (postNetflixLoginReq.getEmail().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            PostNetflixLoginRes postNetflixLoginRes = netflixUserProvider.logIn(postNetflixLoginReq, userIdxByJwt);
            return new BaseResponse<>(postNetflixLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 조회 API
     * [GET] /users/profiles/:userIdx
     * @return BaseResponse<GetProfileRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/profiles/{userIdx}") // (GET) 15.165.16.88:9000/users/:userIdx
    public BaseResponse<List<GetProfilesRes>> getProfiles(@PathVariable("userIdx") int userIdx) {
        // Get Profiles
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetProfilesRes> getProfilesRes = netflixUserProvider.getProfiles(userIdx);
            return new BaseResponse<>(getProfilesRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 카카오 로그인 API
     * [POST] /users/kakao-login
     * @return BaseResponse<GetKakaoLoginRes>
     */
//    private KakaoLogin kakao_restapi = new KakaoLogin();

    @ResponseBody
    @GetMapping("/kakao/oauth") // (GET) 15.165.16.88:9000/users/kakao-login
    public String kakaoConnect() {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "d3af1f91d47223feb24a279ac2a7cbf7");
        url.append("&redirect_uri=https://candy-softsquared.shop/users/kakao/callback");
        url.append("&response_type=code");

        return "redirect:" + url.toString();
    }

    @RequestMapping(value="/kakao/callback",produces="application/json",method= {RequestMethod.GET, RequestMethod.POST})
    public void kakaoLogin(@RequestParam("code")String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response, Model model)throws IOException {

        System.out.println("kakao code:"+code);
    }

//    public void getKakaoAccessToken(String code) {
//        final String RequestUrl = "https://kauth.kakao.com/oauth/token"; // Host
//        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//
//        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
//        postParams.add(new BasicNameValuePair("client_id", "d3af1f91d47223feb24a279ac2a7cbf7")); // REST API KEY
//        postParams.add(new BasicNameValuePair("redirect_uri", "https://candy-softsquared.shop/users/kakao/callback")); // 리다이렉트 URI
//        postParams.add(new BasicNameValuePair("code", "bGw7ZmmygaPAW7_OCmi_731tjzW-wkATKR4HTTM7iG1s5iF7lInGbRhVLGVd-xdMeHDEGQorDNIAAAF7PbL5Hg")); // 로그인 과정중 얻은 code 값
//
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(RequestUrl);
//
//
//        try {
//            post.setEntity(new UrlEncodedFormEntity(postParams));
//
//            final HttpResponse response = client.execute(post);
//            final int responseCode = response.getStatusLine().getStatusCode();
//
//            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
//            System.out.println("Post parameters : " + postParams);
//            System.out.println("Response Code : " + responseCode);
//
//            // JSON 형태 반환값 처리
//            ObjectMapper mapper = new ObjectMapper();
//
//            returnNode = mapper.readTree(response.getEntity().getContent());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public JsonNode getKakaoAccessToken(String code) {
//        final String RequestUrl = "https://kauth.kakao.com/oauth/token"; // Host
//        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//
//        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
//        postParams.add(new BasicNameValuePair("client_id", "d3af1f91d47223feb24a279ac2a7cbf7")); // REST API KEY
//        postParams.add(new BasicNameValuePair("redirect_uri", "https://candy-softsquared.shop/users/kakao/callback")); // 리다이렉트 URI
//        postParams.add(new BasicNameValuePair("code", code)); // 로그인 과정중 얻은 code 값
//
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(RequestUrl);
//
//        JsonNode returnNode = null;
//
//        try {
//            post.setEntity(new UrlEncodedFormEntity(postParams));
//
//            final HttpResponse response = client.execute(post);
//            final int responseCode = response.getStatusLine().getStatusCode();
//
//            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
//            System.out.println("Post parameters : " + postParams);
//            System.out.println("Response Code : " + responseCode);
//
//            // JSON 형태 반환값 처리
//            ObjectMapper mapper = new ObjectMapper();
//
//            returnNode = mapper.readTree(response.getEntity().getContent());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return returnNode;
//    }



}

