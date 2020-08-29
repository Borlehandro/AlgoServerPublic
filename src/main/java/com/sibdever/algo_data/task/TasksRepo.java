package com.sibdever.algo_data.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepo extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

}
