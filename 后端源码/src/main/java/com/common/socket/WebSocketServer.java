package com.common.socket;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/api/pushMessage")
public class WebSocketServer {

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private static Session session;

    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        WebSocketServer.session = session;
    }

    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {

    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {

    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    public static void sendMessage(String message) {
        if (WebSocketServer.session == null) {
            return;
        }
        try {
            WebSocketServer.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
