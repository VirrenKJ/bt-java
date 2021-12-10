package com.bug.tracker.common.object;

public class APP_CONST {

  public static final class HEADER {

    public static final String AUTHORIZATION = "Authorization";

    public static final String TENANT_HEADER = "x-tenant";

    public static final String HTTP_ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS";

    public static final String HTTP_ALLOWED_HEADERS = "Content-Type, X-Requested-With, accept, Origin, Access-Control-Request-Method, "
            + "Access-Control-Request-Headers," + AUTHORIZATION + "," + TENANT_HEADER;
  }
}
