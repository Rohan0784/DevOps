package hello;

/** A greeting returned by the sample REST endpoint. */
public class Greeting {
    /** Unique greeting identifier. */
    private final long id;
    /** Greeting message. */
    private final String content;

    /** Create a greeting with an identifier and message. */
    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    /** Return the greeting identifier. */
    public long getId() {
        return id;
    }

    /** Return the greeting message. */
    public String getContent() {
        return content;
    }
}
