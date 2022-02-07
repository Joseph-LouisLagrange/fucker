package org.example.packet;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface InterceptedHttpServletResponse extends HttpServletResponse {
    void setUrl(String url);

    String getUrl();

    String getTextContent();

    void setTextContent(String textContent);
}
