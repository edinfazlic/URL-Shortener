package data;

import model.Account;
import model.URLInfo;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public class Provider {
	URLSCache urlsCache;
	AccountsCache accountsCache;

	public Provider() {
		createTables();
		accountsCache = QueryRunner.loadAccountsCache();
		urlsCache = QueryRunner.loadURLSCache();
	}

	/**
	 * Creates tables in database on initialization.
	 */
	private void createTables() {
		//data definition language - syntax
		String ddl = "CREATE TABLE IF NOT EXISTS accounts" +
				"(id TEXT PRIMARY KEY NOT NULL," +
				"password TEXT NOT NULL);" +

				"CREATE TABLE IF NOT EXISTS urls" +
				"(url TEXT PRIMARY KEY NOT NULL," +
				"redirect_type INT DEFAULT 302," +
				"short_url TEXT UNIQUE NOT NULL," +
				"counter INT DEFAULT 0," +
				"account_id TEXT NOT NULL," +
				"FOREIGN KEY (account_id) REFERENCES accounts(id));";

		QueryRunner.update(ddl);
	}

	//region Caches
	public boolean checkAuthorization(String accountId, String password) {
		return accountsCache.containsKey(accountId) && accountsCache.getObject(accountId).getPassword()
				.equals(password);
	}

	public boolean accountIdExists(String accountId) {
		return accountsCache.containsKey(accountId);
	}

	public boolean shortUrlExists(String shortUrl) {
		return urlsCache.getByShortUrl(shortUrl) != null;
	}

	public URLInfo getUrlInfo(String url) {
		return urlsCache.getObject(url);
	}

	public URLInfo getUrlInfoByShortUrl(String shortUrl) {
		return urlsCache.getByShortUrl(shortUrl);
	}

	public Map<String, URLInfo> getUrlsByAccountId(String accountId) {
		return urlsCache.getByAccountId(accountId);
	}
	//endregion Caches

	public void createAccount(Account account) {
		accountsCache.put(account.getAccountId(), account);
		QueryRunner.update("insert into ACCOUNTS values(\"" + account.getAccountId() + "\",\"" + account
				.getPassword() + "\")");
	}

	public void createURL(URLInfo urlInfo) {
		urlsCache.put(urlInfo.getUrl(), urlInfo);
		QueryRunner.update("insert into URLS (URL, SHORT_URL, REDIRECT_TYPE, ACCOUNT_ID) " +
				"values (\"" + urlInfo.getUrl() + "\",\"" + urlInfo.getShortUrl() + "\",\"" + urlInfo
				.getRedirectType() + "\",\"" + urlInfo.getAccountId() + "\")");
	}

	public void updateURL(URLInfo urlInfo) {
		urlsCache.updateRedirectType(urlInfo.getUrl(), urlInfo.getRedirectType());
		QueryRunner.update("update URLS set REDIRECT_TYPE = \"" + urlInfo.getRedirectType() + "\"" +
				"where url = \"" + urlInfo.getUrl() + "\"");
	}

	public void incrementCounter(String url) {
		int count = urlsCache.getObject(url).getCounter() + 1;
		urlsCache.getObject(url).setCounter(count);
		QueryRunner.update("update URLS set COUNTER = \"" + count + "\" where URL = \"" + url + "\"");
	}
}
