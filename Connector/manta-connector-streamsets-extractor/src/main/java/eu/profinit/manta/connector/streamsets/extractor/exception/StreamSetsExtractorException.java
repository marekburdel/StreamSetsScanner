package eu.profinit.manta.connector.streamsets.extractor.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author mburdel
 */
public class StreamSetsExtractorException extends Exception implements Serializable {

    private static final long serialVersionUID = 8846782005663667256L;

    private final Set<ProblemCause> causes;

    /**
     * Creates new extract exception.
     * @param description description of the exception
     * @param cause enum describing the cause that lead to throwing this exception
     */
    public StreamSetsExtractorException(ProblemCause cause, String description) {
        super(description);
        this.causes = Collections.singleton(cause);
    }

    /**
     * Creates new wrapper extract exception.
     * @param cause enum describing the cause that lead to throwing this exception
     * @param e exception that caused the problem
     */
    public StreamSetsExtractorException(ProblemCause cause, Throwable e) {
        super(e);
        this.causes = Collections.singleton(cause);
    }

    /**
     * Creates new extract exception.
     * @param causes all causes that lead to throwing this exception
     */
    public StreamSetsExtractorException(Set<ProblemCause> causes) {
        this.causes = causes;
    }

    @Override public String toString() {
        if (causes.isEmpty()) {
            return super.toString();
        }
        String causesString = causes.stream().map(ProblemCause::toString).collect(Collectors.joining(" "));
        return getMessage() == null ? causesString : causesString + " " + getMessage();
    }
}
