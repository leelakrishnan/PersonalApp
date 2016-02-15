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

public class PubnubPublisher {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PubnubPublisher.class);

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

    public static void publishMessageToChannel(final String channel,
            final String message) {
        setKeys();
        final Pubnub pubnub = new Pubnub(publishKey, subscribeKey);
        final Callback callback = new Callback() {
            @Override
            public void successCallback(final String channel,
                    final Object response) {
                // when message is published successfully
            }

            @Override
            public void errorCallback(final String channel,
                    final PubnubError error) {
                // when error occurs in publishing
            }
        };
        pubnub.publish(channel, message, callback);
    }
}
