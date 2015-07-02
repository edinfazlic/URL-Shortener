package util;

/**
 * Created with IntelliJ IDEA.
 * Author: Edin Fazlic
 * Date: 4/19/14
 */
public class Constants {
	public static final String ACCOUNT_OPENED = "Your account is opened.";
	public static final String MISSING_ACCOUNT_ID = "Couldn't find value for parameter 'accountId'.";
	public static final String ACCOUNT_EXISTS = "Account with that ID already exists.";

	/**
	 * /help page
	 */
	public static final String HELP_PAGE = "<style>\n" +
			"table,th,td\n" +
			"{\n" +
			"\tborder:1px solid black;\n" +
			"\tborder-collapse:collapse;\n" +
			"}\n" +
			"th,td\n" +
			"{\n" +
			"\tpadding:5px;\n" +
			"}\n" +
			"</style>\n" +
			"<h3>URL Shortener:</h3>\n" +
			"URL Shortener consists of configuration and user part.\n" +
			"<br/>Configuration serves for:\n" +
			"<ul>\n" +
			"\t<li>Create account</li>\n" +
			"\t<li>Register URL</li>\n" +
			"\t<li>Fetch statistics</li>\n" +
			"</ul>\n" +
			"To access Configuration part use REST call with JSON parameters.\n" +
			"<br/>User part serves for redirection.\n" +
			"<br/>\n" +
			"<br/><b>Installation</b>\n" +
			"<ul>\n" +
			"\t<li>There is no external dependencies, therefore no additional installation is required.</li>\n" +
			"</ul>\n" +
			"<b>Running</b>\n" +
			"<ol>\n" +
			"\t<li>Start a web server.</li>\n" +
			"\t<li>Deploy the service war file to the web server.</li>\n" +
			"</ol>\n" +
			"<b>Usage</b>\n" +
			"<br/>Use a client-side application to access following functionalities. All request and response types are application/json.\n" +
			"<ol>\n" +
			"<li>\n" +
			"\t<u><i>Creating account</i></u>\n" +
			"\t<br/>HTTP Method: <b>POST</b>\n" +
			"\t<br/>URI: <b>/account</b>\n" +
			"\t<br/>Request Body: <b>accountId</b>\n" +
			"\t<ul>\n" +
			"\t\t<li>Example: {\"accountId\":\"myAccount1\"}</li>\n" +
			"\t</ul>\n" +
			"\tResponse Body: <b>success</b>, <b>description</b>, <b>password</b>\n" +
			"\t<ul>\n" +
			"\t\t<li>Example: {\"success\":\"true\",\"description\":\"Your account is opened.\",\"password\":\"xC345Fc0\"}\n" +
			"\t</ul>\n" +
			"\tDescription\n" +
			"\t<ul>\n" +
			"\t\t<li>\"accountId\" is mandatory. It's trimmed and case insensitive.\n" +
			"\t\t<br/>\"success\" can have two values: true and false. False value occurs if \"accountId\" is empty after trimming.\n" +
			"\t\tIn that case status 415 is set. False value also occurs when \"accountId\" already exists. In that case status 409 is set. \n" +
			"\t\tStatus 201 and \"password\" are set only when \"success\" is true. It is random generated 8 alphanumeric characters long string. \n" +
			"\t\t<br/>For each of these cases appropriate description is set.</li>\n" +
			"\t</ul>\n" +
			"</li>\n" +
			"<li>\n" +
			"\t<u><i>Registering URL</i></u>\n" +
			"\t<br/>HTTP Method: <b>POST</b>\n" +
			"\t<br/>URI: <b>/register</b>\n" +
			"\t<br/>Request Header: <b>Authorization</b>\n" +
			"\t<br/>Request Body: <b>url</b>, <b>redirectType</b>\n" +
			"\t<ul>\n" +
			"\t\t<li>Example: {\"url\":\"http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1\",\"redirectType\":\"301\"}</li>\n" +
			"\t</ul>\n" +
			"\tResponse Body: <b>shortUrl</b>\n" +
			"\t<ul>\n" +
			"\t\t<li>Example: {\"shortUrl\":\"http://localhost:8080/xYswlE\"}\n" +
			"\t</ul>\n" +
			"\tDescription\n" +
			"\t<ul>\n" +
			"\t\t<li>\"url\" is shorten and is mandatory. \"redirectType\" is not, it can be 301, but if set as anything else, then it's 302.\n" +
			"\t\t<br/>\"shortUrl\" is not always set. Response Body is set empty when authorization fails, and for that case status is set to 401.\n" +
			"\t\tResponse Body is also set to empty when \"url\" is empty after trimming. In that case status 415 is set. \n" +
			"\t\tIf \"url\" was already registered it is updated and status is set to 201, else it is created and status is set to 200.</li>\n" +
			"\t</ul>\n" +
			"</li>\n" +
			"<li>\n" +
			"\t<u><i>Fetching statistics</i></u>\n" +
			"\t<br/>HTTP Method: <b>GET</b>\n" +
			"\t<br/>URI: <b>/statistic/{accountId}</b>\n" +
			"\t<br/>Request Header: <b>Authorization</b>\n" +
			"\tResponse Body: <b>key:value</b>\n" +
			"\t<ul>\n" +
			"\t\t<li>Example: {\"http://myweb.com/somelongurl/somedirectory/\":\"10\",\"http://myweb.com/somelongurl1/somedirectory2/\":\"4\",\"http://myweb.com/somelongurl3/somedirectory4/\":\"91\"}\n" +
			"\t</ul>\n" +
			"\tDescription\n" +
			"\t<ul>\n" +
			"\t\t<li>Response Body is set empty when authorization fails or account set in Authorization header differs from the one stated in URI, and for that case status is set to 401.\n" +
			"\t\tIn other cases, status is set to 200, Response body is set to object populated with key:value order pairs for each url that that account registered.\n" +
			"\t\tKey represents registered url, and value represents number of times that url was called.</li>\n" +
			"\t</ul>\n" +
			"</li>\n" +
			"<li>\n" +
			"\t<u><i>Redirecting</i></u>\n" +
			"\t<br/>Description\n" +
			"\t<ul>\n" +
			"\t\tEntering shortUrl provided in \"Registering URL\" redirection occurs to registered url and number of calls for that url is incremented by 1.\n" +
			"\t</ul>\n" +
			"</li>\n" +
			"</ol>";

}
