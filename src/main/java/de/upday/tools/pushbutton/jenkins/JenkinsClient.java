package de.upday.tools.pushbutton.jenkins;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;
import java.util.Map;

public class JenkinsClient {

    private final String jenkinsBaseUrl;
    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    public JenkinsClient(String jenkinsBaseUrl, String username, String password) {
        this(jenkinsBaseUrl, username, password, true);
    }

    /**
     * @param jenkinsBaseUrl The base uri to your jenkins installation (without trailing slash)
     * @param username       The username to use the API with
     * @param password       The password (or API token) to use the API with
     * @param xsrf           Set this to true if jenkins is configured with XSRF protection enabled (default)
     */
    public JenkinsClient(String jenkinsBaseUrl, String username, String password, boolean xsrf) {
        this.jenkinsBaseUrl = jenkinsBaseUrl;

        addAuthHeaderInterceptor(username, password);

        if (xsrf) {
            addCrumbHeaderInterceptor(jenkinsBaseUrl);
        }
    }

    public JenkinsJobListResponse loadJobs() {
        return query("view/haralds_view_of_the_world/", JenkinsJobListResponse.class);
    }

    /**
     * Queries the given path and deserializes the response from jenkins into the given type.
     * The path can contain urlVariables (e.g. "{foo}"), provide the value as variable argument list
     */
    public <T> T query(String path, Class<T> responseType, Object... urlVariables) {
        return restTemplate.getForObject(jenkinsBaseUrl + path + "/api/json", responseType, urlVariables);
    }

    /**
     * Starts a job and returns the URL to the started build when successful
     */
    public URI startJob(String jobPath) {
        return startJob(jobPath, null);
    }

    /**
     * Starts a job with parameters and returns the URL to the started build when successful
     */
    public URI startJob(String jobPath, Map<String, String> jobParameters) {
        // convert to MultiValueMap, so RestTemplate sends a form POST (see FormHttpMessageConverter)
        MultiValueMap<String, String> formPostParams = new LinkedMultiValueMap<>();

        if (jobParameters != null) {
            jobParameters.forEach(formPostParams::set);
        }

        String endpoint = formPostParams.size() > 0 ? "/buildWithParameters" : "/build";
        return restTemplate.postForLocation(jobPath + endpoint, formPostParams, String.class);
    }

    private void addAuthHeaderInterceptor(String username, String password) {
        final String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("Authorization", authHeaderValue);
            return execution.execute(request, body);
        });
    }


    /**
     * Adds an interceptor to the current restTemplate that issues another request to jenkins
     * requesting a crumb which will be added to the actual request as header value
     * (required if jenkins is configured with XSRF protection enabled)
     */
    private void addCrumbHeaderInterceptor(String jenkinsBaseUrl) {
        restTemplate.getInterceptors().add((request, body, execution) -> {
            if (request.getMethod() == HttpMethod.POST) {
                final JenkinsCrumb crumb = restTemplate.getForObject(jenkinsBaseUrl + "/crumbIssuer/api/json", JenkinsCrumb.class);

                HttpHeaders headers = request.getHeaders();
                headers.set(crumb.crumbRequestField, crumb.crumb);
            }

            return execution.execute(request, body);
        });
    }
}
