#### The process of authentication using OAuth2 explained using Github

1.User makes a GET request to the this server providing redirect url
```
http://localhost:8080/oauth2/authorize/github?redirect_uri=http://localhost:3000/oauth2/redirect
```

2.The server redirects the user to Github authorization page and provides clientId and redirect url (this time this is the url that goes to the server)
```
https://github.com/login/oauth/authorize?response_type=code&client_id=CLIENT_ID&scope=user&state=STATE_VALUE&redirect_uri=http://localhost:8080/oauth2/callback/github
```

3.When user logged in to Github and provided access to its account then Github redirects using the redirect url from the step 2. The redirect url contains a code param which is a temporary code for issuing an access token. The temporary code will expire in 10 mins
```
http://localhost:8080/oauth2/callback/github?code=TEMPORARY_CODE&state=STATE_VALUE
```

4.Then the server makes a POST request to the Github to exchange the temporary for an access token. The request contains the temporary code, clientId and other possible params
```
https://github.com/login/oauth/access_token?code=TEMPORARY_CODE&client_id=CLIENT_ID&state=STATE_VALUE
```

5.When the server receives an access token issued by Github the server make a request to get some info about the user (user's email) and register this user in the application. Then the server issues a JWT token

6.The JWT token is sent to the frontend via a redirect using the redirect url provided in the step 1
```
http://localhost:3000/oauth2/redirect?token=JWT_TOKEN
```