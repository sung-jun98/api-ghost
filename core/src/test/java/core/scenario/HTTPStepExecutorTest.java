package core.scenario;

import com.apighost.model.scenario.request.Request;
import com.apighost.model.scenario.step.*;
import com.apighost.scenario.executor.HTTPStepExecutor;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link HTTPStepExecutor}.
 * <p>
 * This test verifies basic HTTP GET execution logic, including:
 * <ul>
 *     <li>HTTP status code matching</li>
 *     <li>Route matching based on expected status</li>
 *     <li>Execution result construction</li>
 * </ul>
 * <p>
 * Uses a known external endpoint (https://httpbin.org/status/200) to simulate a simple successful request.
 * This helps verify that request execution, response handling, and routing logic are functioning correctly.
 * </p>
 * <p>
 * Note: This test performs a real network call. For isolated testing, consider mocking {@code HttpClient}.
 *
 * @author haazz
 * @version BETA-0.0.1
 */
class HTTPStepExecutorTest {

    @Test
    void testExecute_simpleGetRequest_returnsSuccess() throws Exception {
        HTTPStepExecutor executor = new HTTPStepExecutor();

        Step step = new Step.Builder()
            .type(ProtocolType.HTTP)
            .request(new Request.Builder()
                .method(HTTPMethod.GET)
                .url("https://httpbin.org/status/200")
                .build())
            .route(List.of(new Route.Builder()
                .expected(new Expected.Builder().status("200").build())
                .then(new Then.Builder().step(null).build())
                .build()))
            .build();

        var result = executor.execute("step1", step, Map.of(), 2000);

        assertEquals("step1", result.getStepName());
        assertEquals(200, result.getStatus());
        assertTrue(result.getIsRequestSuccess());
        assertNull(result.getNextStep());
    }
}

