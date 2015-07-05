# URL Shortener


### Problem

Create HTTP service used to shorten URLs, with following functionalities:
- Register web address (API)
- Redirect client according to shorten URL
- Visits statistic

##### 1. Basic architecture
Service has two parts: configuration and client.

##### 1.1 Configuration
Using REST call with JSON parameters, call configuration to:
- Open account
- Register URL
- Fetch statistic

###### 1.1.1 Open account
Parameter | Description
--- | ---
HTTP method | POST
URI | /account
Request Type | application/json
Request Body | JSON object with following parameters:<ul><li>`AccountId (String, required)`</li></ul>*Example: {"AccountId : "myAccountId"}*
Response Type | application/json
Response | There is a difference between successful and unsuccessful registration. Unsuccessful registration happens only when respective account ID already exists. Parameters are following: <ul><li>`success: true | false`</li><li>`description: Status description, e.g. Account with that ID already exist`</li><li>`password: Returned only when account is successfully opened. Automatically generated password 8 alphanumeric characters long.`</li></ul> *Example {"success":"true", "description":"Your account is opened", password: "xC345Fc"'}*

###### 1.1.2 Register URL
Parameter | Description
--- | ---
HTTP method | POST
URI | /register
Request Type | application/json
Request Headers | Set Authorization header, and authorize user
Request Body | JSON object with following parameters:	<ul><li>`url (required, url to be shorten)`</li><li>`redirectType : 301 | 302 (not required, default 302)`</li></ul> *Example: {"url": "http://stackoverflow.com/questions/1567929/website-safe-data-access-architecture-question?rq=1", "redirectType":"301"}*
Response Type | application/json
Response | Response parameters in case of successful registration are following: <ul><li>`shortUrl (shorten URL)`</li></ul> *Example: {"shortUrl":"http://short.com/xYswlE"}*

###### 1.1.3 Fetch statistic
Parameter | Description
--- | ---
HTTP method | GET
URI | /statistic/{AccountId}
Request Headers | Set Authorization header, and authorize user
Response Type | application/json
Response | Server responds with JSON object, that is map key:value, where key is registered URL, and value is number of calls to it. *Example {"http://myweb.com/someverylongdirectory/someotherdirectory/":"10", "http://myweb.com/someverylongdirectory2/someotherdirectory2/":"4", "http://myweb.com/someverylongdirectory3/someotherdirectory3/":"91"}*

##### 1.2	Redirect
Redirect client to configured address with configured HTTP status.

##### 2. General requirements
-	Use Java programing language
-	Pay attention that response HTTP statuses are according to REST standards (list of statuses http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
- Application must not require any additional configuration, that is, it must not contain any external dependencies. It must work on first run.
- According to previous request, it is not allowed to use databases, unless they are embedded in application itself.
- Create help page `(uri: /help)` containing run, setup and use instructions
- Deliver source code preferably as maven project

### Solution

##### 1.1.1 
Added one more case for unsuccessful registration. It happens when `accountId` passed is empty, as it can be seen in `/help`. Every other check is not covered in this task.
##### 1.1.2
Authorization Header is base64 encoded according to RFC2617.
If user authorization fails `Response` is empty, but the status is corresponding, as stated in `/help`.
Response is also empty when url passed is empty.
##### 1.1.3
Statistic is fetched only for urls which respective account registered.

##### Addition
If some addresses get registered, and then application gets undeployed and deployed on another port or url link, access to those short urls will be lost. That happens because 1.1.2 requires shortUrl to be absolutePath, and as such it is saved to database(embedded). It is possible to save only 6-digit generated short url, but then it would not be consistent to user, holding on by the rule to pass him absolute path. That way it could be possible to appended 6-digit short url, on any deployed link, and validly redirect.

Also done and what was not within the scope of task are: logs, regular exception handling... Database is used, and although it is embedded, cache is used for demonstration. There are only few cache instances, so loading is done as simple as possible. For more of them there needs be better approach.

Not many queries are used, so they are written inline. Another way is using QueryRepository.

There was not a lot of paying attention to data security, so password is saved as it is passed to user. For larger complexity, md5 hash is recommended.

It is not neccessary to depend on libraries, on such a small project like this, for writing JSON object, generating random string and other things.

---
##### This is a perfect example of Java development task for a job interview.
