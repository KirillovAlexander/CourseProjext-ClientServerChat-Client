package ru.netology.client;

import junit.framework.TestCase;
import org.mockito.Mockito;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientTest extends TestCase {
    private static String SETTING_TEST_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Settings.txt";

    public void testGetSetting() {
        //given:
        Setting setting = new Setting("localhost", 23334);

        //then:
        assertThat(Client.getSetting(SETTING_TEST_PATH), is(equalTo(setting)));
        assertThat(Client.getSetting(""), is(equalTo(null)));
    }

    public void testChatting() throws IOException{
        //given:
        SocketChannel socketChannel = Mockito.mock(SocketChannel.class);
        String exitMessage = "/exit\r\n";
        InputStream stdin = System.in;

        //then:
        try {
            System.setIn(new ByteArrayInputStream(exitMessage.getBytes()));
            Scanner scanner = new Scanner(System.in);
            assertThat(Client.chatting(scanner, socketChannel), is(equalTo(false)));
        } finally {
            System.setIn(stdin);
        }
    }
}