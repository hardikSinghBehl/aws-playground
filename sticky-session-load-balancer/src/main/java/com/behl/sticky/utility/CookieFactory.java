package com.behl.sticky.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieFactory {

	private static final String COOKIE_NAME = "STICKY_UNTIL_OTP_EXPIRATION";

	/**
	 * Sends a cookie with specified expiration time to the browser client
	 * 
	 * @param expirationSeconds:   until cookie will be valid
	 * @param httpServletResponse: response to which cookie has to be added
	 */
	public static void insert(final Integer expirationSeconds, final HttpServletResponse httpServletResponse) {
		final Cookie cookie = new Cookie(COOKIE_NAME, "true");
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setComment(
				"meant to navigate user to the same server instance for OTP verfication until OTP expiration");
		cookie.setMaxAge(expirationSeconds);
		httpServletResponse.addCookie(cookie);
	}

	/**
	 * Method used to invalidate cookie
	 * 
	 * @param httpServletResponse: response from which cookie has to be expired
	 */
	public static void remove(final HttpServletResponse httpServletResponse) {
		final Cookie cookie = new Cookie(COOKIE_NAME, "true");
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setMaxAge(0); // setting max-age as 0 to expire cookie
		httpServletResponse.addCookie(cookie);
	}

}
