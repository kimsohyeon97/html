package kr.co.pamStory.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.pamStory.dto.ArticleDTO;
import kr.co.pamStory.util.DBHelper;
import kr.co.pamStory.util.SQL;

public class ArticleDAO extends DBHelper {
	private static final ArticleDAO INSTANCE = new ArticleDAO();

	public static ArticleDAO getInstance() {
		return INSTANCE;
	}

	private ArticleDAO() {
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public int insertArticle(ArticleDTO dto) {

		int no = 0;

		try {
			conn = getConnection();
			psmt = conn.prepareStatement(SQL.INSERT_ARTICLE);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setInt(3, dto.getFile());
			psmt.setString(4, dto.getWriter());
			psmt.setString(5, dto.getRegip());
			psmt.setString(6, dto.getCate());
			
			psmt.executeUpdate();

			// 글 번호 조회 쿼리 실행
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL.SELECT_MAX_NO);
			if (rs.next()) {
				no = rs.getInt(1);
			}

			closeAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return no;
	}

	public ArticleDTO selectArticle(int no) {
		ArticleDTO dto = null;
		try {
			conn = getConnection();
			psmt = conn.prepareStatement(SQL.SELECT_ARTICLE_BY_NO);
			psmt.setInt(1, no);
			rs = psmt.executeQuery();
			if (rs.next()) {
				dto = new ArticleDTO();
				dto.setNo(rs.getInt(1));
				dto.setCate(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setComment(rs.getInt(5));
				dto.setFile(rs.getInt(6));
				dto.setHit(rs.getInt(7));
				dto.setWriter(rs.getString(8));
				dto.setRegip(rs.getString(9));
				dto.setWdate(rs.getString(10));
				dto.setNick(rs.getString(11));
			}
			closeAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return dto;
	}

	public int selectCountArticle() {
		int total = 0;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL.SELECT_COUNT_ARTICLE);
			if (rs.next()) {
				total = rs.getInt(1);
			}
			closeAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return total;
	}

	public List<ArticleDTO> selectAllArticle(int start) {

		List<ArticleDTO> articles = new ArrayList<ArticleDTO>();

		try {

			conn = getConnection();
			psmt = conn.prepareStatement(SQL.SELECT_ALL_ARTICLE);
			psmt.setInt(1, start);
			rs = psmt.executeQuery();

			while (rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				dto.setNo(rs.getInt(1));
				dto.setCate(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setComment(rs.getInt(5));
				dto.setFile(rs.getInt(6));
				dto.setHit(rs.getInt(7));
				dto.setWriter(rs.getString(8));
				dto.setRegip(rs.getString(9));
				dto.setWdate(rs.getString(10));
				dto.setNick(rs.getString(11));
				articles.add(dto);
			}
			closeAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return articles;

	}
	
	public int selectCountArticleBySearch(ArticleDTO articleDTO) {
		int count=0;
		StringBuilder sql= new StringBuilder(SQL.SELECT_COUNT_ARTICLE_FOR_SEARCH);
	
		if(articleDTO.getSearchType().equals("title")) {
			sql.append(SQL.WHERE_FOR_SEARCH_TITLE);
		}else if(articleDTO.getSearchType().equals("content")) {
			sql.append(SQL.WHERE_FOR_SEARCH_CONTENT);
		}else if(articleDTO.getSearchType().equals("writer")) {
			sql.append(SQL.JOIN_FOR_SEARCH_NICK);
			sql.append(SQL.WHERE_FOR_SEARCH_WRITER);
		}
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql.toString());
			psmt.setString(1, "%"+articleDTO.getKeyword()+"%");
			logger.debug(psmt.toString());
			
			rs=psmt.executeQuery();
			
			if(rs.next()) {
				count=rs.getInt(1);
			}
			closeAll();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
public List<ArticleDTO> selectAllArticleBySearch(ArticleDTO articleDTO, int start) {
		
		List<ArticleDTO> articles = new ArrayList<ArticleDTO>();
		
		StringBuilder sql = new StringBuilder(SQL.SELECT_ALL_ARTICLE_BY_SEARCH);
		
		if(articleDTO.getSearchType().equals("title")) {
			sql.append(SQL.WHERE_FOR_SEARCH_TITLE);
			sql.append(SQL.ORDER_FOR_SEARCH);
			sql.append(SQL.LIMIT_FOR_SEARCH);			
		}else if(articleDTO.getSearchType().equals("content")) {
			sql.append(SQL.WHERE_FOR_SEARCH_CONTENT);
			sql.append(SQL.ORDER_FOR_SEARCH);
			sql.append(SQL.LIMIT_FOR_SEARCH);			
		}else if(articleDTO.getSearchType().equals("writer")) {
			sql.append(SQL.WHERE_FOR_SEARCH_WRITER);
			sql.append(SQL.ORDER_FOR_SEARCH);
			sql.append(SQL.LIMIT_FOR_SEARCH);			
		}
		
		try {
			conn = getConnection();
			psmt = conn.prepareStatement(sql.toString());
			psmt.setString(1, "%"+articleDTO.getKeyword()+"%");
			psmt.setString(2, articleDTO.getCate());
			psmt.setInt(3, start);
			logger.debug(psmt.toString());
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				dto.setNo(rs.getInt(1));
				dto.setCate(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setComment(rs.getInt(5));
				dto.setFile(rs.getInt(6));
				dto.setHit(rs.getInt(7));
				dto.setWriter(rs.getString(8));
				dto.setRegip(rs.getString(9));
				dto.setWdate(rs.getString(10));
				dto.setNick(rs.getString(11));
				articles.add(dto);
			}
			closeAll();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}			
		return articles;		
	}

	public void updateArticle(ArticleDTO dto) {
		
	    try {
	        conn = getConnection();
	        psmt = conn.prepareStatement(SQL.UPDATE_BY_NO);
	        psmt.setString(1, dto.getTitle());
	        psmt.setString(2, dto.getContent());
	        psmt.setString(3, dto.getWriter());
	        psmt.setString(4, dto.getRegip());
	        psmt.setInt(5, dto.getNo());

	        int result = psmt.executeUpdate(); 

	        if (result > 0) {
	            logger.info("게시글 수정 완료, no=" + dto.getNo());
	        } else {
	            logger.warn("게시글 수정 실패, no=" + dto.getNo());
	        }

	        closeAll(); 

	    } catch (Exception e) {
	        logger.error(e.getMessage());
	    }
	}


	public void deleteArticle(String no) {

		try {

			conn = getConnection();
			psmt = conn.prepareStatement(SQL.DELETE_ARTICLE);
			psmt.setString(1, no);
			psmt.executeUpdate();
			closeAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public int selectCountArticle(String uid) {
		int postCount = 0;
	    
	    try {
			conn = getConnection();
	        psmt = conn.prepareStatement(SQL.SELECT_COUNT_USER_BY_UID);
	        psmt.setString(1, uid);
	        rs = psmt.executeQuery();

	        if (rs.next()) {
	            postCount = rs.getInt(1);  // 게시글 수를 가져옴
	        }
	        closeAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	    return postCount;
	}
	
	public int selectCountArticleByCate(String cate) {
		int total = 0;

		try {
			conn = getConnection();
			psmt = conn.prepareStatement(SQL.SELECT_COUNT_ARTICLE_BY_CATE);
			psmt.setString(1, cate);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				total = rs.getInt(1);
			}
			closeAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return total;
	}

	public List<ArticleDTO> selectAllArticleByCate(int start, String cate) {
		List<ArticleDTO> articles = new ArrayList<ArticleDTO>();

		try {

			conn = getConnection();
			psmt = conn.prepareStatement(SQL.SELECT_ALL_ARTICLE_BY_CATE);
			psmt.setString(1, cate);
			psmt.setInt(2, start);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				dto.setNo(rs.getInt(1));
				dto.setCate(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setComment(rs.getInt(5));
				dto.setFile(rs.getInt(6));
				dto.setHit(rs.getInt(7));
				dto.setWriter(rs.getString(8));
				dto.setRegip(rs.getString(9));
				dto.setWdate(rs.getString(10));
				dto.setNick(rs.getString(11));
				articles.add(dto);
			}
			closeAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return articles;

	}

	public List<ArticleDTO> selectArticleByCateLimit5(String cate) {
		List<ArticleDTO> articles = new ArrayList<ArticleDTO>();

		try {

			conn = getConnection();
			psmt = conn.prepareStatement(SQL.SELECT_ALL_ARTICLE_BY_CATE_LIMIT_5);
			psmt.setString(1, cate);
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				ArticleDTO dto = new ArticleDTO();
				dto.setNo(rs.getInt(1));
				dto.setCate(rs.getString(2));
				if(rs.getString(3).length() > 8) {
					dto.setTitle(rs.getString(3).substring(0,8) + "...");					
				}else {
					dto.setTitle(rs.getString(3));
				}
			
				dto.setContent(rs.getString(4));
				dto.setComment(rs.getInt(5));
				dto.setFile(rs.getInt(6));
				dto.setHit(rs.getInt(7));
				dto.setWriter(rs.getString(8));
				dto.setRegip(rs.getString(9));
				dto.setWdate(rs.getString(10).substring(0,11));
				dto.setNick(rs.getString(11));
				articles.add(dto);
			}
			closeAll();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return articles;

	}
}