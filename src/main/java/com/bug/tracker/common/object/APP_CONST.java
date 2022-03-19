package com.bug.tracker.common.object;

public class APP_CONST {

  public static final String BASE_URL_BACKEND = "http://localhost:8080";
  public static final String BASE_URL_FRONTEND = "http://localhost:4200";
  public static final String[] ALLOWED_ENDPOINTS = {"/authenticate", "/user/username", "/user/add", "/user/send-token",
          "/user/validate-token", "/user/reset-password", "/role/list", "/user/email"};

  public static final class HEADER {

    public static final String AUTHORIZATION = "Authorization";

    public static final String TENANT_HEADER = "x-tenant";

    public static final String USER_ID = "user-id";

    public static final String ROLE = "role";

    public static final String HTTP_ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS";

    public static final String HTTP_ALLOWED_HEADERS = "Content-Type, X-Requested-With, accept, Origin, Access-Control-Request-Method, "
            + "Access-Control-Request-Headers," + AUTHORIZATION + "," + TENANT_HEADER + "," + USER_ID + "," + ROLE;
  }
}
