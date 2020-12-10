package game.messages;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private boolean isSuccessful;
    private String description;
    private T content;

    public Result(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public Result(boolean isSuccessful, T content) {
        this.isSuccessful = isSuccessful;
        this.content = content;
    }

    public Result(boolean isSuccessful, String description) {
        this.isSuccessful = isSuccessful;
        this.description = description;
    }

    public T getContent() {
        return content;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getDescription() {
        return description;
    }
}
