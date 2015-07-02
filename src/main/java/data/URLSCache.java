package data;

import model.URLInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public class URLSCache extends Cache<URLInfo> {
	/**
	 * Gets URLInfo with given shortUrl.
	 *
	 * @return URLInfo if exists, else null
	 */
	public URLInfo getByShortUrl(String shortUrl) {
		for (String key : container.keySet())
			if (container.get(key).getShortUrl().equals(shortUrl))
				return container.get(key);
		return null;
	}

	/**
	 * Updates redirectType for given url.
	 */
	public void updateRedirectType(String url, int redirectType) {
		container.get(url).setRedirectType(redirectType);
	}

	/**
	 * Gets part of cache for given accountId.
	 *
	 * @return map where key is url and value is URLInfo
	 */
	public Map<String, URLInfo> getByAccountId(String accountId) {
		Map<String, URLInfo> urls = new HashMap<>();

		for (String key : container.keySet())
			if (accountId.equals(container.get(key).getAccountId()))
				urls.put(container.get(key).getUrl(), container.get(key));

		return urls;
	}
}
