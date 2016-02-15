package com.leela.pubnub;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class PubnubSubscriber {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PubnubSubscriber.class);

    private static String publishKey = null;
    private static String subscribeKey = null;

    private static void setKeys() {
        if (publishKey == null || subscribeKey == null) {
            try {
                final InputStream inputStream = new FileInputStream(
                        "pubnub.properties");
                final Properties properties = new Properties();
                properties.load(inputStream);
                subscribeKey = properties.getProperty("SUBSCRIBE-KEY");
                publishKey = properties.getProperty("PUBLISH-KEY");
                inputStream.close();
            } catch (final FileNotFoundException e) {
                LOGGER.error("Error", e);
            } catch (final IOException e) {
                LOGGER.error("Error", e);
            }
        }
    }

    public static void subscribeToChannel(final String channel) {
        setKeys();
        final Pubnub pubnub = new Pubnub(publishKey, subscribeKey);
        try {
            pubnub.subscribe(channel, new Callback() {

                @Override
                public void connectCallback(final String channel,
                        final Object message) {
                    // When subscription is ready
                }

                @Override
                public void disconnectCallback(final String channel,
                        final Object message) {
                    // When listening to a channel is stopped
                }

                @Override
                public void reconnectCallback(final String channel,
                        final Object message) {
                    // When listening is reconnected
                }

                @Override
                public void successCallback(final String channel,
                        final Object message) {
                    // when a message is received on a channel of subscription
                }

                @Override
                public void errorCallback(final String channel,
                        final PubnubError error) {
                    // When there's a error in subscription
                }
            });
        } catch (final PubnubException e) {
            LOGGER.error("Error", e);
        }
    }
}
