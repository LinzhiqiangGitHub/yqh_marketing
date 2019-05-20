package com.yqh.marketing.service.job;

public interface JobConstant {

    String APPLICATION_NAME = "EXPERT-USER-SERVICE";

    /*
     * 审核状态
     * */
    interface AUDIT_STATUS {

        //待审核
        int CHECK_PENDING = 1;
        //已审核通过
        int APPROVED = 2;
        //未审核通过
        int REJECTED = 3;
    }

    /*
    * 任务审核（达人接取任务后）
    * */
    interface JOB_AUDIT_STATUS {

        //接取任务
        int PICK_UP_THE_TASK = 1;

        //提交任务
        int SUBMIT_JOB = 2;
        //待审核(提交)
        int SUBMIT_JOB_CHECK_PENDING = 3;
        //已审核通过(提交)
        int SUBMIT_JOB_APPROVED = 4;
        //未审核通过(提交)
        int SUBMIT_JOB_REJECTED = 5;

        //完成任务
        int PERFORM = 6;
        //超期完成任务
        int EXCEED_THE_DEADLINE = 7;
        //未完成任务
        int UNFINISHED_TASK = 8;
    }

    /*
     * 任务审核（达人接取任务后）
     * */
    interface COMMIT_JOB_AUDIT_STATUS {

        //待审核(提交)
        int SUBMIT_JOB_CHECK_PENDING = 1;
        //已审核通过(提交)
        int SUBMIT_JOB_APPROVED = 2;
        //未审核通过(提交)
        int SUBMIT_JOB_REJECTED = 3;

        //完成任务
        int PERFORM = 4;
        //超期完成任务
        int EXCEED_THE_DEADLINE = 5;
        //未完成任务
        int UNFINISHED_TASK = 6;
    }
}
