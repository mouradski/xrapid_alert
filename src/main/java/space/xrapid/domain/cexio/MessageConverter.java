package space.xrapid.domain.cexio;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class MessageConverter implements HttpMessageConverter<Trade[]> {

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
  public Trade[] read(Class<? extends Trade[]> aClass, HttpInputMessage httpInputMessage)
      throws IOException, HttpMessageNotReadableException {
    String json = IOUtils.toString(httpInputMessage.getBody(), StandardCharsets.UTF_8.name());

    return new ObjectMapper().readValue(json, Trade[].class);
  }

  @Override
  public void write(Trade[] trades, MediaType mediaType, HttpOutputMessage httpOutputMessage)
      throws IOException, HttpMessageNotWritableException {

  }
}

