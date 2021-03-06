package tw.dp103g3.comment;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import com.mysql.cj.xdevapi.Type;

import tw.dp103g3.shop.Shop;

import static tw.dp103g3.main.Common.PASSWORD;
import static tw.dp103g3.main.Common.CLASS_NAME;
import static tw.dp103g3.main.Common.URL;
import static tw.dp103g3.main.Common.USER;



public class CommentDaoMySqlImpl implements CommentDao {

	public CommentDaoMySqlImpl() {
		super();
		try {
			Class.forName(CLASS_NAME);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int insert(Comment comment, Shop shop){
		int count = 0;
		String sqlComment = "INSERT INTO `comment` (cmt_score, cmt_detail, shop_id, mem_id, cmt_state,"
				+ " cmt_feedback)" + "VALUES (?, ?, ?, ?, ?, ?);";
		String sqlShop	= " UPDATE `shop` SET shop_ttscore = ?, shop_ttrate = ? "
				+ "WHERE shop_id = ?;";
		Connection connection = null;
		PreparedStatement psComment = null;
		PreparedStatement psShop = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			connection.setAutoCommit(false);
			psComment = connection.prepareStatement(sqlComment);
			psShop = connection.prepareStatement(sqlShop);
			psComment.setInt(1, comment.getCmt_score());
			psComment.setString(2, comment.getCmt_detail());
			psComment.setInt(3, comment.getShop_id());
			psComment.setInt(4, comment.getMem_id());
			psComment.setInt(5, comment.getCmt_state());
			psComment.setString(6, comment.getCmt_feedback());
			psShop.setInt(1, shop.getTtscore());
			psShop.setInt(2, shop.getTtrate());
			psShop.setInt(3, shop.getId());
			
			if (psComment.executeUpdate() == psShop.executeUpdate()) {
				count = 1;
			} else {
				count = 0;
			}
			
			connection.commit();
		} catch (SQLException e) {
			try {
			connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (psComment != null) {
					psComment.close();
				}
				if (psShop != null) {
					psShop.close();
				}
				if (connection != null) {
					connection.setAutoCommit(true);
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		return count;
	}

	@Override
	public int update(Comment comment, Shop shop) {
		int count = 0;
		String sqlComment = "UPDATE `comment` SET cmt_score = ?, cmt_detail = ?, cmt_state = ?, cmt_feedback = ?"
				+ " WHERE cmt_id = ?;";
		String sqlShop = "UPDATE `shop` SET shop_ttrate = ?, shop_ttscore = ? WHERE shop_id = ?;";
		Connection connection = null;
		PreparedStatement psComment = null;
		PreparedStatement psShop = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			connection.setAutoCommit(false);
			psComment = connection.prepareStatement(sqlComment);
			psShop = connection.prepareStatement(sqlShop);
			psComment.setInt(1, comment.getCmt_score());
			psComment.setString(2, comment.getCmt_detail());
			psComment.setInt(3, comment.getCmt_state());
			psComment.setString(4, comment.getCmt_feedback());
			psComment.setInt(5, comment.getCmt_id());
			psShop.setInt(1, shop.getTtrate());
			psShop.setInt(2, shop.getTtscore());
			psShop.setInt(3, shop.getId());
			
			if (psComment.executeUpdate() == psShop.executeUpdate()) {
				count = 1;
			} else {
				count = 0;
			}
			
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (psComment != null) {
					psComment.close();
				}
				if (psShop != null) {
					psShop.close();
				}
				if (connection != null) {
					connection.setAutoCommit(true);
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public Comment findByCommentId(int cmt_id) {
		String sql = "SELECT cmt_id, cmt_score, cmt_detail, shop_id, mem_id"
				+ ", cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state "
				+ "FROM `comment` WHERE cmt_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		Comment comment = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, cmt_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int cmtId = rs.getInt(1);
				int cmt_score = rs.getInt(2);
				String cmt_detail = rs.getString(3);
				int shop_id = rs.getInt(4);
				int mem_id = rs.getInt(5);
				int cmt_state = rs.getInt(6);
				String cmt_feedback = rs.getString(7);
				Date cmt_time = rs.getTimestamp(8);
				Date cmt_feedback_time = rs.getTimestamp(9);
				int cmt_feedback_state = rs.getInt(10);
				comment = new Comment(cmtId, cmt_score, cmt_detail, shop_id, mem_id
						, cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state);
			}
			return comment;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return comment;
	}

	@Override
	public List<Comment> findByCase(int id, String type, int state) {
		String sqlPart = "";
		switch (type) {
			case "member":
				sqlPart = "mem_id";
				break;
			case "shop":
				sqlPart = "shop_id";
				break;
			default: 
				return null;
		}
		String sql = null;
		sql = "SELECT cmt_id, cmt_score, cmt_detail, shop_id, mem_id"
				+ ", cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state "
				+ "FROM `comment` WHERE " + sqlPart + " = ? AND cmt_state = ? ORDER BY cmt_time DESC;";
		Connection connection = null;
		PreparedStatement ps = null;
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, state);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int cmt_id = rs.getInt(1);
				int cmt_score = rs.getInt(2);
				String cmt_detail = rs.getString(3);
				int shop_id = rs.getInt(4);
				int mem_id = rs.getInt(5);
				int cmt_state = rs.getInt(6);
				String cmt_feedback = rs.getString(7);
				Date cmt_time = rs.getTimestamp(8);
				Date cmt_feedback_time = rs.getTimestamp(9);
				int cmt_feedback_state = rs.getInt(10);
				Comment comment = new Comment(cmt_id, cmt_score, cmt_detail, shop_id,
						mem_id, cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state);
				commentList.add(comment);
			}
			return commentList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return commentList;
	}

	@Override
	public List<Comment> findByCase(int id, String type) {
		String sqlPart = "";
		switch (type) {
			case "member":
				sqlPart = "mem_id";
				break;
			case "shop":
				sqlPart = "shop_id";
				break;
			default: 
				return null;
		}
		String sql = null;
		sql = "SELECT cmt_id, cmt_score, cmt_detail, shop_id, mem_id"
				+ ", cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state "
				+ "FROM `comment` WHERE " + sqlPart + " = ? ORDER BY cmt_time DESC;";
		Connection connection = null;
		PreparedStatement ps = null;
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int cmt_id = rs.getInt(1);
				int cmt_score = rs.getInt(2);
				String cmt_detail = rs.getString(3);
				int shop_id = rs.getInt(4);
				int mem_id = rs.getInt(5);
				int cmt_state = rs.getInt(6);
				String cmt_feedback = rs.getString(7);
				Date cmt_time = rs.getTimestamp(8);
				Date cmt_feedback_time = rs.getTimestamp(9);
				int cmt_feedback_state = rs.getInt(10);
				Comment comment = new Comment(cmt_id, cmt_score, cmt_detail, shop_id, mem_id
						, cmt_state, cmt_feedback, cmt_time, cmt_feedback_time, cmt_feedback_state);
				commentList.add(comment);
			}
			return commentList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return commentList;
	}

	@Override
	public int reply(Comment comment) {
		int count = 0;
		String sql = "UPDATE `comment` SET cmt_feedback = ?, cmt_feedback_time = ?, cmt_feedback_state = ?"
				+ " WHERE cmt_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setString(1, comment.getCmt_feedback());
			if (comment.getCmt_feedback_time() == null) {
				ps.setNull(2, Types.TIMESTAMP);
			} else {
				ps.setTimestamp(2, new Timestamp(comment.getCmt_feedback_time().getTime()));
			}
			
			ps.setInt(3, comment.getCmt_feedback_state());
			ps.setInt(4, comment.getCmt_id());
			count = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}

}
