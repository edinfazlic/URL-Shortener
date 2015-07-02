package service;

import data.Provider;
import model.Account;
import model.URLInfo;
import util.Constants;
import util.Helper;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/")
public class Service {
	private Provider provider = new Provider();

	@POST
	@Path("/account")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAccount(Account account) {
		boolean success = true;
		String description = Constants.ACCOUNT_OPENED;
		Integer status = 201;

		account.setAccountId(account.getAccountId().toLowerCase().trim());

		if ("".equals(account.getAccountId())) {
			success = false;
			status = 415;
			description = Constants.MISSING_ACCOUNT_ID;
		} else if (provider.accountIdExists(account.getAccountId())) {
			success = false;
			status = 409;
			description = Constants.ACCOUNT_EXISTS;
		} else {
			account.setPassword(Helper.generateRandomString(8));
			provider.createAccount(account);
		}

		String response = Helper.generateAccountResponse(success, description, account.getPassword());
		return Response.status(status).entity(response).build();
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerURL(@Context UriInfo uriInfo, @HeaderParam("Authorization") String header, URLInfo urlInfo) {
		String decoded = Helper.decodeHeader(header);
		String accountId = decoded.substring(0, decoded.indexOf(":"));
		String password = decoded.substring(decoded.indexOf(":") + 1);

		if (!provider.checkAuthorization(accountId, password))
			return Response.status(401).build();

		urlInfo.setUrl(urlInfo.getUrl().trim());

		if ("".equals(urlInfo.getUrl()))
			return Response.status(415).build();

		urlInfo.setAccountId(accountId);
		if (!Integer.valueOf(301).equals(urlInfo.getRedirectType()) && !Integer.valueOf(
				301).equals(urlInfo.getRedirectType()))
			urlInfo.setRedirectType(302);
		URLInfo existing = provider.getUrlInfo(urlInfo.getUrl());

		if (existing == null) {
			urlInfo.setShortUrl(uriInfo.getBaseUri() + Helper.generateRandomString(6));
			while (provider.shortUrlExists(urlInfo.getShortUrl()))
				urlInfo.setShortUrl(uriInfo.getBaseUri() + Helper.generateRandomString(6));
			provider.createURL(urlInfo);
			return Response.status(201).entity(Helper.generateURLResponse(urlInfo.getShortUrl())).build();
		}

		if (!existing.getRedirectType().equals(urlInfo.getRedirectType()))
			provider.updateURL(urlInfo);

		return Response.status(200).entity(Helper.generateURLResponse(existing.getShortUrl())).build();
	}

	@GET
	@Path("/statistic/{accountId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(@HeaderParam("Authorization") String header,
	                                  @PathParam("accountId") String accountId) {
		String decoded = Helper.decodeHeader(header);
		String accId = decoded.substring(0, decoded.indexOf(":"));
		String password = decoded.substring(decoded.indexOf(":") + 1);

		if (!provider.checkAuthorization(accId, password) || !accId.equals(accountId))
			return Response.status(401).build();

		String response = Helper.generateStatisticResponse(provider.getUrlsByAccountId(accountId));

		return Response.status(200).entity(response).build();
	}

	@GET
	@Path("/{shortUrl}")
	public Response redirect(@Context UriInfo uriInfo, @PathParam("shortUrl") String shortUrl) {
		URLInfo urlInfo = provider.getUrlInfoByShortUrl(uriInfo.getBaseUri() + shortUrl);
		if (urlInfo != null) {
			provider.incrementCounter(urlInfo.getUrl());
			return Response.seeOther(URI.create(urlInfo.getUrl())).status(urlInfo.getRedirectType()).build();
		}

		return Response.status(404).build();
	}

	@GET
	@Path("/help")
	@Produces(MediaType.TEXT_HTML)
	public String help() {
		return Constants.HELP_PAGE;
	}
}