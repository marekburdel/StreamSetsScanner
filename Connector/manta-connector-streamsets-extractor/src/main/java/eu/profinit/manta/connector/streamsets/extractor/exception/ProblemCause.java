package eu.profinit.manta.connector.streamsets.extractor.exception;

/**
 *
 * @author mburdel
 */
public enum ProblemCause {
    /** Username should be already set but it wasn't. */
    EMPTY_USERNAME("Username was not specified."),
    /** Password should be already set but it wasn't. */
    EMPTY_PASSWORD("Password was not specified."),
    /** Sending an HTTP request to a StreamSets Server failed. */
    FAILED_HTTP_REQUEST("Failed to execute HTTP request to StreamSets Server."),
    /** Return status from the server is different than the expected one. */
    UNEXPECTED_RETURN_STATUS("StreamSets Server returned unexpected status."),
    /** Response from StreamSets server can't be turned into JSON. */
    RESPONSE_NOT_IN_JSON_FORMAT("Response from StreamSets server is not in the JSON format."), EMPTY_URI(
            "URL to StreamSets Server was not specified."), BAD_URI(
            "URL to StreamSets Server was specified but it's not a valid URL."),
    ;

    private final String message;

    /**
     * Creates new cause of an extraction problem.
     * @param message user-friendly description of the problem
     */
    ProblemCause(String message) {
        this.message = message;
    }

    @Override public String toString() {
        return message;
    }
}
