package com.example.demo.src.netflixContents;

import com.example.demo.src.netflixContents.model.*;
import com.example.demo.src.netflixUser.model.GetProfilesRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ContentsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // [GET] 메인 추천 프로그램 조회 API (TV or 영화)
    public List<GetContentsTvMainRes> getContentsTvMain(int type, int startPage, int pageNum) {
        String getContentsQuery = "select distinct typeTxt, categoryList, HCL.contentsIdx, posterImgUrl, ifnull(isKeep, 0) isKeep\n" +
                "from Type T,\n" +
                "     Contents C\n" +
                "         join (select min(contentsIdx) MC\n" +
                "               from Contents C\n" +
                "                        join ContentsType CT on C.idx = CT.contentsIdx\n" +
                "               where CT.typeIdx = 1) as TypeContents on C.idx = TypeContents.MC\n" +
                "         join HotContentsList HCL on C.idx = HCL.contentsIdx\n" +
                "         join (select Group_concat(categoryTxt SEPARATOR '·') categoryList, CC.contentsIdx\n" +
                "               from Category Ca\n" +
                "                        join ContentsCategory CC on CC.categoryIdx = Ca.idx\n" +
                "               group by CC.contentsIdx) Tag on C.idx = Tag.contentsIdx\n" +
                "         left join (select case when status = 0 then 1 else 0 end as isKeep, contentsIdx\n" +
                "                    from Keep\n" +
                "                    where userIdx = 1) K on K.contentsIdx = C.idx\n" +
                "where T.idx = ?\n" +
                "limit ?, ?";
        int getContentsParams1 = type;
        int getContentsParams2 = startPage;
        int getContentsParams3 = pageNum;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvMainRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryList"),
                        rs.getInt("contentsIdx"),
                        rs.getString("posterImgUrl"),
                        rs.getInt("isKeep")),
                getContentsParams1, getContentsParams2, getContentsParams3
        );
    }


    // [GET] 한국 Top 10 조회 API (TV or 영화)
    public List<GetContentsTvRankingRes> getContentsTvRanking(int type, int startPage, int pageNum) {
        String getContentsQuery = "select distinct typeTxt, rankingNum, R.contentsIdx contentsIdx, posterImgUrl\n" +
                "from Type T,\n" +
                "     Contents C\n" +
                "         join (select contentsIdx\n" +
                "               from Contents C\n" +
                "                        join ContentsType CT on C.idx = CT.contentsIdx\n" +
                "               where CT.typeIdx = 1) as TypeContents on C.idx = TypeContents.contentsIdx\n" +
                "         join (select C.idx as CountryIdx, contentsIdx\n" +
                "               from Country C\n" +
                "                        inner join ContentsCountry CC\n" +
                "                                   on C.idx = CC.countryIdx\n" +
                "               where C.countryName = '한국') as country on country.contentsIdx = C.idx\n" +
                "         join RankTable R on country.contentsIdx = R.contentsIdx\n" +
                "where T.idx = ?\n" +
                "limit ?, ?";

        int getContentsParams1 = type;
        int getContentsParams2 = startPage;
        int getContentsParams3 = pageNum;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvRankingRes(
                        rs.getString("typeTxt"),
                        rs.getInt("rankingNum"),
                        rs.getInt("contentsIdx"),
                        rs.getString("posterImgUrl")),
                getContentsParams1, getContentsParams2, getContentsParams3
        );
    }

    // [GET] TV 시청중인 콘텐츠 조회 API
    public List<GetContentsTvWatchingRes> getContentsTvWatching(int type, int userIdxByJwt, int startPage, int pageNum) {
        String getContentsQuery = "select distinct typeTxt,\n" +
                "                W.contentsIdx watchingIdx,\n" +
                "                posterImgUrl  watchingPosterUrl,\n" +
                "                case\n" +
                "                    when runTime = 0\n" +
                "                        then E.seasonNum\n" +
                "                    end as    watchingSeasonNum,\n" +
                "                case\n" +
                "                    when runTime = 0\n" +
                "                        then E.episodeNum\n" +
                "                    end as    watchingEpisodNum\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join WatchList W on CT.contentsIdx = W.contentsIdx\n" +
                "         join Contents C on W.contentsIdx = C.idx\n" +
                "         left join EpisodeInfo E on E.contentsIdx = C.idx\n" +
                "    and E.seasonNum = W.seasonNum and E.episodeNum = W.episodeNum\n" +
                "where T.idx = ? and W.userIdx = ?\n" +
                "limit ?, ?";

        int getContentsParams1 = type;
        int getContentsParams2 = userIdxByJwt;
        int getContentsParams3 = startPage;
        int getContentsParams4 = pageNum;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvWatchingRes(
                        rs.getString("typeTxt"),
                        rs.getInt("watchingIdx"),
                        rs.getString("watchingPosterUrl"),
                        rs.getInt("watchingSeasonNum"),
                        rs.getInt("watchingEpisodNum")),
                getContentsParams1, getContentsParams2, getContentsParams3, getContentsParams4
        );
    }

    // [GET] 요즘뜨는 콘텐츠 조회 API (TV or 영화)
    public List<GetContentsTvTrendingRes> getContentsTvTrending(int type) {
        String getContentsQuery = "select typeTxt, C.idx hotIdx, posterImgUrl hotPosterUrl\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join HotContentsList HCL on CT.contentsIdx = HCL.contentsIdx\n" +
                "         join Contents C on HCL.contentsIdx = C.idx\n" +
                "where T.idx = ?";

        int getContentsParams = type;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvTrendingRes(
                        rs.getString("typeTxt"),
                        rs.getInt("hotIdx"),
                        rs.getString("hotPosterUrl")),
                getContentsParams
        );
    }

    // [GET] 내가 찜한 콘텐츠 조회 API (TV or 영화)
    public List<GetContentsTvKeepRes> getContentsTvKeep(int type, int userIdxByJwt) {
        String getContentsQuery = "select typeTxt, C.idx keepIdx, posterImgUrl keepPosterUrl\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join Keep K on CT.contentsIdx = K.contentsIdx\n" +
                "         join Contents C on K.contentsIdx = C.idx\n" +
                "where T.idx = ? and K.userIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = userIdxByJwt;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvKeepRes(
                        rs.getString("typeTxt"),
                        rs.getInt("keepIdx"),
                        rs.getString("keepPosterUrl")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] TV 상세정보 조회 API
    public List<GetContentsTvDetailsRes> getContentsTvDetails(int contentsIdx, int type) {
        String getContentsQuery = "select distinct C.idx contentsIdx,\n" +
                "                previewVideoUrl,\n" +
                "                title,\n" +
                "                releasedYear,\n" +
                "                (select max(E.seasonNum)\n" +
                "                 from EpisodeInfo E\n" +
                "                          join Contents C on E.contentsIdx = C.idx\n" +
                "                 where E.contentsIdx = 1) seasonTotalNum,\n" +
                "                rankingNum,\n" +
                "                E.seasonNum,\n" +
                "                E.episodeNum,\n" +
                "                episodeTitle,\n" +
                "                storyline,\n" +
                "                FP.typeTxt,\n" +
                "                FP.personName,\n" +
                "                case\n" +
                "                    when C.idx = W.contentsIdx and runTime = 0\n" +
                "                        then concat(E.runningTime - W.watchedTime, '분')\n" +
                "                    end as              remainTime,\n" +
                "                if(K.status = 0, 0, 1)  isKeep,\n" +
                "                if(R.status = 0, 0, 1)  isRated\n" +
                "from Type T,\n" +
                "     Contents C\n" +
                "         join EpisodeInfo E on C.idx = E.contentsIdx\n" +
                "         join WatchList W on E.contentsIdx = W.contentsIdx\n" +
                "         join RankTable RT on W.contentsIdx = RT.contentsIdx\n" +
                "         join ContentsPeople CP on C.idx = CP.contentsIdx\n" +
                "         join FilmPeople FP on CP.peopleIdx = FP.idx\n" +
                "         left join Keep K on C.idx = K.contentsIdx\n" +
                "         left join Rating R on C.idx = R.idx\n" +
                "where W.contentsIdx = ?\n" +
                "  and T.idx = ?\n" +
                "limit 1";

        int getContentsParams1 = contentsIdx;
        int getContentsParams2 = type;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvDetailsRes(
                        rs.getInt("contentsIdx"),
                        rs.getString("previewVideoUrl"),
                        rs.getString("title"),
                        rs.getInt("releasedYear"),
                        rs.getInt("seasonTotalNum"),
                        rs.getInt("rankingNum"),
                        rs.getInt("seasonNum"),
                        rs.getInt("episodeNum"),
                        rs.getString("episodeTitle"),
                        rs.getString("storyLine"),
                        rs.getString("typeTxt"),
                        rs.getString("personName"),
                        rs.getString("remainTime"),
                        rs.getInt("isKeep"),
                        rs.getInt("isRated")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] TV 회차 조회 API
    public List<GetContentsTvEpisodesRes> getContentsTvEpisodes(int contentsIdx, int type) {
        String getContentsQuery = "select distinct E.seasonNum, E.episodeNum, episodeTitle, runningTime, storyline\n" +
                "from Type T, WatchList W,\n" +
                "     EpisodeInfo E\n" +
                "         join Contents C on E.contentsIdx = C.idx\n" +
                "where E.contentsIdx = ? and T.idx = ?";

        int getContentsParams1 = contentsIdx;
        int getContentsParams2 = type;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsTvEpisodesRes(
                        rs.getInt("seasonNum"),
                        rs.getInt("episodeNum"),
                        rs.getString("episodeTitle"),
                        rs.getInt("runningTime"),
                        rs.getString("storyLine")),
                getContentsParams1, getContentsParams2
        );
    }


    // [GET] 영화 시청중인 콘텐츠 조회 API
    public List<GetContentsMovieWatchingRes> getContentsMovieWatching(int type, int userIdxByJwt) {
        String getContentsQuery = "select distinct typeTxt,\n" +
                "                W.contentsIdx watchingIdx,\n" +
                "                posterImgUrl  watchingPosterUrl,\n" +
                "                case\n" +
                "                    when runTime = 0\n" +
                "                        then null\n" +
                "                    else time_format(sec_to_time((C.runTime - W.watchedTime) * 60), '%l시간 %i분')\n" +
                "                    end as    watchingRemainTime\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join WatchList W on CT.contentsIdx = W.contentsIdx\n" +
                "         join Contents C on W.contentsIdx = C.idx\n" +
                "where T.idx = ? and W.userIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = userIdxByJwt;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieWatchingRes(
                        rs.getString("typeTxt"),
                        rs.getInt("watchingIdx"),
                        rs.getString("watchingPosterUrl"),
                        rs.getString("watchingRemainTime")),
                getContentsParams1, getContentsParams2
        );
    }


    // [GET] 영화 상세정보 조회 API
    public List<GetContentsMovieDetailsRes> getContentsMovieDetails(int contentsIdx, int type) {
        String getContentsQuery = "select C.idx                  contentsIdx,\n" +
                "       previewVideoUrl,\n" +
                "       title,\n" +
                "       releasedYear,\n" +
                "       runTime,\n" +
                "       rankingNum,\n" +
                "       case\n" +
                "           when runTime = 0\n" +
                "               then null\n" +
                "           else time_format(sec_to_time((C.runTime - W.watchedTime) * 60), '%l시간 %i분')\n" +
                "           end as             RemainTime,\n" +
                "       plotSummary,\n" +
                "       FP.typeTxt,\n" +
                "       FP.personName,\n" +
                "       if(K.status = 0, 0, 1) isKeep,\n" +
                "       if(R.status = 0, 0, 1) isRated\n" +
                "from Type T,\n" +
                "     Contents C\n" +
                "         join WatchList W on C.idx = W.contentsIdx\n" +
                "         join RankTable RT on W.contentsIdx = RT.contentsIdx\n" +
                "         join ContentsPeople CP on C.idx = CP.contentsIdx\n" +
                "         join FilmPeople FP on CP.peopleIdx = FP.idx\n" +
                "         left join Keep K on C.idx = K.contentsIdx\n" +
                "         left join Rating R on C.idx = R.idx\n" +
                "where W.contentsIdx = ?\n" +
                "  and T.idx = ?";

        int getContentsParams1 = contentsIdx;
        int getContentsParams2 = type;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieDetailsRes(
                        rs.getInt("contentsIdx"),
                        rs.getString("previewVideoUrl"),
                        rs.getString("title"),
                        rs.getInt("releasedYear"),
                        rs.getInt("runtime"),
                        rs.getInt("rankingNum"),
                        rs.getString("RemainTime"),
                        rs.getString("plotSummary"),
                        rs.getString("typeTxt"),
                        rs.getString("personName"),
                        rs.getInt("isKeep"),
                        rs.getInt("isRated")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] 영화 메인 추천 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieMainCateRes> getContentsMovieMainCate(int type, int categoryIdx) {
        String getContentsQuery = "select distinct typeTxt, categoryList, HCL.contentsIdx, posterImgUrl, ifnull(isKeep, 0) isKeep\n" +
                "from Type T, Category Ca,\n" +
                "     Contents C\n" +
                "         join (select min(contentsIdx) MC\n" +
                "               from Contents C\n" +
                "                        join ContentsType CT on C.idx = CT.contentsIdx\n" +
                "               where CT.typeIdx = 2) as TypeContents on C.idx = TypeContents.MC\n" +
                "         join HotContentsList HCL on C.idx = HCL.contentsIdx\n" +
                "         join (select Group_concat(categoryTxt SEPARATOR '·') categoryList, CC.contentsIdx\n" +
                "               from Category Ca\n" +
                "                        join ContentsCategory CC on CC.categoryIdx = Ca.idx\n" +
                "               group by CC.contentsIdx) Tag on C.idx = Tag.contentsIdx\n" +
                "         join (select C.idx\n" +
                "                    from Contents C\n" +
                "                             join ContentsCategory CC on C.idx = CC.contentsIdx\n" +
                "                    where categoryIdx = 2) CategoryInfo on C.idx = CategoryInfo.idx\n" +
                "         left join (select case when status = 0 then 1 else 0 end as isKeep, contentsIdx\n" +
                "                    from Keep\n" +
                "                    where userIdx = 2) K on K.contentsIdx = C.idx\n" +
                "where T.idx = ? and Ca.categoryIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = categoryIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieMainCateRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryList"),
                        rs.getInt("contentsIdx"),
                        rs.getString("posterImgUrl"),
                        rs.getInt("isKeep")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] 영화 한국 Top 10 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieRankingCateRes> getContentsMovieRankingCate(int type, int categoryIdx) {
        String getContentsQuery = "select distinct typeTxt, categoryTxt, rankingNum, R.contentsIdx contentsIdx, posterImgUrl\n" +
                "from Type T,\n" +
                "     Contents C\n" +
                "         join (select contentsIdx\n" +
                "               from Contents C\n" +
                "                        join ContentsType CT on C.idx = CT.contentsIdx\n" +
                "               where CT.typeIdx = 2) as TypeContents on C.idx = TypeContents.contentsIdx\n" +
                "         join ContentsCategory CC on C.idx = CC.contentsIdx\n" +
                "         join Category Ca on CC.categoryIdx = Ca.idx\n" +
                "         join (select C.idx as CountryIdx, contentsIdx\n" +
                "               from Country C\n" +
                "                        inner join ContentsCountry CC\n" +
                "                                   on C.idx = CC.countryIdx\n" +
                "               where C.countryName = '한국') as country on country.contentsIdx = C.idx\n" +
                "         join RankTable R on country.contentsIdx = R.contentsIdx\n" +
                "where T.idx = ? and CC.categoryIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = categoryIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieRankingCateRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryTxt"),
                        rs.getInt("rankingNum"),
                        rs.getInt("contentsIdx"),
                        rs.getString("posterImgUrl")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] 영화 시청중인 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieWatchingCateRes> getContentsMovieWatchingCate(int type, int categoryIdx, int userIdxByJwt) {
        String getContentsQuery = "select distinct typeTxt,\n" +
                "                Ca.categoryTxt,\n" +
                "                W.contentsIdx watchingIdx,\n" +
                "                posterImgUrl  watchingPosterUrl,\n" +
                "                case\n" +
                "                    when runTime = 0\n" +
                "                        then null\n" +
                "                    else time_format(sec_to_time((C.runTime - W.watchedTime) * 60), '%l시간 %i분')\n" +
                "                    end as    watchingRemainTime\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join WatchList W on CT.contentsIdx = W.contentsIdx\n" +
                "         join Contents C on W.contentsIdx = C.idx\n" +
                "         join ContentsCategory CC on C.idx = CC.contentsIdx\n" +
                "         join Category Ca on CC.categoryIdx = Ca.idx\n" +
                "where T.idx = ?\n" +
                "  and CC.categoryIdx = ? and W.userIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = categoryIdx;
        int getContentsParams3 = userIdxByJwt;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieWatchingCateRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryTxt"),
                        rs.getInt("watchingIdx"),
                        rs.getString("watchingPosterUrl"),
                        rs.getString("watchingRemainTime")),
                getContentsParams1, getContentsParams2, getContentsParams3
        );
    }

    // [GET] 영화 요즘뜨는 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieTrendingCateRes> getContentsMovieTrendingCate(int type, int categoryIdx) {
        String getContentsQuery = "select typeTxt, Ca.categoryTxt, C.idx hotIdx, posterImgUrl hotPosterUrl\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join HotContentsList HCL on CT.contentsIdx = HCL.contentsIdx\n" +
                "         join Contents C on HCL.contentsIdx = C.idx\n" +
                "         join ContentsCategory CC on C.idx = CC.contentsIdx\n" +
                "         join Category Ca on CC.categoryIdx = Ca.idx\n" +
                "where T.idx = ?\n" +
                "  and CC.categoryIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = categoryIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieTrendingCateRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryTxt"),
                        rs.getInt("hotIdx"),
                        rs.getString("hotPosterUrl")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] 영화 내가 찜한 콘텐츠 ('한국' 카테고리) 조회 API
    public List<GetContentsMovieKeepCateRes> getContentsMovieKeepCate(int type, int categoryIdx, int userIdxByJwt) {
        String getContentsQuery = "select typeTxt, Ca.categoryTxt, C.idx keepIdx, posterImgUrl keepPosterUrl\n" +
                "from Type T\n" +
                "         join ContentsType CT on T.idx = CT.typeIdx\n" +
                "         join Keep K on CT.contentsIdx = K.contentsIdx\n" +
                "         join Contents C on K.contentsIdx = C.idx\n" +
                "         join ContentsCategory CC on C.idx = CC.contentsIdx\n" +
                "         join Category Ca on CC.categoryIdx = Ca.idx\n" +
                "where T.idx = ?\n" +
                "  and CC.categoryIdx = ? and K.userIdx = ?";

        int getContentsParams1 = type;
        int getContentsParams2 = categoryIdx;
        int getContentsParams3 = userIdxByJwt;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsMovieKeepCateRes(
                        rs.getString("typeTxt"),
                        rs.getString("categoryTxt"),
                        rs.getInt("keepIdx"),
                        rs.getString("keepPosterUrl")),
                getContentsParams1, getContentsParams2, getContentsParams3
        );
    }

    // [GET] 공개 예정 조회 API
    public List<GetContentsToBeReleasedRes> getContentsToBeReleased() {
        String getContentsQuery = "select distinct TBR.contentsIdx,TBR.releaseDate,previewVideoUrl,title,if(A.status = 0, 0, 1) isAlarm,\n" +
                "                typeTxt,\n" +
                "                case\n" +
                "                    when typeTxt = 'TV 프로그램' then storyLine\n" +
                "                    else plotSummary end as storyLine,\n" +
                "                categoryList\n" +
                "from EpisodeInfo E, Contents C\n" +
                "         join ToBeReleased TBR on C.idx = TBR.contentsIdx\n" +
                "         join ContentsType CT on TBR.contentsIdx = CT.contentsIdx and C.idx=CT.contentsIdx\n" +
                "         join Type T on CT.typeIdx = T.idx\n" +
                "         join (select Group_concat(categoryTxt SEPARATOR '·') categoryList, CC.contentsIdx\n" +
                "               from Category Ca\n" +
                "                        join ContentsCategory CC on CC.categoryIdx = Ca.idx\n" +
                "               group by CC.contentsIdx) Tag on C.idx = Tag.contentsIdx\n" +
                "         left join Alarm A on C.idx = A.contentsIdx\n" +
                "where T.idx = 1\n" +
                "order by contentsIdx asc";


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetContentsToBeReleasedRes(
                        rs.getInt("contentsIdx"),
                        rs.getString("releaseDate"),
                        rs.getString("previewVideoUrl"),
                        rs.getString("title"),
                        rs.getInt("isAlarm"),
                        rs.getString("typeTxt"),
                        rs.getString("storyLine"),
                        rs.getString("categoryList"))
        );
    }





}
