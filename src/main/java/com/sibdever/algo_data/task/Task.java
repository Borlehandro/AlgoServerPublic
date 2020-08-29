package com.sibdever.algo_data.task;

import com.sibdever.algo_data.point.Point;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    // Todo This id must be difficult to predict!
    @Id
    @Column(nullable = false)
    private long taskId;

    @OneToMany(mappedBy = "task")
    private Set<Point> points;

    private String taskDescRu, taskDescEn, taskDescZh;

    private String choice1Ru, choice1En, choice1Zh;

    private String choice2Ru, choice2En, choice2Zh;

    private String choice3Ru, choice3En, choice3Zh;

    @Column(nullable = false)
    private int rightChoice;

    public long getTaskId() {
        return taskId;
    }

    public String getTaskDescRu() {
        return taskDescRu;
    }

    public String getTaskDescEn() {
        return taskDescEn;
    }

    public String getTaskDescZh() {
        return taskDescZh;
    }

    public String getChoice1Ru() {
        return choice1Ru;
    }

    public String getChoice1En() {
        return choice1En;
    }

    public String getChoice1Zh() {
        return choice1Zh;
    }

    public String getChoice2Ru() {
        return choice2Ru;
    }

    public String getChoice2En() {
        return choice2En;
    }

    public String getChoice2Zh() {
        return choice2Zh;
    }

    public String getChoice3Ru() {
        return choice3Ru;
    }

    public String getChoice3En() {
        return choice3En;
    }

    public String getChoice3Zh() {
        return choice3Zh;
    }

    public boolean isRight(int choice) {
        return choice == rightChoice;
    }

    public Set<Point> infoPoints() {
        return points;
    }
}
