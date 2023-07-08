package com.selfrunner.gwalit.global.util.jwt;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

}


/*
CREATE TABLE `gwalitdb`.`member` (`member_id` bigint NOT NULL AUTO_INCREMENT,`name` varchar(255) NOT NULL,`type` varchar(255) NOT NULL,`phone` varchar(255) NOT NULL,`password` varchar(255) NOT NULL,`school` varchar(255),`grade` varchar(255),`need_notification` boolean NOT NULL, PRIMARY KEY (member_id));
 */