import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vavr.collection.List;
import io.vavr.gson.VavrGson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SerializationTest {
    private final Logger log = LoggerFactory.getLogger(SerializationTest.class);

    @Test
    public void asdf1() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("testing 1");
        arrayList.add("testing 2");
        arrayList.add("testing 3");

        log.info(new Gson().toJson(arrayList));
    }

    @Test
    public void asdf2() {
        List<String> vavrList = List.of(
                "vavr testing 1",
                "vavr testing 2",
                "vavr testing 3");

        log.info(new Gson().toJson(vavrList));
    }

    @Test
    public void asdf3() {
        List<String> vavrList = List.of(
                "vavr-gson testing 1",
                "vavr-gson testing 2",
                "vavr-gson testing 3");

        GsonBuilder builder = new GsonBuilder();
        VavrGson.registerAll(builder);
        Gson gson = builder.create();
        log.info(gson.toJson(vavrList));
    }
}
