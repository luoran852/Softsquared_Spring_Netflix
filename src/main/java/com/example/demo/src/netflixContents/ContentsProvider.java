package com.example.demo.src.netflixContents;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.netflixContents.model.*;
import com.example.demo.src.user.model.*;
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
public class ContentsProvider {

    private final ContentsDao contentsDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ContentsProvider(ContentsDao contentsDao, JwtService jwtService) {
        this.contentsDao = contentsDao;
        this.jwtService = jwtService;
    }

    // [GET] 메인 추천 프로그램 조회 API (TV or 영화)
    public List<GetContentsTvMainRes> getContentsTvMain(int type, int startPage, int pageNum) throws BaseException{
        try{
            List<GetContentsTvMainRes> getContentsTvMainRes = contentsDao.getContentsTvMain(type, startPage, pageNum);
            return getContentsTvMainRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 한국 Top 10 조회 API (TV or 영화)
    public List<GetContentsTvRankingRes> getContentsTvRanking(int type, int startPage, int pageNum) throws BaseException{
        try{
            List<GetContentsTvRankingRes> getContentsTvRankingRes = contentsDao.getContentsTvRanking(type, startPage, pageNum);
            return getContentsTvRankingRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] TV 시청중인 콘텐츠 조회 API
    public List<GetContentsTvWatchingRes> getContentsTvWatching(int type, int userIdxByJwt, int startPage, int pageNum) throws BaseException{
        try{
            List<GetContentsTvWatchingRes> getContentsTvWatchingRes = contentsDao.getContentsTvWatching(type, userIdxByJwt, startPage, pageNum);

            return getContentsTvWatchingRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 요즘뜨는 콘텐츠 조회 API (TV or 영화)
    public List<GetContentsTvTrendingRes> getContentsTvTrending(int type) throws BaseException{
        try{
            List<GetContentsTvTrendingRes> getContentsTvTrendingRes = contentsDao.getContentsTvTrending(type);
            return getContentsTvTrendingRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 내가 찜한 콘텐츠 조회 API (TV or 영화)
    public List<GetContentsTvKeepRes> getContentsTvKeep(int type, int userIdxByJwt) throws BaseException{
        try{
            List<GetContentsTvKeepRes> getContentsTvKeepRes = contentsDao.getContentsTvKeep(type, userIdxByJwt);
            return getContentsTvKeepRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] TV 상세정보 조회 API
    public List<GetContentsTvDetailsRes> getContentsTvDetails(int contentsIdx, int type) throws BaseException{
        try{
            List<GetContentsTvDetailsRes> getContentsTvDetailsRes = contentsDao.getContentsTvDetails(contentsIdx, type);
            return getContentsTvDetailsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] TV 회차 조회 API
    public List<GetContentsTvEpisodesRes> getContentsTvEpisodes(int contentsIdx, int type) throws BaseException{
        try{
            List<GetContentsTvEpisodesRes> getContentsTvEpisodesRes = contentsDao.getContentsTvEpisodes(contentsIdx, type);
            return getContentsTvEpisodesRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // [GET] 영화 시청중인 콘텐츠 조회 API
    public List<GetContentsMovieWatchingRes> getContentsMovieWatching(int type, int userIdxByJwt) throws BaseException{
        try{
            List<GetContentsMovieWatchingRes> getContentsMovieWatchingRes = contentsDao.getContentsMovieWatching(type, userIdxByJwt);
            return getContentsMovieWatchingRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // [GET] 영화 상세정보 조회 API
    public List<GetContentsMovieDetailsRes> getContentsMovieDetails(int contentsIdx, int type) throws BaseException{
        try{
            List<GetContentsMovieDetailsRes> getContentsMovieDetailsRes = contentsDao.getContentsMovieDetails(contentsIdx, type);
            return getContentsMovieDetailsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 영화 메인 추천 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieMainCateRes> getContentsMovieMainCate(int type, int categoryIdx) throws BaseException{
        try{
            List<GetContentsMovieMainCateRes> getContentsMovieMainCateRes = contentsDao.getContentsMovieMainCate(type, categoryIdx);
            return getContentsMovieMainCateRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 영화 한국 Top 10 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieRankingCateRes> getContentsMovieRankingCate(int type, int categoryIdx) throws BaseException{
        try{
            List<GetContentsMovieRankingCateRes> getContentsMovieRankingCateRes = contentsDao.getContentsMovieRankingCate(type, categoryIdx);
            return getContentsMovieRankingCateRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 영화 시청중인 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieWatchingCateRes> getContentsMovieWatchingCate(int type, int categoryIdx, int userIdxByJwt) throws BaseException{
        try{
            List<GetContentsMovieWatchingCateRes> getContentsMovieWatchingCateRes = contentsDao.getContentsMovieWatchingCate(type, categoryIdx, userIdxByJwt);
            return getContentsMovieWatchingCateRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 영화 요즘뜨는 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieTrendingCateRes> getContentsMovieTrendingCate(int type, int categoryIdx) throws BaseException{
        try{
            List<GetContentsMovieTrendingCateRes> getContentsMovieTrendingCateRes = contentsDao.getContentsMovieTrendingCate(type, categoryIdx);
            return getContentsMovieTrendingCateRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 영화 내가 찜한 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieKeepCateRes> getContentsMovieKeepCate(int type, int categoryIdx, int userIdxByJwt) throws BaseException{
        try{
            List<GetContentsMovieKeepCateRes> getContentsMovieKeepCateRes = contentsDao.getContentsMovieKeepCate(type, categoryIdx, userIdxByJwt);
            return getContentsMovieKeepCateRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 공개 예정 조회 API
    public List<GetContentsToBeReleasedRes> getContentsToBeReleased() throws BaseException{
        try{
            List<GetContentsToBeReleasedRes> getContentsToBeReleasedRes = contentsDao.getContentsToBeReleased();
            return getContentsToBeReleasedRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
