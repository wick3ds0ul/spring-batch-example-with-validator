package com.prabhakar.batch;

import com.prabhakar.model.User;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
public class Processor implements ItemProcessor<User, User>  {

    private static final Map<String, String> DEPT_NAMES =
            new HashMap<>();

    private Long jobExecutionId;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecutionId = stepExecution.getJobExecutionId();
    }

    public Processor() {
        DEPT_NAMES.put("001", "Technology");
        DEPT_NAMES.put("002", "Operations");
        DEPT_NAMES.put("003", "Accounts");
    }

    @Override
    public User process(User user) {
        System.out.println(this.jobExecutionId);
        String deptCode = user.getDept();
        String dept = DEPT_NAMES.get(deptCode);
        user.setDept(dept);
        user.setTime(new Date());
        System.out.println(String.format("Converted from [%s] to [%s]", deptCode, dept));
        return user;
    }

}
