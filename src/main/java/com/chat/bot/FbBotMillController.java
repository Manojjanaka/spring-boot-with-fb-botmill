package com.chat.bot;

import co.aurasphere.botmill.fb.FbBotMillContext;
import co.aurasphere.botmill.fb.internal.util.json.FbBotMillJsonUtils;
import co.aurasphere.botmill.fb.model.incoming.MessageEnvelope;
import co.aurasphere.botmill.fb.model.incoming.MessengerCallback;
import co.aurasphere.botmill.fb.model.incoming.MessengerCallbackEntry;
import co.aurasphere.botmill.fb.model.incoming.handler.IncomingToOutgoingMessageHandler;
//import com.chat.bot.bot.Amy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Manoj Janaka
 * @version 1.0
 * @since 7/4/17
 * 
 * This call is used to replace the FbBotMillServlet webhook  
 * please remove the dispatchServletRegistration in BotApplication if you are using this
 */

@RestController
@RequestMapping(path = "/webhook123")
public class FbBotMillController {

	private static final Logger logger = LoggerFactory.getLogger(FbBotMillController.class);

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Get received!");
		String validationToken = FbBotMillContext.getInstance().getValidationToken();
		Map<String, String[]> parameters = req.getParameterMap();
		String hubMode = safeUnwrapGetParameters(parameters.get("hub.mode"));
		String hubToken = safeUnwrapGetParameters(parameters.get("hub.verify_token"));
		String hubChallenge = safeUnwrapGetParameters(parameters.get("hub.challenge"));
		if (hubMode.equals("subscribe") && hubToken.equals(validationToken)) {
			logger.info("Subscription OK.");
			resp.setStatus(200);
			resp.setContentType("text/plain");
			resp.getWriter().write(hubChallenge);
		} else {
			logger.warn("GET received is not a subscription or wrong validation token. Ensure you have set the correct validation token using FbBotMillContext.getInstance().setup(String, String).");
			resp.sendError(403);
		}

	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("POST received!");
		new MessengerCallback();
		String json = readerToString(req.getReader());
		logger.debug("JSON input: " + json);

		MessengerCallback callback;
		try {
			callback = FbBotMillJsonUtils.fromJson(json, MessengerCallback.class);
		} catch (Exception var10) {
			logger.debug("Error during MessengerCallback parsing: " + var10);
			return;
		}

		if (callback != null) {
			logger.debug("callback not null");
			List<MessengerCallbackEntry> callbackEntries = callback.getEntry();
			if (callbackEntries != null) {

				for (MessengerCallbackEntry entry : callbackEntries) {
					List<MessageEnvelope> envelopes = entry.getMessaging();
					if (envelopes != null) {
						logger.debug("envelopes found"+envelopes.size() +"   "+ Arrays.toString(envelopes.toArray()));
						MessageEnvelope lastEnvelope = envelopes.get(envelopes.size() - 1);
						IncomingToOutgoingMessageHandler.getInstance().process(lastEnvelope);

					}
				}
			}
		}

		resp.setStatus(200);
	}

	private static String safeUnwrapGetParameters(String[] parameter) {
		return parameter != null && parameter[0] != null ? parameter[0] : "";
	}

	protected static String readerToString(Reader reader) throws IOException {
		char[] arr = new char[8192];
		StringBuilder buffer = new StringBuilder();

		int numCharsRead;
		while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
			buffer.append(arr, 0, numCharsRead);
		}

		return buffer.toString();
	}
}
