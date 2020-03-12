import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
}