package parser.scenario.flattener;

import com.apighost.parser.flattener.JsonFlattener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JsonFlattener}.
 * <p>
 * This test suite verifies the correct flattening of JSON strings into key-value maps using dot and
 * bracket notation. Covers simple objects, nested structures, arrays, various primitive types, null
 * values, and invalid input handling.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
class JsonFlattenerTest {

    private final JsonFlattener flattener = new JsonFlattener(new ObjectMapper());

    /**
     * Tests flattening of a simple one-level JSON object with string and integer values.
     */
    @Test
    void testFlatten_simpleJson() {
        String json = """
            {
              "name": "Alice",
              "age": 30
            }
            """;

        Map<String, Object> result = flattener.flatten(json);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get("name"));
        assertEquals(30, result.get("age"));
    }

    /**
     * Tests flattening of nested JSON objects into dot notation keys.
     */
    @Test
    void testFlatten_nestedJson() {
        String json = """
            {
              "user": {
                "id": 1,
                "info": {
                  "email": "alice@example.com"
                }
              }
            }
            """;

        Map<String, Object> result = flattener.flatten(json);

        assertEquals(2, result.size());
        assertEquals(1, result.get("user.id"));
        assertEquals("alice@example.com", result.get("user.info.email"));
    }

    /**
     * Tests flattening of a JSON array into bracket notation keys.
     */
    @Test
    void testFlatten_arrayJson() {
        String json = """
            {
              "roles": ["admin", "user"]
            }
            """;

        Map<String, Object> result = flattener.flatten(json);

        assertEquals(2, result.size());
        assertEquals("admin", result.get("roles[0]"));
        assertEquals("user", result.get("roles[1]"));
    }

    /**
     * Tests flattening of a JSON object containing multiple primitive types, including boolean,
     * double, and null.
     */
    @Test
    void testFlatten_mixedJson() {
        String json = """
            {
              "active": true,
              "score": 95.5,
              "meta": null
            }
            """;

        Map<String, Object> result = flattener.flatten(json);

        assertEquals(3, result.size());
        assertEquals(true, result.get("active"));
        assertEquals(95.5, result.get("score"));
        assertNull(result.get("meta"));
    }

    /**
     * Tests behavior when invalid JSON is provided. Expects a {@link RuntimeException} to be
     * thrown.
     */
    @Test
    void testFlatten_invalidJson_shouldThrowException() {
        String invalidJson = "{ invalid json }";

        assertThrows(RuntimeException.class, () -> flattener.flatten(invalidJson));
    }
}
