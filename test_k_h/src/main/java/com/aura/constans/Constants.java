package com.aura.constans;

public interface Constants {

    /**
     * 作业的运行模式，默认是dev
     */
    String SPARK_JOB_RUN_MODE = "spark.job.run.mode";

    String SPARK_JOB_SESSION_TASK_ID = "spark.job.session.task.id";
    String SPARK_JOB_PRODUCT_TASK_ID = "spark.job.product.task.id";

    //param字段
    String PARAM_START_DATE = "startDate";
    String PARAM_END_DATE = "endDate";
    String PARAM_START_AGE = "startAge";
    String PARAM_END_AGE = "endAge";
    String PARAM_PROFESSIONALS = "professionals";
    String PARAM_CITIES = "cities";
    String PARAM_SEX = "sex";

    //聚合字段
    String FIELD_SESSION_COUNT = "sessionCount";
    String FIELD_STEP_LEN = "stepLen";
    String FIELD_VISIT_LEN = "visitLen";
    String FIELD_SEARCH_KEYWORDS = "searchKeywords";
    String FIELD_SESSION_ID = "sessionId";
    String FIELD_START_TIME = "startTime";
    String FIELD_CLICK_CATEGORY_IDS = "clickCategoryIds";
    String FIELD_ORDER_CATEGORY_IDS = "orderCategoryIds";
    String FIELD_PAY_CATEGORY_IDS = "payCategoryIds";
    String FIELD_AGE = "age";
    String FIELD_CITY = "city";
    String FIELD_PROFESSIONAL = "professional";
    String FIELD_SEX = "sex";

    //聚合统计字段-访问时长
    String TIME_PERIOD_1s_3s = "1s_3s";
    String TIME_PERIOD_4s_6s = "4s_6s";
    String TIME_PERIOD_7s_9s = "7s_9s";
    String TIME_PERIOD_10s_30s = "10s_30s";
    String TIME_PERIOD_30s_60s = "30s_60s";
    String TIME_PERIOD_1m_3m = "1m_3m";
    String TIME_PERIOD_3m_10m = "3m_10m";
    String TIME_PERIOD_10m_30m = "10m_30m";
    String TIME_PERIOD_30m = "30m";
    //聚合统计字段-访问步长
    String STEP_PERIOD_1_3 = "1_3";
    String STEP_PERIOD_4_6 = "4_6";
    String STEP_PERIOD_7_9 = "7_9";
    String STEP_PERIOD_10_30 = "10_30";
    String STEP_PERIOD_30_60 = "30_60";
    String STEP_PERIOD_60 = "60";
}
