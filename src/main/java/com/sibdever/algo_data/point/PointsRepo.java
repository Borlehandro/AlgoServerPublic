package com.sibdever.algo_data.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointsRepo extends JpaRepository<Point, Long> {

    @Query("select p from Point p where p.code = ?1")
    Point findByCode(String code);

    @Query("select count(p) from Point p where p.picName = ?1")
    int countByPicture(String pictureName);

}
