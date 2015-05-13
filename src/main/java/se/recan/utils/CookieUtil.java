package se.recan.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

/**
 * @author Anders Recksén
 */
 
public class CookieUtil {

    /**
     * 
     */
    public static final int AGE_DAY = 60 * 60 * 24;
    /**
     *
     */
    public static final int AGE_WEEK = AGE_DAY * 7;
    /**
     *
     */
    public static final int AGE_MONTH = AGE_DAY * 30;
    /**
     *
     */
    public static final int AGE_YEAR = AGE_DAY * 365;
    /**
     *
     */
    public static final String PATH = "/";

    // Returned Cookie must be set to response.
    // actionContext.getResponse().addCookie(cookie);
    /**
     *
     * @param name
     * @param value
     * @return
     */
    public static Cookie setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(AGE_MONTH);
        cookie.setPath(PATH);
        
        return cookie;
    }
    
    /**
     *
     * @param request
     * @param cookieName
     * @param value
     */
    public static void setCookieValue(HttpServletRequest request, String cookieName, String value) {
        Cookie[] cookies = request.getCookies();

        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue(value);
                    break;
                }
            }
        }
    }

    /**
     * Search for a cookie. If found, return it's value, else return default value
     *
     * @param cookies
     * @param cookieName
     * @param defaultValue
     * @return Cookie value or default
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (cookies == null) {
            return (defaultValue);
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }

        return (defaultValue);
    }

    /**
     *
     * @param request
     * @param cookieName
     * @param defaultValue
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return (defaultValue);
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }

        return (defaultValue);
    }

    /**
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    returnCookie = cookie;
                    break;
                }
            }
        }

        return returnCookie;
    }

//    public static void delete(HttpServletRequest request, Cookie cookie) throws Exception {
//        cookie.setMaxAge(-1);
//        cookie.setPath(PATH);
//        response.addCookie(cookie);
//    }

//    public static void delete(HttpServletRequest request, String cookieName) throws Exception {
//        Cookie c = getCookie(request, cookieName);
//        if(c!=null) {
//            delete(request, c);
//        }
//    }

    // Debug pourpose
    /**
     *
     * @param request
     */
    public static void printCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("Cookie: " + cookie.getName() + ", Value: " + cookie.getValue() + ", Max age: " + cookie.getMaxAge() + ", Comment: " + cookie.getComment());
        }
    }
}