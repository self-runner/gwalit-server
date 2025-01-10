package com.selfrunner.domainmodule.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.domainmodule.GwaritDomainRoot;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EnableJpaAuditing
@EnableJpaRepositories(basePackageClasses = {GwaritDomainRoot.class})
@EntityScan(basePackageClasses = {GwaritDomainRoot.class})
@Configuration
public class JpaConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
