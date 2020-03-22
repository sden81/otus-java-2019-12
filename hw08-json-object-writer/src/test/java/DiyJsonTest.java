import com.google.gson.Gson;
import diyJson.DiyJson;
import org.junit.Before;
import org.junit.Test;
import testClass.Person;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiyJsonTest {
    protected Person testPerson;

    @Before
    public void createPerson() {
        this.testPerson = Person.createPerson();
    }

    @Test
    public void testConvertToJson() {
        Gson gson = new Gson();
        String gsonJson = gson.toJson(testPerson);

        DiyJson diyJson = new DiyJson();
        String diyJsonResult = diyJson.toJson(testPerson);

        assertEquals(gsonJson, diyJsonResult);
    }

    @Test
    public void testNullConvert(){
        Gson gson = new Gson();
        String gsonJson = gson.toJson(null);

        DiyJson diyJson = new DiyJson();
        String diyJsonResult = diyJson.toJson(null);

        assertEquals(gsonJson, diyJsonResult);
    }

    @Test
    public void test() {
        Gson gson = new Gson();
        DiyJson diyJson = new DiyJson();

        assertEquals(gson.toJson(null), diyJson.toJson(null));
        assertEquals(gson.toJson((byte)1), diyJson.toJson((byte)1));
        assertEquals(gson.toJson((short)1f), diyJson.toJson((short)1f));
        assertEquals(gson.toJson(1), diyJson.toJson(1));
        assertEquals(gson.toJson(1L), diyJson.toJson(1L));
        assertEquals(gson.toJson(1f), diyJson.toJson(1f));
        assertEquals(gson.toJson(1d), diyJson.toJson(1d));
        assertEquals(gson.toJson("aaa"), diyJson.toJson("aaa"));
        assertEquals(gson.toJson('a'), diyJson.toJson('a'));
        assertEquals(gson.toJson(new int[] {1, 2, 3}), diyJson.toJson(new int[] {1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2 ,3)), diyJson.toJson(List.of(1, 2 ,3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), diyJson.toJson(Collections.singletonList(1)));

    }
}