package com.dfirago.jsh.aws;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.dfirago.jsh.aws.web.rest.model.ActionRequest;
import com.dfirago.jsh.aws.web.rest.model.ActionResponse;
import com.dfirago.jsh.aws.web.rest.service.JarvisHubService;
import com.dfirago.jsh.aws.web.rest.service.RestServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by dmfi on 16/01/2017.
 */
public class JarvisSmartHomeSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(JarvisSmartHomeSpeechlet.class);

    private JarvisHubService jarvisHubService = RestServiceFactory.createService(JarvisHubService.class);

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    }


    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        String intentName = request.getIntent().getName();
        switch (intentName) {
            case "ActionIntent":
                return handleActionRequest(request);
            case "AMAZON.HelpIntent":
                return handleHelpRequest();
            case "AMAZON.StopIntent":
            case "AMAZON.CancelIntent":
                return handleSessionEndRequest();
            default:
                throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getWelcomeResponse() {
        String speechOutput = "Welcome to Jarvis Smart Home.";
        String repromptText = "With Jarvis Smart Home, you can control your home appliances."
                + " For example, you could ask Jarvis to turn off the kitchen light, if you added such module.";
        return newAskResponse(speechOutput, false, repromptText, false);
    }

    private SpeechletResponse handleHelpRequest() {
        String speechOutput = "With Jarvis Smart Home, you can control your home appliances." +
                " For example, you could ask Jarvis to turn off the kitchen light, if you added such module." +
                " To add a new module, you should install Android application, connect it to the Jarvis Smart Hub and" +
                " go through the module addition wizard.";
        return newTellResponse(speechOutput, true);
    }

    private SpeechletResponse handleSessionEndRequest() {
        String speechOutput = "Thank you for using Jarvis Smart Home. Goodbye!";
        return newTellResponse(speechOutput, true);
    }

    private SpeechletResponse handleActionRequest(IntentRequest request) {
        Intent intent = request.getIntent();
        String action = intent.getSlot("Action").getValue();
        String subject = intent.getSlot("Subject").getValue();
        ActionRequest actionRequest = new ActionRequest(action, subject);
        Call<ActionResponse> responseCall = jarvisHubService.postActionRequest(actionRequest);
        String speechOutput;
        try {
            log.info("Sending action request: {}", actionRequest);
            Response<ActionResponse> response = responseCall.execute();
            log.info("Response received:", response);
            if (response.isSuccessful()) {
                log.error("Request processing completed successfully");
                speechOutput = String.format("You asked me to %s %s. Request completed successfully.", action, subject);
            } else {
                log.error("Request processing failed");
                speechOutput = String.format("Failed to %s %s. ", action, subject);
            }
        } catch (IOException e) {
            log.error("Failed to post action request", e);
            speechOutput = "Something went wrong.";
        }
        return newTellResponse(speechOutput, false);
    }

    private SpeechletResponse newAskResponse(String outputText, boolean isOutputSsml, String repromptText, boolean isRepromptSsml) {
        Reprompt reprompt = new Reprompt();
        OutputSpeech outputSpeech = getOutputSpeech(outputText, isOutputSsml);
        OutputSpeech repromptOutputSpeech = getOutputSpeech(repromptText, isRepromptSsml);
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

    private SpeechletResponse newTellResponse(String outputText, boolean isOutputSsml) {
        OutputSpeech outputSpeech = getOutputSpeech(outputText, isOutputSsml);
        return SpeechletResponse.newTellResponse(outputSpeech);
    }

    private OutputSpeech getOutputSpeech(String output, boolean isSsml) {
        OutputSpeech outputSpeech;
        if (isSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(output);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(output);
        }
        return outputSpeech;
    }
}
