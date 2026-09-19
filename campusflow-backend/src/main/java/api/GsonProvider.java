package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonProvider {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    out.value(value != null ? value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
                }
                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    String val = in.nextString();
                    return val != null ? LocalDateTime.parse(val) : null;
                }
            })
            .create();
}