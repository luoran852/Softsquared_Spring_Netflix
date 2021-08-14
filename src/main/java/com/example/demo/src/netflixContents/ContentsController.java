package com.example.demo.src.netflixContents;

import com.example.demo.src.board.model.*;
import com.example.demo.src.netflixContents.model.*;
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
@RequestMapping("/contents")
public class ContentsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ContentsProvider contentsProvider;
    @Autowired
    private final ContentsService contentsService;
    @Autowired
    private final JwtService jwtService;




    public ContentsController(ContentsProvider contentsProvider, ContentsService contentsService, JwtService jwtService){
        this.contentsProvider = contentsProvider;
        this.contentsService = contentsService;
        this.jwtService = jwtService;
    }

    /**
     * 메인 추천 프로그램 조회 API (TV or 영화)
     * [GET] /contents/main?type=1
     * @return BaseResponse<GetContentsTvMainRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/main") // (GET) 15.165.16.88:9000/contents/main?type=1
    public BaseResponse<List<GetContentsTvMainRes>> getContentsTvMain(@RequestParam int type, @RequestParam(required = false) int page, @RequestParam(required = false) int size) {

        try{
            // @RequestParam default value 추가
            // paging 가공
            int startPage = (page - 1) * size;
            int pageNum = size;

            // Get TV main contents
            List<GetContentsTvMainRes> getContentsTvMainRes = contentsProvider.getContentsTvMain(type, startPage, pageNum);
            return new BaseResponse<>(getContentsTvMainRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 한국 Top 10 조회 API (TV or 영화)
     * [GET] /contents/ranking?type=1
     * @return BaseResponse<GetContentsTvRankingRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/ranking") // (GET) (GET) 15.165.16.88:9000/contents/ranking?type=1
    public BaseResponse<List<GetContentsTvRankingRes>> getContentsTvRanking(@RequestParam int type, @RequestParam int page, @RequestParam int size) {

        try{
            // paging 가공
            int startPage = (page - 1) * size;
            int pageNum = size;

            // Get TV main contents
            List<GetContentsTvRankingRes> getContentsTvRankingRes = contentsProvider.getContentsTvRanking(type, startPage, pageNum);
            return new BaseResponse<>(getContentsTvRankingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * TV 시청중인 콘텐츠 조회 API
     * [GET] /contents/tvWatching?type=1
     * @return BaseResponse<GetContentsTvWatchingRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/tvWatching") // (GET) 15.165.16.88:9000/contents/tvWatching?type=1
    public BaseResponse<List<GetContentsTvWatchingRes>> getContentsTvWatching(@RequestParam int type, @RequestParam int page, @RequestParam int size) {

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // paging 가공
            int startPage = (page - 1) * size;
            int pageNum = size;

            // Get TV main contents
            List<GetContentsTvWatchingRes> getContentsTvWatchingRes = contentsProvider.getContentsTvWatching(type, userIdxByJwt, startPage, pageNum);
            return new BaseResponse<>(getContentsTvWatchingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 요즘뜨는 콘텐츠 조회 API (TV or 영화)
     * [GET] /contents/trending?type=1
     * @return BaseResponse<GetContentsTvTrendingRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/trending") // (GET) 15.165.16.88:9000/contents/trending?type=1
    public BaseResponse<List<GetContentsTvTrendingRes>> getContentsTvTrending(@RequestParam int type) {
        try{
            // Get TV main contents
            List<GetContentsTvTrendingRes> getContentsTvTrendingRes = contentsProvider.getContentsTvTrending(type);
            return new BaseResponse<>(getContentsTvTrendingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내가 찜한 콘텐츠 조회 API (TV or 영화)
     * [GET] /contents/keep?type=1
     * @return BaseResponse<GetContentsTvKeepRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/keep") // (GET) 15.165.16.88:9000/contents/keep?type=1
    public BaseResponse<List<GetContentsTvKeepRes>> getContentsTvKeep(@RequestParam int type) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // Get TV main contents
            List<GetContentsTvKeepRes> getContentsTvKeepRes = contentsProvider.getContentsTvKeep(type, userIdxByJwt);
            return new BaseResponse<>(getContentsTvKeepRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * TV 상세정보 조회 API
     * [GET] /contents/:contentsIdx/tvDetails?type=1
     * @return BaseResponse<GetContentsTvDetailsRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{contentsIdx}/tvDetails") // (GET) 15.165.16.88:9000/contents/:contentsIdx/tvDetails?type=1
    public BaseResponse<List<GetContentsTvDetailsRes>> getContentsTvDetails(@PathVariable("contentsIdx") int contentsIdx, @RequestParam int type) {
        try{
            // Get TV main contents
            List<GetContentsTvDetailsRes> getContentsTvDetailsRes = contentsProvider.getContentsTvDetails(contentsIdx, type);
            return new BaseResponse<>(getContentsTvDetailsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * TV 회차 조회 API
     * [GET] /contents/:contentsIdx/episodes?type=1
     * @return BaseResponse<GetContentsTvEpisodesRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{contentsIdx}/episodes") // (GET) 15.165.16.88:9000/contents/:contentsIdx/episodes?type=1
    public BaseResponse<List<GetContentsTvEpisodesRes>> getContentsTvEpisodes(@PathVariable("contentsIdx") int contentsIdx, @RequestParam int type) {
        try{
            // Get TV main contents
            List<GetContentsTvEpisodesRes> getContentsTvEpisodesRes = contentsProvider.getContentsTvEpisodes(contentsIdx, type);
            return new BaseResponse<>(getContentsTvEpisodesRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 영화 시청중인 콘텐츠 조회 API
     * [GET] /contents/watching?type=2
     * @return BaseResponse<GetContentsMovieWatchingRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/movieWatching") // (GET) 15.165.16.88:9000/contents/movieWatching?type=2
    public BaseResponse<List<GetContentsMovieWatchingRes>> getContentsMovieWatching(@RequestParam int type) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // Get TV main contents
            List<GetContentsMovieWatchingRes> getContentsMovieWatchingRes = contentsProvider.getContentsMovieWatching(type, userIdxByJwt);
            return new BaseResponse<>(getContentsMovieWatchingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 영화 상세정보 조회 API
     * [GET] /contents/:contentsIdx/movieDetails?type=2
     * @return BaseResponse<GetContentsMovieDetailsRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{contentsIdx}/movieDetails") // (GET) 15.165.16.88:9000/contents/:contentsIdx/movieDetails?type=2
    public BaseResponse<List<GetContentsMovieDetailsRes>> getContentsMovieDetails(@PathVariable("contentsIdx") int contentsIdx, @RequestParam int type) {
        try{
            // Get TV main contents
            List<GetContentsMovieDetailsRes> getContentsMovieDetailsRes = contentsProvider.getContentsMovieDetails(contentsIdx, type);
            return new BaseResponse<>(getContentsMovieDetailsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 메인 추천 콘텐츠 ('한국' 카테고리) 조회 API
     * [GET] /contents/mainCate?type=2&categoryIdx=2
     * @return BaseResponse<GetContentsMovieMainCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/mainCate") // (GET) 15.165.16.88:9000/contents/mainCate?type=2&categoryIdx=2
    public BaseResponse<List<GetContentsMovieMainCateRes>> getContentsMovieMainCate(@RequestParam int type, @RequestParam int categoryIdx) {
        try{
            // Get movie main contents
            List<GetContentsMovieMainCateRes> getContentsMovieMainCateRes = contentsProvider.getContentsMovieMainCate(type, categoryIdx);
            return new BaseResponse<>(getContentsMovieMainCateRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 한국 Top 10 ('한국' 카테고리) 조회 API
     * [GET] /contents/rankingCate?type=2&categoryIdx=2
     * @return BaseResponse<GetContentsMovieRankingCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/rankingCate") // (GET) 15.165.16.88:9000/contents/rankingCate?type=2&categoryIdx=2
    public BaseResponse<List<GetContentsMovieRankingCateRes>> getContentsMovieRankingCate(@RequestParam int type, @RequestParam int categoryIdx) {
        try{
            // Get movie ranking contents
            List<GetContentsMovieRankingCateRes> getContentsMovieRankingCateRes = contentsProvider.getContentsMovieRankingCate(type, categoryIdx);
            return new BaseResponse<>(getContentsMovieRankingCateRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 시청중인 콘텐츠 ('한국' 카테고리) 조회 API
     * [GET] /contents/watchingCate?type=2&categoryIdx=2
     * @return BaseResponse<GetContentsMovieWatchingCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/watchingCate") // (GET) 15.165.16.88:9000/contents/watchingCate?type=2&categoryIdx=2
    public BaseResponse<List<GetContentsMovieWatchingCateRes>> getContentsMovieWatchingCate(@RequestParam int type, @RequestParam int categoryIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // Get movie watching contents
            List<GetContentsMovieWatchingCateRes> getContentsMovieWatchingCateRes = contentsProvider.getContentsMovieWatchingCate(type, categoryIdx, userIdxByJwt);
            return new BaseResponse<>(getContentsMovieWatchingCateRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 요즘뜨는 콘텐츠 ('한국' 카테고리) 조회 API
     * [GET] /contents/trendingCate?type=2&categoryIdx=2
     * @return BaseResponse<GetContentsMovieTrendingCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/trendingCate") // (GET) 15.165.16.88:9000/contents/trendingCate?type=2&categoryIdx=2
    public BaseResponse<List<GetContentsMovieTrendingCateRes>> getContentsMovieTrendingCate(@RequestParam int type, @RequestParam int categoryIdx) {
        try{
            // Get movie hot contents
            List<GetContentsMovieTrendingCateRes> getContentsMovieTrendingCateRes = contentsProvider.getContentsMovieTrendingCate(type, categoryIdx);
            return new BaseResponse<>(getContentsMovieTrendingCateRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 내가 찜한 콘텐츠 ('한국' 카테고리) 조회 API
     * [GET] /contents/keepCate?type=2&categoryIdx=2
     * @return BaseResponse<GetContentsMovieTrendingCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/keepCate") // (GET) 15.165.16.88:9000/contents/keepCate?type=2&categoryIdx=2
    public BaseResponse<List<GetContentsMovieKeepCateRes>> getContentsMovieKeepCate(@RequestParam int type, @RequestParam int categoryIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // Get movie hot contents
            List<GetContentsMovieKeepCateRes> getContentsMovieKeepCateRes = contentsProvider.getContentsMovieKeepCate(type, categoryIdx, userIdxByJwt);
            return new BaseResponse<>(getContentsMovieKeepCateRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 공개 예정 조회 API
     * [GET] /contents/to-be-released
     * @return BaseResponse<GetContentsMovieTrendingCateRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/to-be-released") // (GET) 15.165.16.88:9000/contents/to-be-released
    public BaseResponse<List<GetContentsToBeReleasedRes>> getContentsToBeReleased() {
        try{
            // Get movie to-be-released contents
            List<GetContentsToBeReleasedRes> getContentsToBeReleasedRes = contentsProvider.getContentsToBeReleased();
            return new BaseResponse<>(getContentsToBeReleasedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
