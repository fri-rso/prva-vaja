package si.jaklic.adverts;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class CommentsHealthCheck implements HealthCheck {
    private static final Logger LOG = Logger.getLogger(CommentsHealthCheck.class.getSimpleName());

    @Override
    public HealthCheckResponse call() {
        try {
            ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();
            String url = configurationUtil.get("orders.comments-url").orElse("");

            LOG.info("Trying to check health of " + url);

            HttpURLConnection connection = (HttpURLConnection) new URL(url + "/comments").openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(CommentsHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }

        return HealthCheckResponse.named(CommentsHealthCheck.class.getSimpleName()).down().build();
    }
}