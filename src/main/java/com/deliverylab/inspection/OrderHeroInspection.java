package com.deliverylab.inspection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.deliverylab.inspection.common.AppProperties;

import lombok.Setter;

@Setter
@SpringBootApplication
@Component
@EnableConfigurationProperties(AppProperties.class)
public class OrderHeroInspection {

	public static void main(String[] args) {
		SpringApplication.run(OrderHeroInspection.class, args);
	}
}
