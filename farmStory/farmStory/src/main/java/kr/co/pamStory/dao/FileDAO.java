package kr.co.pamStory.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.pamStory.dto.FileDTO;
import kr.co.pamStory.util.DBHelper;
import kr.co.pamStory.util.SQL;

public class FileDAO extends DBHelper {
	private static final FileDAO INSTANCE = new FileDAO();
	public static FileDAO getInstance() {
		return INSTANCE;
	}
	private FileDAO() {}
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void insertFile(FileDTO dto) {
		try {
			conn = getConnection();
			psmt = conn.prepareStatement(SQL.INSERT_FILE);
			psmt.setInt(1, dto.getAno());
			psmt.setString(2, dto.getoName());
			psmt.setString(3, dto.getsName());
			psmt.executeUpdate();
			closeAll();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public FileDTO selectFile(int fno) {
		return null;
	}
	
	public List<FileDTO> selectFile(String no) {
		List<FileDTO> dtos = new ArrayList<>();
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(SQL.SELECT_FILE_BY_ANO);
			psmt.setString(1, no);
			rs=psmt.executeQuery();
			
			while(rs.next()) {
				FileDTO dto = new FileDTO();
				dto.setFno(rs.getInt(1));
				dto.setAno(rs.getInt(2));
				dto.setoName(rs.getString(3));
				dto.setsName(rs.getString(4));
				dto.setDownload(rs.getInt(5));
				dto.setRdate(rs.getString(6));
				dtos.add(dto);
				
			}
			closeAll();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return dtos;
	}
	
	public List<FileDTO> selectAllFile() {
		return null;
	}
	
	public void updateFile(FileDTO dto) {
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(SQL.UPDATE_FILE);
			psmt.setString(1, dto.getoName());
			psmt.setString(2, dto.getsName());
			psmt.setInt(3, dto.getFno());
			
			psmt.executeUpdate();
			
			closeAll();
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	

	public void deleteFile(String ano) {
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(SQL.DELETE_FILE_BY_ANO);
			psmt.setString(1, ano);
			psmt.executeUpdate();
			closeAll();
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
public List<String> selectFileByAno(String ano) {
		List<String> snames = new ArrayList<String>();
		try {
			conn=getConnection();
			psmt= conn.prepareStatement(SQL.SELECT_FILE_BY_SNAME);
			psmt.setString(1, ano);
			rs=psmt.executeQuery();
			while(rs.next()) {
				snames.add(rs.getString(1));
			}
			closeAll();
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return snames;
		
	}
	
	//파일 다운로드
	public FileDTO chooseFile(String fno) {
		FileDTO fileDTO = null;
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(SQL.CHOOSE_FILE_BY_FNO);
			psmt.setString(1, fno);
			
			rs=psmt.executeQuery();
			
			if(rs.next()) {
				fileDTO = new FileDTO();
				fileDTO.setFno(rs.getInt(1));
				fileDTO.setAno(rs.getInt(2));
				fileDTO.setoName(rs.getString(3));
				fileDTO.setsName(rs.getString(4));
				fileDTO.setDownload(rs.getInt(5));
				fileDTO.setRdate(rs.getString(6));
				
			}
			closeAll();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return fileDTO;
	}
	public void updateFileDownloadCount(String fno) {
		try {
			conn=getConnection();
			psmt= conn.prepareStatement(SQL.UPDATE_FILE_DOWNLOAD_COUNT);
			psmt.setString(1, fno);
			psmt.executeUpdate();
			closeAll();
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
}