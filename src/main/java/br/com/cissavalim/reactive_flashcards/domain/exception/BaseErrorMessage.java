package br.com.cissavalim.reactive_flashcards.domain.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_NOT_FOUND = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOWED = new BaseErrorMessage("generic.methodNotAllowed");

    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params){
        this.params = ArrayUtils.clone(params);
        return this;
    }

    public String getMessage(){
        var message = tryToGetMessageFromBundle();
        if(ArrayUtils.isNotEmpty(params)){
            final var format = new MessageFormat(message);
            message = format.format(params);
        }
        return message;
    }

    private String tryToGetMessageFromBundle() {
        return getResourceBundle().getString(key);
    }

    public ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }
}
