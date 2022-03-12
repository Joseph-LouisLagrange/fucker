package org.example.packet;


import javax.activation.MimeType;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

public interface Packet extends Cloneable {
    AtomicLong INC = new AtomicLong(0);

    Charset getCharset();

    void setCharset(Charset charset);

    MimeType getMimeType();

    void setMimeType(MimeType mimeType);

    Long getID();
}
