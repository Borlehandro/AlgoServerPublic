package com.sibdever.algo_data.point;

import com.sibdever.algo_data.tables.PointToQuestItem;
import com.sibdever.algo_data.task.Task;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "points")
public class Point {

    @Id
    private long pointId;

    @OneToMany(mappedBy = "point")
    private Set<PointToQuestItem> items;

    private String descName, picName, nameRu, nameEn, nameZh;

    @Column (nullable = false)
    private String code;

    private double latitude, longitude;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public static class ShortPoint {

        ShortPoint(Point point) {

            this.picName = point.picName;
            this.nameRu = point.nameRu;
            this.nameEn = point.nameEn;
            this.nameZh = point.nameZh;

            this.latitude = point.latitude;
            this.longitude = point.longitude;
        }

        private String picName, nameRu, nameEn, nameZh;

        private double latitude, longitude;

        public String getNameEn() {
            return nameEn;
        }

        public String getNameRu() {
            return nameRu;
        }

        public String getPicName() {
            return picName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getNameZh() {
            return nameZh;
        }
    }

    public ShortPoint cut() {
        return new ShortPoint(this);
    }

    public long getPointId() {
        return pointId;
    }

    public String getDescName() {
        return descName;
    }

    public String getPicName() {
        return picName;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameZh() {
        return nameZh;
    }

    public String getCode() {
        return code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Task infoTask() {
        return task;
    }

    public long getTaskId() {
        return task.getTaskId();
    }
}