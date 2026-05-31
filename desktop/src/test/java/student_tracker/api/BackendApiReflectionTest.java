package student_tracker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class BackendApiReflectionTest {
    @Test
    void enc_encodesSpaces() throws Exception {
        BackendApi api = new BackendApi(new ObjectMapper());
        Method enc = BackendApi.class.getDeclaredMethod("enc", String.class);
        enc.setAccessible(true);
        String out = (String) enc.invoke(api, "a b+c@/");
        // ensure spaces are encoded
        assertTrue(out.contains("%20") || out.contains("%2B"));
    }
}
