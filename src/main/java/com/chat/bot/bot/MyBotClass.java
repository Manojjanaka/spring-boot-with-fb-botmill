package com.chat.bot.bot;

import co.aurasphere.botmill.core.annotation.Bot;
import co.aurasphere.botmill.fb.FbBot;
import co.aurasphere.botmill.fb.autoreply.AutoReply;
import co.aurasphere.botmill.fb.autoreply.MessageAutoReply;
import co.aurasphere.botmill.fb.event.FbBotMillEventType;
import co.aurasphere.botmill.fb.model.annotation.FbBotMillController;
import co.aurasphere.botmill.fb.model.incoming.MessageEnvelope;
import co.aurasphere.botmill.fb.model.outcoming.FbBotMillResponse;
import co.aurasphere.botmill.fb.model.outcoming.factory.ReplyFactory;

/**
 * @author Manoj Janaka
 * @version 1.0
 * @since 7/4/17
 */
@Bot
public class MyBotClass extends FbBot {

    @FbBotMillController(eventType = FbBotMillEventType.MESSAGE_PATTERN, pattern = "(?i:hi)|(?i:hello)|(?i:hey)|(?i:good day)|(?i:home)")
    public void replyWithQuickReply(MessageEnvelope envelope) {
        reply(new AutoReply() {
            @Override
            public FbBotMillResponse createResponse(MessageEnvelope envelope) {
                return ReplyFactory.addTextMessageOnly("Text message with quick replies")
                        .addQuickReply("Quick reply 1", "Payload for quick reply 1").build(envelope);
            }
        });
    }
}
