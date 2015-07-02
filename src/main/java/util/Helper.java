package util;

import model.URLInfo;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public class Helper {
	/**
	 * Candidate characters for random selection
	 */
	private static final String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom random = new SecureRandom();

	/**
	 * Generates random alphanumeric String.
	 *
	 * @param len length of a generated string
	 * @return random alphanumeric string for given length
	 */
	public static String generateRandomString(Integer len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(symbols.charAt(random.nextInt(symbols.length())));

		return sb.toString();
	}

	/**
	 * Example: {"success":"false","description":"Account with that ID already exists."}
	 *
	 * @return Json response as String for Account.
	 */
	public static String generateAccountResponse(boolean success, String message, String password) {
		return "{\"success\":\"" + success + "\"," +
				"\"description\":\"" + message + "\"" +
				(success ? ",\"password\":\"" + password + "\"}" : "}");
	}

	/**
	 * Generates json object. Example {"http://www.myweb.com/abc/d":"2","http://www.myweb.com/trt/page":"15"}
	 *
	 * @param urls map of urls, similar to cache
	 * @return json object as String with key:value representation of parameters
	 */
	public static String generateStatisticResponse(Map<String, URLInfo> urls) {
		StringBuilder sb = new StringBuilder("{");
		for (String key : urls.keySet())
			sb.append("\"").append(urls.get(key).getUrl()).append("\":\"").append(urls.get(key).getCounter())
					.append("\",");

		return sb.length() == 1 ? "" : sb.deleteCharAt(sb.length() - 1).append("}").toString();
	}

	/**
	 * Generates json object. Example {"shortUrl":"http://localhost:8080/xYswlE"}
	 *
	 * @return json object as String with parameter shortUrl
	 */
	public static String generateURLResponse(String shortUrl) {
		return "{\"shortUrl\":\"" + shortUrl + "\"}";
	}

	/**
	 * Decodes Authorization header in a request.
	 *
	 * @return decoded header, should be in format "account:password"
	 */
	public static String decodeHeader(String header) {
		return new String(Base64.decodeBase64(header.substring(6).getBytes()));
	}
}
