package guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class JdbcContext {

	private final DataSource dataSource;

	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int update(String sql, Object... parameters) { // ... 가변 파라미터(마지막 파라미터만 가능)
		return updateWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);

				// parameter binding
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}

				return pstmt;
			}
		});
	}

	private int updateWithStatementStrategy(StatementStrategy statementStrategy) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			pstmt = statementStrategy.makeStatement(conn);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch (SQLException ignore) {
			}

		}
	}

	public <E> List<E> query(String sql, RowMapper<E> rowMapper) {
		return queryWithStatementStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				return connection.prepareStatement(sql);
			}

		}, rowMapper);
	}

	private <E> List<E> queryWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
		List<E> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DataSourceUtils.getConnection(dataSource);
			pstmt = statementStrategy.makeStatement(conn);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				E e = rowMapper.mapRow(rs, rs.getRow());
				result.add(e);
			}
			
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException(e); // try catch를 강제하지 않는다. (application이 처리하도록 위로 던진다)
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch (SQLException ignore) {
			}
		}
	}

	public <E> E queryForObject(String sql, Object[] parameters, RowMapper<E> rowMapper) {
		return queryForObjectWithStatementStrategy(new StatementStrategy() {

			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);

				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}

				return pstmt;
			}

		}, rowMapper);
	}

	private <E> E queryForObjectWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			pstmt = statementStrategy.makeStatement(conn);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rowMapper.mapRow(rs, rs.getRow());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e); // try catch를 강제하지 않는다. (application이 처리하도록 위로 던진다)
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch (SQLException ignore) {
			}
		}

		return null;
	}
}
