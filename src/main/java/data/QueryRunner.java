package data;

import model.Account;
import model.URLInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public class QueryRunner {
	/**
	 * In charge of loading cache for Accounts on Provider initialization.
	 */
	public static AccountsCache loadAccountsCache() {
		Statement stmt = null;
		Connection c = null;
		AccountsCache ac = new AccountsCache();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:core.db");
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("select * from ACCOUNTS");
			while (rs.next()) {
				ac.put(rs.getString("ID"), new Account(rs.getString("ID"), rs.getString("PASSWORD")));
			}
		} catch (Exception e) {
			//log - e
		} finally {
			handleClosing(stmt, c);
		}
		return ac;
	}

	/**
	 * In charge of loading cache for URLS on Provider initialization.
	 */
	public static URLSCache loadURLSCache() {
		Statement stmt = null;
		Connection c = null;
		URLSCache uc = new URLSCache();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:core.db");
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("select * from URLS");
			while (rs.next()) {
				uc.put(rs.getString("URL"), new URLInfo(rs.getString("URL"), rs.getInt("REDIRECT_TYPE"),
						rs.getString("SHORT_URL"), rs.getInt("COUNTER"), rs.getString("ACCOUNT_ID")));
			}
		} catch (Exception e) {
			//log - e
		} finally {
			handleClosing(stmt, c);
		}
		return uc;
	}

	private static void handleClosing(Statement stmt, Connection c) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			//log - "SQLException: Statement " + st, e
		} finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				//log - "SQLException: Connection " + c, e
			}
		}
	}

	/**
	 * Executes a update query in embedded database.
	 *
	 * @return Number of updated rows.
	 */
	public static int update(String query) {
		Statement stmt = null;
		Connection c = null;
		int rows = 0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:core.db");
			stmt = c.createStatement();

			rows = stmt.executeUpdate(query);
		} catch (Exception e) {
			//log - e
		} finally {
			handleClosing(stmt, c);
		}

		return rows;
	}
}
