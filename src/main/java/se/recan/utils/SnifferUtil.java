package se.recan.utils;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * 2013-feb-06
 *
 * @author Anders Recksén (recan)
 */
public class SnifferUtil implements Filter {

    private static final Logger LOGGER = Logger.getLogger("Sniffer");

    protected static final String LINE_BREAK = "+---------------------------------\n";

    private static boolean SHOW_HEADER = ResourceUtil.SHOW_HEADER;
    private static boolean SHOW_PARAM = ResourceUtil.SHOW_PARAM;
    private static boolean SHOW_ATTR = ResourceUtil.SHOW_ATTR;
    private static boolean SHOW_SESS = ResourceUtil.SHOW_SESS;

    private static StringBuilder builder;

    @Override
    public void init(FilterConfig filterConfig) {}
    
    private void setParams(HttpServletRequest request) {
        if(request.getParameter("SHOW_HEADER") != null) {
            SHOW_HEADER = Boolean.parseBoolean(request.getParameter("SHOW_HEADER"));
        }
        if(request.getParameter("SHOW_PARAM") != null) {
            SHOW_PARAM = Boolean.parseBoolean(request.getParameter("SHOW_PARAM"));
        }
        if(request.getParameter("SHOW_ATTR") != null) {
            SHOW_ATTR = Boolean.parseBoolean(request.getParameter("SHOW_ATTR"));
        }
        if(request.getParameter("SHOW_SESS") != null) {
            SHOW_SESS = Boolean.parseBoolean(request.getParameter("SHOW_SESS"));
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        setParams(req);

        builder = new StringBuilder();
        builder.append("\n");
        builder.append("\n");
        
        builder.append(LINE_BREAK);
        builder.append("| ");
        builder.append(req.getRequestURI());
        builder.append("\n");

        logHeader(req, res);
        logParameters(req, res);
        logAttributes(req, res);
        logSessions(req, res);
    
        builder.append(LINE_BREAK);

        LOGGER.debug(builder.toString());
        
        req.getRequestDispatcher(req.getServletPath()).forward(req, res);
    }
    
    

    public static void logHeader(HttpServletRequest req, HttpServletResponse res) {
        if (SHOW_HEADER) {
            builder.append(LINE_BREAK);
            builder.append("| H e a d e r s \n");

            builder.append("| ");
            builder.append("Method");
            builder.append(" => ");
            builder.append(req.getHeader(req.getMethod()));
            builder.append("\n");

            for (Enumeration e = req.getHeaderNames(); e.hasMoreElements();) {
                String key = (String) e.nextElement();

                builder.append("| ");
                builder.append(String.format("%-20s", key));
                builder.append(" => ");
                builder.append(req.getHeader(key));
                builder.append("\n");

            }
        }
    }

    public static void logParameters(HttpServletRequest req, HttpServletResponse res) {
        if (SHOW_PARAM) {
            builder.append(LINE_BREAK);
            builder.append("| P a r a m e t r a r \n");
            for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
                String key = (String) e.nextElement();

                builder.append("| ");
                builder.append(String.format("%-20s", key));
                builder.append(" => ");
                builder.append(req.getParameter(key));
                builder.append("\n");

            }
        }
    }

    public static void logAttributes(HttpServletRequest req, HttpServletResponse res) {
        if (SHOW_ATTR) {
            builder.append(LINE_BREAK);
            builder.append("| A t t r i b u t \n");
            for (Enumeration e = req.getAttributeNames(); e.hasMoreElements();) {
                String key = (String) e.nextElement();

                builder.append("| ");
                builder.append(String.format("%-20s", key));
                builder.append(" => ");
                builder.append(req.getAttribute(key));
                builder.append("\n");

            }
        }
    }

    public static void logSessions(HttpServletRequest req, HttpServletResponse res) {
        if (SHOW_SESS) {
            builder.append(LINE_BREAK);
            builder.append("| S e s s i o n e r \n");

            HttpSession session = req.getSession();
            java.util.Enumeration<String> sessionName = session.getAttributeNames();

            while (sessionName.hasMoreElements()) {
                String key = sessionName.nextElement();
                builder.append("| ");
                builder.append(String.format("%-20s", key));
                builder.append(" => ");
                try {
                    builder.append(session.getAttribute(key));
                } catch (NullPointerException npe) {
                    builder.append("");
                }
                builder.append("\n");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
