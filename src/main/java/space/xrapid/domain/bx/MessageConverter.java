package space.xrapid.domain.bx;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MessageConverter implements HttpMessageConverter<Response> {

    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return new ArrayList();
    }

    @Override
    public Response read(Class<? extends Response> aClass, HttpInputMessage httpInputMessage)
            throws IOException, HttpMessageNotReadableException {
        String json = IOUtils.toString(httpInputMessage.getBody(), StandardCharsets.UTF_8.name());

        return new ObjectMapper().readValue(json, Response.class);
    }

    @Override
    public void write(Response response, MediaType mediaType, HttpOutputMessage httpOutputMessage)
            throws IOException, HttpMessageNotWritableException {
    }
}
