package com.sibdever.algo_data;

import com.sibdever.algo_data.point.Point;
import com.sun.istack.Nullable;

public class QuestStatus {

    // Todo NOT_CHANGED? Really?
    public enum StatusType {
        NOT_STARTED,
        NEW,
        IN_PROGRESS_NOT_FINISHED,
        IN_PROGRESS_AGAIN,
        FINISHED,
        FINISHED_FIRST_TIME,
        FINISHED_AGAIN,
        NOT_CHANGED,
        ERROR
    }

    private StatusType status;
    private Point.ShortPoint point;

    public QuestStatus(StatusType status, @Nullable Point point) {
        this.status = status;
        this.point = point!=null ? point.cut() : null;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Point.ShortPoint getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point.cut();
    }

}