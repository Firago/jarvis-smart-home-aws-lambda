package com.dfirago.jsh.aws;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmfi on 16/01/2017.
 */
public class JarvisSmartHomeSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds = new HashSet<>();

    public JarvisSmartHomeSpeechletRequestStreamHandler() {
        super(new JarvisSmartHomeSpeechlet(), supportedApplicationIds);
    }

    public JarvisSmartHomeSpeechletRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }
}
