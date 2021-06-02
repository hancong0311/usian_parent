package com.usian.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Title: ReceiveMessageLog
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/6/1 10:23
 */
@Table(name = "receive_message_log")
public class ReceiveMessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
