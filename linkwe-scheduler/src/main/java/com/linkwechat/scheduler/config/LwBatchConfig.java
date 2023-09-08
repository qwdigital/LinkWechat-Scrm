//package com.linkwechat.scheduler.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.launch.support.SimpleJobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * 批处理相关配置
// */
//@Configuration
//public class LwBatchConfig {
//    private Logger logger = LoggerFactory.getLogger(LwBatchConfig.class);
//
//
//    /**
//     * JobRepository定义：Job的注册容器以及和数据库打交道（事务管理等）
//     * @param dataSource
//     * @param transactionManager
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public JobRepository myJobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
//        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
//        jobRepositoryFactoryBean.setDatabaseType("mysql");
//        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
//        jobRepositoryFactoryBean.setDataSource(dataSource);
//        return jobRepositoryFactoryBean.getObject();
//    }
//
//
//    /**
//     * jobLauncher定义： job的启动器,绑定相关的jobRepository
//     * @param dataSource
//     * @param transactionManager
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public SimpleJobLauncher myJobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        FlatFileItemReader
//        // 设置jobRepository
//        jobLauncher.setJobRepository(myJobRepository(dataSource, transactionManager));
//        return jobLauncher;
//    }
//
//    /**
//     * 定义job
//     * @param jobs
//     * @param myStep
//     * @return
//     */
//    @Bean
//    public Job myJob(JobBuilderFactory jobs, Step myStep){
//        return jobs.get("myJob")
//                .incrementer(new RunIdIncrementer())
//                .flow(myStep)
//                .end()
//                .listener(myJobListener())
//                .build();
//    }
//
//}
