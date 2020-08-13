package com.mindology.app.models;

import java.util.Date;

public class Notification extends BaseModel implements Comparable<Notification>
{
    private Date createdDate;
    private String content;
    private boolean read;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public int compareTo(Notification notification) {
        if (isRead())
        {
//            if (notification.isRead())
//            {
//                return getCreatedDate().compareTo(notification.getCreatedDate());
//            }
//            else
//            {
                return 1;
//            }
        }
        else
        {
//            if (notification.isRead())
//            {
                return 0;
//            }
//            else
//            {
//                return getCreatedDate().compareTo(notification.getCreatedDate());
//            }
        }
    }
}
